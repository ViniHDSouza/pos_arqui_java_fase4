package br.com.fiap.tech_challenge_4a_fase_pagamento.adapter.exception;

public class TransacaoNaoEncontradaException extends RuntimeException {
    public TransacaoNaoEncontradaException(String transacaoId) {
        super("Transação não encontrada com o ID: " + transacaoId);
    }
}
