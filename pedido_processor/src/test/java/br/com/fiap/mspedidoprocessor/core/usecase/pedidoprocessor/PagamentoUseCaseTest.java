package br.com.fiap.mspedidoprocessor.core.usecase.pedidoprocessor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import br.com.fiap.mspedidoprocessor.adapter.external.pagamentoservice.dto.PagamentoRequestDTO;
import br.com.fiap.mspedidoprocessor.adapter.external.pagamentoservice.dto.PagamentoResponseDTO;
import br.com.fiap.mspedidoprocessor.core.domain.PagamentoDTO;
import br.com.fiap.mspedidoprocessor.core.domain.PedidoProcessor;
import br.com.fiap.mspedidoprocessor.core.domain.PedidoStatus;
import br.com.fiap.mspedidoprocessor.core.exception.PagamentoException;
import br.com.fiap.mspedidoprocessor.core.gateways.PagamentoServiceGateway;

class PagamentoUseCaseTest {

    private PagamentoServiceGateway pagamentoGateway;
    private PagamentoUseCase pagamentoUseCase;

    @BeforeEach
    void setup() {
        pagamentoGateway = mock(PagamentoServiceGateway.class);
        pagamentoUseCase = new PagamentoUseCase(pagamentoGateway);
    }

    private PedidoProcessor criarPedido() {
        PedidoProcessor pedido = new PedidoProcessor();
        pedido.setId(UUID.randomUUID());
        pedido.setNumeroCartao("1234-5678-9876-5432");
        pedido.setTotal(BigDecimal.valueOf(100.0));
        return pedido;
    }

    @Test
    void deveLancarExcecaoQuandoRespostaPagamentoForNula() {
        PedidoProcessor pedido = criarPedido();

        when(pagamentoGateway.solicitarPagamento(any(PagamentoRequestDTO.class)))
                .thenReturn(null);

        PagamentoException exception = assertThrows(PagamentoException.class,
                () -> pagamentoUseCase.processarPagamento(pedido));

        assertEquals("motivo", exception.getMessage());
        verify(pagamentoGateway, times(1)).solicitarPagamento(any());
    }

    @Test
    void deveLancarExcecaoQuandoGatewayLancarErro() {
        PedidoProcessor pedido = criarPedido();

        when(pagamentoGateway.solicitarPagamento(any(PagamentoRequestDTO.class)))
                .thenThrow(new RuntimeException("Timeout"));

        Exception ex = assertThrows(RuntimeException.class,
                () -> pagamentoUseCase.processarPagamento(pedido));

        assertEquals("Timeout", ex.getMessage());
        verify(pagamentoGateway, times(1)).solicitarPagamento(any());
    }
    
    @Test
    void deveRetornarPagamentoDTOQuandoPagamentoForBemSucedido() {
        PedidoProcessor pedido = criarPedido();

        PagamentoResponseDTO response = new PagamentoResponseDTO(
        	    1L,
        	    pedido.getId(),
        	    pedido.getTotal(),
        	    pedido.getNumeroCartao(),
        	    "APROVADO",
        	    LocalDateTime.now(),
        	    "TRANS123456789"
        	);

        when(pagamentoGateway.solicitarPagamento(any(PagamentoRequestDTO.class)))
                .thenReturn(response);

        PagamentoDTO resultado = pagamentoUseCase.processarPagamento(pedido);

        assertEquals(pedido.getId().toString(), resultado.pedidoId());
        assertEquals(pedido.getTotal(), resultado.valor());
        assertEquals(pedido.getNumeroCartao(), resultado.numeroCartao());
        assertEquals(PedidoStatus.APROVADO, resultado.status());

        verify(pagamentoGateway, times(1)).solicitarPagamento(any());
    }

}
