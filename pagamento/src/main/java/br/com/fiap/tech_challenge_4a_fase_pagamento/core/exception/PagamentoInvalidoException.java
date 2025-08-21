package br.com.fiap.tech_challenge_4a_fase_pagamento.core.exception;

public class PagamentoInvalidoException extends RuntimeException {
    public PagamentoInvalidoException(String message) {
        super(message);
    }
}
