package br.com.fiap.mspedidoprocessor.core.domain;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PedidoProcessor {
    private UUID id;
    private Long pedidoReciverId;
    private Long clienteId;
    private List<ItemPedidoProcessor> itens;
    private BigDecimal total;
    private PedidoStatus status;
    private String numeroCartao;
}
