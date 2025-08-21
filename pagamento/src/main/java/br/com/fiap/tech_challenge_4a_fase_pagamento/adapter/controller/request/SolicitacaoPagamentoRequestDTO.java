package br.com.fiap.tech_challenge_4a_fase_pagamento.adapter.controller.request;

import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class SolicitacaoPagamentoRequestDTO {

    @NotNull(message = "ID do pedido é obrigatório")
    @Positive(message = "ID do pedido deve ser positivo")
    private Long pedidoId;

    @NotNull(message = "ID do cliente é obrigatório")
    @Positive(message = "ID do cliente deve ser positivo")
    private Long clienteId;

    @NotBlank(message = "Número do cartão é obrigatório")
    @Pattern(regexp = "\\d{13,19}", message = "Número do cartão deve conter entre 13 e 19 dígitos")
    private String numeroCartao;

    @NotNull(message = "Valor é obrigatório")
    @DecimalMin(value = "0.01", message = "Valor deve ser maior que zero")
    @Digits(integer = 10, fraction = 2, message = "Valor deve ter no máximo 2 casas decimais")
    private BigDecimal valor;



}

