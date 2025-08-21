package br.com.fiap.mspedidoprocessor.core.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import br.com.fiap.mspedidoprocessor.core.exception.PedidoNaoEncontradoException;
import br.com.fiap.mspedidoprocessor.core.exception.PedidoProcessorException;

class PedidoNaoEncontradoExceptionTest {

    @Test
    void deveCriarPedidoNaoEncontradoExceptionComMensagemCorreta() {
        Long pedidoId = 123L;
        PedidoNaoEncontradoException exception = new PedidoNaoEncontradoException(pedidoId);

        assertNotNull(exception);
        assertEquals("Pedido não encontrado com ID: 123", exception.getMessage());
    }

    @Test
    void deveSerInstanciaDePedidoProcessorException() {
        PedidoNaoEncontradoException exception = new PedidoNaoEncontradoException(1L);

        assertTrue(exception instanceof PedidoProcessorException);
    }
}
