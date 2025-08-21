package br.com.fiap.mspedidoprocessor.core.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import org.junit.jupiter.api.Test;

class EstoqueExceptionTest {

    @Test
    void deveCriarEstoqueExceptionComMotivo() {
        EstoqueException exception = new EstoqueException("produto sem saldo");
        assertNotNull(exception);
        assertEquals("Pagamento recusado: produto sem saldo", exception.getMessage());
    }

    @Test
    void deveCriarEstoqueExceptionComExceptionInterna() {
        Exception causa = new RuntimeException("erro no serviço");
        EstoqueException exception = new EstoqueException(causa);
        assertNotNull(exception);
        assertEquals("Pagamento recusado. Exception: erro no serviço", exception.getMessage());
    }

    @Test
    void deveCriarEstoqueExceptionComMotivoEException() {
        Exception causa = new RuntimeException("falha de rede");
        EstoqueException exception = new EstoqueException("problema de conexão", causa);
        assertNotNull(exception);
        assertEquals("Pagamento recusado: problema de conexão", exception.getMessage());
        assertSame(causa, exception.getCause());
    }
}
