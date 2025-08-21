package br.com.fiap.mspedidoprocessor.core.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class PagamentoCallback {
    private Long pedidoId;
    private Long clienteId;
    private String status;
}
