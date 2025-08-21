package br.com.fiap.mspedidoprocessor.adapter.external.pagamentoservice.dto;

import java.math.BigDecimal;

public record PagamentoRequestDTO(
        //UUID pedidoId,
        String pedidoId,
        BigDecimal valor,
        String numeroCartao) {
}
