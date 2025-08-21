package br.com.fiap.mspedidoprocessor.core.domain;

import java.math.BigDecimal;

public record PagamentoDTO(
                    String pedidoId,
                    BigDecimal valor,
                    String numeroCartao,
                    PedidoStatus status) {
}
