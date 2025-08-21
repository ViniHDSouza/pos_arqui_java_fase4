package br.com.fiap.mspedidoprocessor.core.usecase.pedidoprocessor;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import br.com.fiap.mspedidoprocessor.adapter.external.estoqueservice.dto.EstoqueResponseDTO;
import br.com.fiap.mspedidoprocessor.core.domain.ItemPedidoProcessor;
import br.com.fiap.mspedidoprocessor.core.domain.PedidoProcessor;
import br.com.fiap.mspedidoprocessor.core.exception.ClienteNaoEncontradoException;
import br.com.fiap.mspedidoprocessor.core.exception.EstoqueInsuficienteException;
import br.com.fiap.mspedidoprocessor.core.exception.ProdutoNaoEncontradoException;
import br.com.fiap.mspedidoprocessor.core.gateways.ClienteServiceGateway;
import br.com.fiap.mspedidoprocessor.core.gateways.EstoqueServiceGateway;
import br.com.fiap.mspedidoprocessor.core.gateways.ProdutoServiceGateway;
import br.com.fiap.mspedidoprocessor.core.usecase.pedidoprocessor.ValidacaoUseCase;

public class ValidacaoUseCaseTest {

    private ClienteServiceGateway clienteService;
    private ProdutoServiceGateway produtoService;
    private EstoqueServiceGateway estoqueService;
    private ValidacaoUseCase validacaoUseCase;

    @BeforeEach
    void setup() {
        clienteService = mock(ClienteServiceGateway.class);
        produtoService = mock(ProdutoServiceGateway.class);
        estoqueService = mock(EstoqueServiceGateway.class);
        validacaoUseCase = new ValidacaoUseCase(clienteService, produtoService, estoqueService);
    }

    @Test
    void deveValidarClienteComSucesso() {
        PedidoProcessor pedido = new PedidoProcessor();
        pedido.setClienteId(123L);
        pedido.setPedidoReciverId(1L);

        when(clienteService.clienteExiste(123L)).thenReturn(true);

        assertDoesNotThrow(() -> validacaoUseCase.validarCliente(pedido));
    }

    @Test
    void deveLancarExcecao_QuandoClienteNaoExiste() {
        PedidoProcessor pedido = new PedidoProcessor();
        pedido.setClienteId(123L);
        pedido.setPedidoReciverId(1L);

        when(clienteService.clienteExiste(123L)).thenReturn(false);

        assertThrows(ClienteNaoEncontradoException.class, () -> validacaoUseCase.validarCliente(pedido));
    }

    @Test
    void deveValidarProdutoComEstoqueSuficiente() {
        PedidoProcessor pedido = new PedidoProcessor();
        pedido.setPedidoReciverId(1L);
        ItemPedidoProcessor item = new ItemPedidoProcessor("SKU123", 2, BigDecimal.ZERO, BigDecimal.ZERO);
        pedido.setItens(List.of(item));

        when(produtoService.skuExiste("SKU123")).thenReturn(true);
        when(estoqueService.consultaProduto("SKU123")).thenReturn(new EstoqueResponseDTO("SKU123", 10));

        assertDoesNotThrow(() -> validacaoUseCase.validarProdutos(pedido));
    }

    @Test
    void deveLancarExcecao_QuandoProdutoNaoExiste() {
        PedidoProcessor pedido = new PedidoProcessor();
        pedido.setPedidoReciverId(1L);
        ItemPedidoProcessor item = new ItemPedidoProcessor("SKU123", 2, BigDecimal.ZERO, BigDecimal.ZERO);
        pedido.setItens(List.of(item));

        when(produtoService.skuExiste("SKU123")).thenReturn(false);

        assertThrows(ProdutoNaoEncontradoException.class, () -> validacaoUseCase.validarProdutos(pedido));
    }

    @Test
    void deveLancarExcecao_QuandoEstoqueInsuficiente() {
        PedidoProcessor pedido = new PedidoProcessor();
        pedido.setPedidoReciverId(1L);
        ItemPedidoProcessor item = new ItemPedidoProcessor("SKU123", 5, BigDecimal.ZERO, BigDecimal.ZERO);
        pedido.setItens(List.of(item));

        when(produtoService.skuExiste("SKU123")).thenReturn(true);
        when(estoqueService.consultaProduto("SKU123")).thenReturn(new EstoqueResponseDTO("SKU123", 3));

        assertThrows(EstoqueInsuficienteException.class, () -> validacaoUseCase.validarProdutos(pedido));
    }
}
