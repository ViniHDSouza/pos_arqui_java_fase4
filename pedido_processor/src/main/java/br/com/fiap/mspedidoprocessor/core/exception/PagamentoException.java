package br.com.fiap.mspedidoprocessor.core.exception;

public class PagamentoException extends PedidoProcessorException {
    public PagamentoException(String motivo) {
        super("motivo");
    }
    public PagamentoException(Exception exception) {
        super("Pagamento recusado.  Exception : " + exception.getMessage());
    }
}