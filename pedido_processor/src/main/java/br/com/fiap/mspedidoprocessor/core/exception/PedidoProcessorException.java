package br.com.fiap.mspedidoprocessor.core.exception;

public class PedidoProcessorException extends RuntimeException {
    public PedidoProcessorException(String message) {
        super(message);
    }

    public PedidoProcessorException(String message, Throwable cause) {
        super(message, cause);
    }
}