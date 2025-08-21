package br.com.fiap.tech_challenge_4a_fase_pagamento.core.exception;

public class EstadoPagamentoInvalidoException extends RuntimeException {
    public EstadoPagamentoInvalidoException(String message) {
        super(message);
    }
}
