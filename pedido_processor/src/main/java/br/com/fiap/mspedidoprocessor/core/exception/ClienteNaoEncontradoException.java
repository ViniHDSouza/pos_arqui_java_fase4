package br.com.fiap.mspedidoprocessor.core.exception;

public class ClienteNaoEncontradoException extends PedidoProcessorException {
    public ClienteNaoEncontradoException(Long clienteId) {
        super("Cliente não encontrado com ID: " + clienteId);
    }
}
