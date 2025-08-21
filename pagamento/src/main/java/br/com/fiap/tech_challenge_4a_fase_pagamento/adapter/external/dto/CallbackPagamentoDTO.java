package br.com.fiap.tech_challenge_4a_fase_pagamento.adapter.external.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class CallbackPagamentoDTO {

    @NotBlank(message = "ID da transação externa é obrigatório")
    private String transacaoExternaId;

    @NotBlank(message = "Status é obrigatório")
    private String status;

    private String motivo;

}