package br.com.fiap.mspedidoprocessor.core.usecase.pedidoprocessor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import br.com.fiap.mspedidoprocessor.core.domain.PagamentoCallback;
import br.com.fiap.mspedidoprocessor.core.gateways.ConsultaPedidoProcessorServiceGateway;
import br.com.fiap.mspedidoprocessor.core.usecase.pedidoprocessor.ConsultaPedidoProcessorUseCase;

class ConsultaPedidoProcessorUseCaseTest {

    private ConsultaPedidoProcessorServiceGateway consultaGateway;
    private ConsultaPedidoProcessorUseCase useCase;

    @BeforeEach
    void setUp() {
        consultaGateway = mock(ConsultaPedidoProcessorServiceGateway.class);
        useCase = new ConsultaPedidoProcessorUseCase(consultaGateway);
    }

    @Test
    void consultarPagamentoCallback_deveRetornarCallbackCorretamente() {
        // Arrange
        Long pedidoId = 123L;
        PagamentoCallback esperado = new PagamentoCallback(pedidoId, 456L, "APROVADO");

        when(consultaGateway.consultarPagamentoCallback(pedidoId)).thenReturn(esperado);

        // Act
        PagamentoCallback resultado = useCase.consultarPagamentoCallback(pedidoId);

        // Assert
        assertNotNull(resultado);
        assertEquals(pedidoId, resultado.getPedidoId());
        assertEquals(456L, resultado.getClienteId());
        assertEquals("APROVADO", resultado.getStatus());

        verify(consultaGateway, times(1)).consultarPagamentoCallback(pedidoId);
    }
}
