package br.com.fiap.tech_challenge_4a_fase_pagamento.core.domain;

import br.com.fiap.tech_challenge_4a_fase_pagamento.core.exception.EstadoPagamentoInvalidoException;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@ToString
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
public class Pagamento {

    @EqualsAndHashCode.Include
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

    // Construtor para novos pagamentos
    public Pagamento(Long pedidoId, Long clienteId, String numeroCartao, BigDecimal valor) {
        this.pedidoId = pedidoId;
        this.clienteId = clienteId;
        this.numeroCartao = numeroCartao;
        this.valor = valor;
        this.status = StatusPagamento.PENDENTE;
        this.dataCriacao = LocalDateTime.now();
        this.dataAtualizacao = LocalDateTime.now();
    }

    public void iniciarProcessamento() {
        if (this.status != StatusPagamento.PENDENTE) {
            throw new EstadoPagamentoInvalidoException("Pagamento só pode ser processado se estiver pendente");
        }
        this.status = StatusPagamento.PROCESSANDO;
        this.dataAtualizacao = LocalDateTime.now();
    }

    public void aprovar(String transacaoExternaId) {
        if (this.status != StatusPagamento.PROCESSANDO) {
            throw new EstadoPagamentoInvalidoException("Pagamento só pode ser aprovado se estiver sendo processado");
        }
        this.status = StatusPagamento.APROVADO;
        this.transacaoExternaId = transacaoExternaId;
        this.dataAtualizacao = LocalDateTime.now();
    }

    public void rejeitar(String motivoRejeicao) {
        if (this.status != StatusPagamento.PROCESSANDO) {
            throw new EstadoPagamentoInvalidoException("Pagamento só pode ser rejeitado se estiver sendo processado");
        }
        this.status = StatusPagamento.REJEITADO;
        this.motivoRejeicao = motivoRejeicao;
        this.dataAtualizacao = LocalDateTime.now();
    }

    public void marcarComoErro(String motivoErro) {
        this.status = StatusPagamento.ERRO;
        this.motivoRejeicao = motivoErro;
        this.dataAtualizacao = LocalDateTime.now();
    }

    public void estornar() {
        if (this.status != StatusPagamento.APROVADO) {
            throw new EstadoPagamentoInvalidoException("Só é possível estornar pagamentos aprovados");
        }
        this.status = StatusPagamento.ESTORNADO;
        this.dataAtualizacao = LocalDateTime.now();
    }

    public boolean isAprovado() {
        return this.status == StatusPagamento.APROVADO;
    }

    public boolean isRejeitado() {
        return this.status == StatusPagamento.REJEITADO;
    }

    public boolean isProcessando() {
        return this.status == StatusPagamento.PROCESSANDO;
    }


}
