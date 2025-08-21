package br.com.fiap.mspedidoprocessor.adapter.external.clientkafka.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
@AllArgsConstructor
public class PagamentoDTO {
    public Long pedidoId;
    public BigDecimal valor;
    public String numeroCartao;

}
