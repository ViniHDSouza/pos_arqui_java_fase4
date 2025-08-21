package br.com.fiap.tech_challenge_4a_fase_pagamento.core.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class ResultadoConsultaPagamento {

    private final String status;
    private final String motivo;
    private final boolean aprovado;

}
