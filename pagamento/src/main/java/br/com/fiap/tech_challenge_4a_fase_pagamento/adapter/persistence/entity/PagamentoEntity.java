package br.com.fiap.tech_challenge_4a_fase_pagamento.adapter.persistence.entity;

import br.com.fiap.tech_challenge_4a_fase_pagamento.core.domain.Pagamento;
import br.com.fiap.tech_challenge_4a_fase_pagamento.core.domain.StatusPagamento;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Getter
@Table(name = "pagamentos")
public class PagamentoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "pedido_id", nullable = false)
    private Long pedidoId;

    @Column(name = "cliente_id", nullable = false)
    private Long clienteId;

    @Column(name = "numero_cartao", nullable = false, length = 50)
    private String numeroCartao;

    @Column(name = "valor", nullable = false, precision = 12, scale = 2)
    private BigDecimal valor;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    private StatusPagamento status;

    @Column(name = "transacao_externa_id", length = 100)
    private String transacaoExternaId;

    @Column(name = "motivo_rejeicao", length = 500)
    private String motivoRejeicao;

    @Column(name = "data_criacao", nullable = false)
    private LocalDateTime dataCriacao;

    @Column(name = "data_atualizacao", nullable = false)
    private LocalDateTime dataAtualizacao;


    public PagamentoEntity(Pagamento pagamento) {
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

    public Pagamento toDomain() {
        return new Pagamento(
                this.id,
                this.pedidoId,
                this.clienteId,
                this.numeroCartao,
                this.valor,
                this.status,
                this.transacaoExternaId,
                this.motivoRejeicao,
                this.dataCriacao,
                this.dataAtualizacao
        );
    }

}


