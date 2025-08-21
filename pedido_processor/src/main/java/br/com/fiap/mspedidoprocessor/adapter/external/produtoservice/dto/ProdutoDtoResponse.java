package br.com.fiap.mspedidoprocessor.adapter.external.produtoservice.dto;

import java.math.BigDecimal;


public record ProdutoDtoResponse(
    Long id,
    String nome,
    String sku,
    BigDecimal preco
) {
}
