package br.com.fiap.mspedidoprocessor.core.exception;

public class EstoqueException extends PedidoProcessorException {
    public EstoqueException(String motivo) {
        super("Pagamento recusado: " + motivo);
    }

    public EstoqueException(Exception ex) {
        super("Pagamento recusado. Exception: " + ex.getMessage());
    }

    public EstoqueException(String motivo, Exception ex) {
        super("Pagamento recusado: " + motivo, ex);
    }
}