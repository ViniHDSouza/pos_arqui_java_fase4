package br.com.fiap.tech_challenge_4a_fase_pagamento.core.domain;


import lombok.Getter;

@Getter
public enum StatusPagamento {
    PENDENTE("Pagamento pendente de processamento"),
    PROCESSANDO("Pagamento sendo processado"),
    APROVADO("Pagamento aprovado com sucesso"),
    REJEITADO("Pagamento rejeitado por falta de fundos"),
    ERRO("Erro no processamento do pagamento"),
    ESTORNADO("Pagamento estornado");

    private final String descricao;

    StatusPagamento(String descricao) {
        this.descricao = descricao;
    }

}