package br.com.fiap.tech_challenge_4a_fase_cliente.core.exception;

public class CPFInvalidoException extends RuntimeException {
    public CPFInvalidoException() {
        super("CPF inválido");
    }
}