package br.com.fiap.tech_challenge_4a_fase_pagamento.adapter.external.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class TransacaoMock {
    String id;
    String status;
    String motivo;
    boolean aprovado;
    boolean estornado;
    BigDecimal valor;

    public TransacaoMock(String transacaoId, String status, String motivo, boolean aprovado, BigDecimal valor) {
        this.id = transacaoId;
        this.status = status;
        this.motivo = motivo;
        this.aprovado = aprovado;
        this.estornado = false;
        this.valor = valor;
    }

    public TransacaoMock() {
        // Construtor padrão necessário para serialização/deserialização
    }
}