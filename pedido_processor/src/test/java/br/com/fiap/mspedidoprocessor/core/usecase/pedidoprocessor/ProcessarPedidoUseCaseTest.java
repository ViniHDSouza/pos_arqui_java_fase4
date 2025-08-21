package br.com.fiap.mspedidoprocessor.core.usecase.pedidoprocessor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import br.com.fiap.mspedidoprocessor.adapter.mapper.PedidoProcessorMapper;
import br.com.fiap.mspedidoprocessor.core.domain.ItemPedidoProcessor;
import br.com.fiap.mspedidoprocessor.core.domain.PagamentoDTO;
import br.com.fiap.mspedidoprocessor.core.domain.PedidoProcessor;
import br.com.fiap.mspedidoprocessor.core.domain.PedidoStatus;
import br.com.fiap.mspedidoprocessor.core.exception.ClienteNaoEncontradoException;
import br.com.fiap.mspedidoprocessor.core.exception.EstoqueException;
import br.com.fiap.mspedidoprocessor.core.exception.PagamentoException;
import br.com.fiap.mspedidoprocessor.core.exception.ProdutoNaoEncontradoException;
import br.com.fiap.mspedidoprocessor.core.gateways.ClienteServiceGateway;
import br.com.fiap.mspedidoprocessor.core.gateways.EstoqueServiceGateway;
import br.com.fiap.mspedidoprocessor.core.gateways.PagamentoServiceGateway;
import br.com.fiap.mspedidoprocessor.core.gateways.PedidoRepositoryGateway;
import br.com.fiap.mspedidoprocessor.core.gateways.ProdutoServiceGateway;
import br.com.fiap.mspedidoprocessor.core.usecase.pedidoprocessor.CalculoUseCase;
import br.com.fiap.mspedidoprocessor.core.usecase.pedidoprocessor.EstoqueUseCase;
import br.com.fiap.mspedidoprocessor.core.usecase.pedidoprocessor.PagamentoUseCase;
import br.com.fiap.mspedidoprocessor.core.usecase.pedidoprocessor.ProcessarPedidoUseCase;
import br.com.fiap.mspedidoprocessor.core.usecase.pedidoprocessor.ValidacaoUseCase;

class ProcessarPedidoUseCaseTest {

    private PedidoRepositoryGateway pedidoRepository;
    private ClienteServiceGateway clienteGateway;
    private ProdutoServiceGateway produtoGateway;
    private EstoqueServiceGateway estoqueGateway;
    private PagamentoServiceGateway pagamentoGateway;
    private PedidoProcessorMapper mapper;

    private ValidacaoUseCase validacao;
    private CalculoUseCase calculo;
    private EstoqueUseCase estoque;
    private PagamentoUseCase pagamento;

    private ProcessarPedidoUseCase useCase;

    @BeforeEach
    void setup() {
        pedidoRepository = mock(PedidoRepositoryGateway.class);
        clienteGateway = mock(ClienteServiceGateway.class);
        produtoGateway = mock(ProdutoServiceGateway.class);
        estoqueGateway = mock(EstoqueServiceGateway.class);
        pagamentoGateway = mock(PagamentoServiceGateway.class);
        mapper = mock(PedidoProcessorMapper.class);

        validacao = mock(ValidacaoUseCase.class);
        calculo = mock(CalculoUseCase.class);
        estoque = mock(EstoqueUseCase.class);
        pagamento = mock(PagamentoUseCase.class);

        useCase = new ProcessarPedidoUseCase(
                pedidoRepository,
                clienteGateway,
                produtoGateway,
                estoqueGateway,
                pagamentoGateway,
                mapper,
                validacao,
                calculo,
                estoque,
                pagamento
        );
    }

    private PedidoProcessor criarPedido() {
        PedidoProcessor pedido = new PedidoProcessor();
        pedido.setId(UUID.randomUUID());
        pedido.setPedidoReciverId(100L);
        pedido.setClienteId(1L);
        pedido.setNumeroCartao("1234");
        pedido.setItens(List.of(new ItemPedidoProcessor("sku", 1, BigDecimal.ONE, BigDecimal.ONE)));
        return pedido;
    }

    @Test
    void deveProcessarPedidoComSucesso() {
        PedidoProcessor pedido = criarPedido();
        PagamentoDTO pagamentoDTO = new PagamentoDTO(
                pedido.getId().toString(), BigDecimal.TEN, "1234", PedidoStatus.APROVADO
        );

        when(pagamento.processarPagamento(pedido)).thenReturn(pagamentoDTO);

        useCase.processar(pedido);

        verify(validacao).validarCliente(pedido);
        verify(validacao).validarProdutos(pedido);
        verify(calculo).calcularTotal(pedido);
        verify(estoque).baixarEstoque(pedido);
        verify(pagamento).processarPagamento(pedido);
        verify(pedidoRepository, atLeastOnce()).salvar(pedido);
        assertEquals(PedidoStatus.APROVADO, pedido.getStatus());
    }

    @Test
    void deveTratarErroDeEstoque() {
        PedidoProcessor pedido = criarPedido();

        doThrow(new EstoqueException("Erro estoque")).when(estoque).baixarEstoque(pedido);

        assertThrows(EstoqueException.class, () -> useCase.processar(pedido));

        verify(pedidoRepository, atLeastOnce()).salvar(pedido);
        assertEquals(PedidoStatus.FECHADO_SEM_ESTOQUE, pedido.getStatus());
    }

    @Test
    void deveTratarErroDePagamento() {
        PedidoProcessor pedido = criarPedido();

        doNothing().when(estoque).baixarEstoque(pedido);
        doThrow(new PagamentoException("Erro pagamento")).when(pagamento).processarPagamento(pedido);

        assertThrows(PagamentoException.class, () -> useCase.processar(pedido));

        verify(estoque).reverterEstoque(pedido);
        verify(pedidoRepository, atLeastOnce()).salvar(pedido);
        assertEquals(PedidoStatus.FALHA_PAGAMENTO, pedido.getStatus());
    }

    @Test
    void deveTratarClienteNaoEncontrado() {
        PedidoProcessor pedido = criarPedido();

        doThrow(new ClienteNaoEncontradoException(10912L)).when(validacao).validarCliente(pedido);

        assertThrows(ClienteNaoEncontradoException.class, () -> useCase.processar(pedido));

        verify(pedidoRepository, atLeastOnce()).salvar(pedido);
        assertEquals(PedidoStatus.FALHA_CLIENTE_NAOENCONTRADO, pedido.getStatus());
    }

    @Test
    void deveTratarProdutoNaoEncontrado() {
        PedidoProcessor pedido = criarPedido();

        doNothing().when(validacao).validarCliente(pedido);
        doThrow(new ProdutoNaoEncontradoException("produto")).when(validacao).validarProdutos(pedido);

        assertThrows(ProdutoNaoEncontradoException.class, () -> useCase.processar(pedido));

        verify(pedidoRepository, atLeastOnce()).salvar(pedido);
        assertEquals(PedidoStatus.FALHA_PRODUTO_NAOENCONTRADO, pedido.getStatus());
    }
}
