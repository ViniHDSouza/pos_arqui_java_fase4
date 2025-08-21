package br.com.fiap.mspedidoprocessor.core.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

class PagamentoExceptionTest {

    @Test
    void deveCriarPagamentoExceptionComMotivo() {
        PagamentoException exception = new PagamentoException("Cartão inválido");
        assertNotNull(exception);
        assertEquals("motivo", exception.getMessage());
    }

    @Test
    void deveCriarPagamentoExceptionComExceptionInterna() {
        Exception causa = new RuntimeException("Erro de comunicação");
        PagamentoException exception = new PagamentoException(causa);
        assertNotNull(exception);
        assertEquals("Pagamento recusado.  Exception : Erro de comunicação", exception.getMessage());
    }
}
