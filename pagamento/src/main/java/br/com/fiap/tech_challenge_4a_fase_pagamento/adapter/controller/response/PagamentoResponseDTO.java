package br.com.fiap.tech_challenge_4a_fase_pagamento.adapter.controller.response;

import br.com.fiap.tech_challenge_4a_fase_pagamento.core.domain.Pagamento;
import br.com.fiap.tech_challenge_4a_fase_pagamento.core.domain.StatusPagamento;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Setter
@Getter
@ToString
@NoArgsConstructor
public class PagamentoResponseDTO {

    private Long id;
    private Long pedidoId;
    private Long clienteId;
    private String numeroCartao;
    private BigDecimal valor;
    private StatusPagamento status;
    private String transacaoExternaId;
    private String motivoRejeicao;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;

    public PagamentoResponseDTO(Pagamento pagamento) {
        this.id = pagamento.getId();
        this.pedidoId = pagamento.getPedidoId();
        this.clienteId = pagamento.getClienteId();
        this.numeroCartao = pagamento.getNumeroCartao();
        this.valor = pagamento.getValor();
        this.status = pagamento.getStatus();
        this.transacaoExternaId = pagamento.getTransacaoExternaId();
        this.motivoRejeicao = pagamento.getMotivoRejeicao();
        this.dataCriacao = pagamento.getDataCriacao();
        this.dataAtualizacao = pagamento.getDataAtualizacao();
    }

}
