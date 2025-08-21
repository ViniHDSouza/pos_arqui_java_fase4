package br.com.fiap.mspedidoprocessor.adapter.external.pagamentoservice.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record PagamentoResponseDTO(Long id,
                                   UUID pedidoId,
                                   BigDecimal valor,
                                   String numeroCartao,
                                   String status,
                                   LocalDateTime dataCriacao,
                                   String idTransacao) {
}
