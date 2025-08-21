package br.com.fiap.mspedidoprocessor.core.exception;

public class ProdutoNaoEncontradoException extends PedidoProcessorException {
    public ProdutoNaoEncontradoException(String sku) {
        super("Produto não encontrado com SKU: " + sku);
    }
}