package br.com.fiap.mspedidoprocessor.core.usecase.pedidoprocessor;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import br.com.fiap.mspedidoprocessor.adapter.external.estoqueservice.dto.BaixaEstoqueRequestDTO;
import br.com.fiap.mspedidoprocessor.core.domain.ItemPedidoProcessor;
import br.com.fiap.mspedidoprocessor.core.domain.PedidoProcessor;
import br.com.fiap.mspedidoprocessor.core.exception.EstoqueInsuficienteException;
import br.com.fiap.mspedidoprocessor.core.gateways.EstoqueServiceGateway;
import br.com.fiap.mspedidoprocessor.core.usecase.pedidoprocessor.EstoqueUseCase;

class EstoqueUseCaseTest {

    private EstoqueServiceGateway estoqueGateway;
    private EstoqueUseCase estoqueUseCase;

    @BeforeEach
    void setup() {
        estoqueGateway = mock(EstoqueServiceGateway.class);
        estoqueUseCase = new EstoqueUseCase(estoqueGateway);
    }

    private PedidoProcessor gerarPedidoProcessor() {
        ItemPedidoProcessor item = new ItemPedidoProcessor("SKU123", 2, BigDecimal.TEN, BigDecimal.valueOf(20));
        PedidoProcessor pedido = new PedidoProcessor();
        pedido.setId(java.util.UUID.randomUUID());
        pedido.setItens(List.of(item));
        return pedido;
    }

    @Test
    void deveBaixarEstoqueComSucesso() {
        PedidoProcessor pedido = gerarPedidoProcessor();

        assertDoesNotThrow(() -> estoqueUseCase.baixarEstoque(pedido));

        verify(estoqueGateway, times(1))
                .baixaEstoque(new BaixaEstoqueRequestDTO("SKU123", 2));
    }

    @Test
    void deveReverterEstoqueSeBaixaFalhar() {
        PedidoProcessor pedido = gerarPedidoProcessor();

        doThrow(new RuntimeException("Erro ao baixar estoque"))
                .when(estoqueGateway)
                .baixaEstoque(any(BaixaEstoqueRequestDTO.class));

        assertThrows(EstoqueInsuficienteException.class, () -> estoqueUseCase.baixarEstoque(pedido));

        verify(estoqueGateway, times(1)).baixaEstoque(any());
        verify(estoqueGateway, atLeastOnce()).reverterEstoque(any());
    }

    @Test
    void deveReverterEstoqueComSucesso() {
        PedidoProcessor pedido = gerarPedidoProcessor();

        assertDoesNotThrow(() -> estoqueUseCase.reverterEstoque(pedido));

        verify(estoqueGateway, times(1))
                .reverterEstoque(new BaixaEstoqueRequestDTO("SKU123", 2));
    }
}
