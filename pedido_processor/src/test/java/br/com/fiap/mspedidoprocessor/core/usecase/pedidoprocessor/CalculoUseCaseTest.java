package br.com.fiap.mspedidoprocessor.core.usecase.pedidoprocessor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import br.com.fiap.mspedidoprocessor.adapter.external.produtoservice.dto.ProdutoDtoResponse;
import br.com.fiap.mspedidoprocessor.core.domain.ItemPedidoProcessor;
import br.com.fiap.mspedidoprocessor.core.domain.PedidoProcessor;
import br.com.fiap.mspedidoprocessor.core.gateways.ProdutoServiceGateway;
import br.com.fiap.mspedidoprocessor.core.usecase.pedidoprocessor.CalculoUseCase;

class CalculoUseCaseTest {

    private ProdutoServiceGateway produtoServiceGateway;
    private CalculoUseCase calculoUseCase;

    @BeforeEach
    void setUp() {
        produtoServiceGateway = mock(ProdutoServiceGateway.class);
        calculoUseCase = new CalculoUseCase(produtoServiceGateway);
    }

    @Test
    void calcularTotal_deveCalcularPrecoTotalCorretamente() {
        // Arrange
        ItemPedidoProcessor item1 = new ItemPedidoProcessor(null, 0, null, null);
        item1.setSku("SKU001");
        item1.setQuantidade(2);

        ItemPedidoProcessor item2 = new ItemPedidoProcessor(null, 0, null, null);
        item2.setSku("SKU002");
        item2.setQuantidade(3);

        PedidoProcessor pedido = new PedidoProcessor();
        pedido.setItens(List.of(item1, item2));

        when(produtoServiceGateway.buscarProduto("SKU001"))
                .thenReturn(new ProdutoDtoResponse(null, "SKU001", "Produto 1", new BigDecimal("10.00")));

        when(produtoServiceGateway.buscarProduto("SKU002"))
                .thenReturn(new ProdutoDtoResponse(null, "SKU002", "Produto 2", new BigDecimal("20.00")));

        // Act
        calculoUseCase.calcularTotal(pedido);

        // Assert
        assertEquals(new BigDecimal("20.00"), item1.getPrecoTotal());
        assertEquals(new BigDecimal("10.00"), item1.getPrecoUnitario());

        assertEquals(new BigDecimal("60.00"), item2.getPrecoTotal());
        assertEquals(new BigDecimal("20.00"), item2.getPrecoUnitario());

        assertEquals(new BigDecimal("80.00"), pedido.getTotal());
    }
}
