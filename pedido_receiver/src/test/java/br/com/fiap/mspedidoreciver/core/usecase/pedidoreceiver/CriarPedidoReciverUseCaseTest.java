package br.com.fiap.mspedidoreciver.core.usecase.pedidoreceiver;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import br.com.fiap.mspedidoreciver.adapter.mapper.PedidoMapper;
import br.com.fiap.mspedidoreciver.core.domain.ItemPedido;
import br.com.fiap.mspedidoreciver.core.domain.Pedido;
import br.com.fiap.mspedidoreciver.core.gateways.PedidoReciverGateway;
import br.com.fiap.mspedidoreciver.core.gateways.ProdutorPedidoMensageriaGateway;
import br.com.fiap.mspedidoreciver.core.usecase.pedidoreciver.CriarPedidoReciverUseCase;

class CriarPedidoReciverUseCaseTest {

    private PedidoReciverGateway pedidoReciverGateway;
    private ProdutorPedidoMensageriaGateway produtorPedidoMensageriaGateway;
    private PedidoMapper pedidoMapper; // Não utilizado nesse caso, mas incluído para manter a construção
    private CriarPedidoReciverUseCase useCase;

    @BeforeEach
    void setUp() {
        pedidoReciverGateway = mock(PedidoReciverGateway.class);
        produtorPedidoMensageriaGateway = mock(ProdutorPedidoMensageriaGateway.class);
        pedidoMapper = mock(PedidoMapper.class);

        useCase = new CriarPedidoReciverUseCase(
                pedidoReciverGateway,
                pedidoMapper,
                produtorPedidoMensageriaGateway
        );
    }

    @Test
    void deveCriarPedidoComStatusAbertoEDataCriacaoAtual() {
        // given
        Pedido pedido = Pedido.builder()
                .id(100L)
                .clienteId(1L)
                .numeroCartao("1234")
                .itens(List.of(ItemPedido.builder().sku("SKU1").quantidade(1).build()))
                .build();

        Pedido pedidoSalvo = Pedido.builder()
                .id(pedido.getId())
                .clienteId(pedido.getClienteId())
                .numeroCartao(pedido.getNumeroCartao())
                .itens(pedido.getItens())
                .status("ABERTO")
                .dataCriacao(LocalDateTime.now())
                .build();

        when(pedidoReciverGateway.processarPedidoReciver(any(Pedido.class))).thenReturn(pedidoSalvo);

        // when
        Pedido resultado = useCase.execute(pedido);

        // then
        assertNotNull(resultado);
        assertEquals("ABERTO", resultado.getStatus());
        assertNotNull(resultado.getDataCriacao());
        verify(pedidoReciverGateway).processarPedidoReciver(any(Pedido.class));
        verify(produtorPedidoMensageriaGateway).publicarPedido(resultado);
    }

    @Test
    void devePublicarPedidoAposSalvar() {
        Pedido pedido = Pedido.builder().clienteId(5L).numeroCartao("9999").build();
        Pedido pedidoSalvo = Pedido.builder()
                .clienteId(5L)
                .numeroCartao("9999")
                .status("ABERTO")
                .dataCriacao(LocalDateTime.now())
                .build();

        when(pedidoReciverGateway.processarPedidoReciver(any(Pedido.class))).thenReturn(pedidoSalvo);

        useCase.execute(pedido);

        verify(produtorPedidoMensageriaGateway, times(1)).publicarPedido(pedidoSalvo);
    }
}
