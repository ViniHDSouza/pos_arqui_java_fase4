package br.com.fiap.mspedidoprocessor.adapter.external.pagamentoservice;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import br.com.fiap.mspedidoprocessor.adapter.external.pagamentoservice.dto.PagamentoRequestDTO;
import br.com.fiap.mspedidoprocessor.adapter.external.pagamentoservice.dto.PagamentoResponseDTO;
import scala.math.BigDecimal;

class PagamentoServiceClientTest {

    @Mock
    private PagamentoServiceClient pagamentoServiceClient;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveRetornarRespostaPagamentoComSucesso() {
        // Arrange
        PagamentoRequestDTO request = new PagamentoRequestDTO(null, null, null);
        PagamentoResponseDTO expectedResponse = new PagamentoResponseDTO(1L, null, null, "23143532856", "Aprovado", null, "ABCD4321");

        when(pagamentoServiceClient.solicitarPagamento(request)).thenReturn(expectedResponse);

        // Act
        PagamentoResponseDTO response = pagamentoServiceClient.solicitarPagamento(request);

        // Assert
        assertEquals(1L, response.id());
        assertEquals("Aprovado", response.status());
        assertEquals("23143532856", response.numeroCartao());
        
    }
}
