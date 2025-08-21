package br.com.fiap.mspedidoprocessor.core.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import br.com.fiap.mspedidoprocessor.core.exception.ClienteException;
import br.com.fiap.mspedidoprocessor.core.exception.PedidoProcessorException;

class ClienteExceptionTest {

    @Test
    void deveCriarClienteExceptionComMensagem() {
        String mensagemErro = "Cliente não encontrado";
        ClienteException exception = new ClienteException(mensagemErro);

        assertNotNull(exception);
        assertEquals(mensagemErro, exception.getMessage());
    }

    @Test
    void deveSerInstanciaDePedidoProcessorException() {
        ClienteException exception = new ClienteException("Erro genérico");

        assertTrue(exception instanceof PedidoProcessorException);
    }
}
