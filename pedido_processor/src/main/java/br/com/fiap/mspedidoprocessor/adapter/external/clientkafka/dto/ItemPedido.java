package br.com.fiap.mspedidoprocessor.adapter.external.clientkafka.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ItemPedido {
    private Long id;
    private String sku;
    private Integer quantidade;
}
