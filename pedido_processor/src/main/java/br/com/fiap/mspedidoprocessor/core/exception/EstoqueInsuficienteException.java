package br.com.fiap.mspedidoprocessor.core.exception;

public class EstoqueInsuficienteException extends PedidoProcessorException {
    public EstoqueInsuficienteException(String sku, int quantidadeSolicitada, int quantidadeDisponivel) {
        super(String.format("Estoque insuficiente para o produto %s. Solicitado: %d, Disponível: %d",
                sku, quantidadeSolicitada, quantidadeDisponivel));
    }

    public EstoqueInsuficienteException(String sku) {
        super(String.format("Estoque insuficiente para o produto %s.", sku));
    }
}