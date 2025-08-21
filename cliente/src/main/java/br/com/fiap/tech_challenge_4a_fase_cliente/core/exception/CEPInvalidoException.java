package br.com.fiap.tech_challenge_4a_fase_cliente.core.exception;

public class CEPInvalidoException extends RuntimeException {
    public CEPInvalidoException() {
        super("CEP no padrão incorreto!");
    }
}