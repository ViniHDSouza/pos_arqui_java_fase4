package br.com.fiap.mspedidoprocessor.core.exception;

public class PedidoNaoEncontradoException extends PedidoProcessorException {
    public PedidoNaoEncontradoException(Long pedidoId) {
        super("Pedido não encontrado com ID: " + pedidoId);
    }
}