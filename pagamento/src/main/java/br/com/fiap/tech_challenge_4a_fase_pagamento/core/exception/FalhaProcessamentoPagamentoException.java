package br.com.fiap.tech_challenge_4a_fase_pagamento.core.exception;

public class FalhaProcessamentoPagamentoException extends RuntimeException {
    public FalhaProcessamentoPagamentoException(String message) {
        super(message);
    }
    public FalhaProcessamentoPagamentoException(String message, Throwable cause) {
        super(message, cause);
    }
}
