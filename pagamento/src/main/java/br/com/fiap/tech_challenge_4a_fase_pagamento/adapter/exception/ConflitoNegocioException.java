package br.com.fiap.tech_challenge_4a_fase_pagamento.adapter.exception;

public class ConflitoNegocioException extends RuntimeException {
    public ConflitoNegocioException(String message) {
        super(message);
    }
}
