package br.com.fiap.tech_challenge_4a_fase_pagamento.adapter.mapper;

import br.com.fiap.tech_challenge_4a_fase_pagamento.adapter.persistence.entity.PagamentoEntity;
import br.com.fiap.tech_challenge_4a_fase_pagamento.core.domain.Pagamento;
import org.springframework.stereotype.Component;

@Component
public class PagamentoMapper {

    public PagamentoEntity toEntity(Pagamento domain) {
        if (domain == null) return null;

        return PagamentoEntity.builder()
                .id(domain.getId())
                .pedidoId(domain.getPedidoId())
                .clienteId(domain.getClienteId())
                .numeroCartao(domain.getNumeroCartao())
                .valor(domain.getValor())
                .status(domain.getStatus())
                .transacaoExternaId(domain.getTransacaoExternaId())
                .dataCriacao(domain.getDataCriacao())
                .dataAtualizacao(domain.getDataAtualizacao())
                .build();
    }

    public Pagamento toDomain(PagamentoEntity entity) {
        if (entity == null) return null;

        return Pagamento.builder()
                .id(entity.getId())
                .pedidoId(entity.getPedidoId())
                .clienteId(entity.getClienteId())
                .numeroCartao(entity.getNumeroCartao())
                .valor(entity.getValor())
                .status(entity.getStatus())
                .transacaoExternaId(entity.getTransacaoExternaId())
                .motivoRejeicao(entity.getMotivoRejeicao())
                .dataCriacao(entity.getDataCriacao())
                .dataAtualizacao(entity.getDataAtualizacao())
                .build();
    }
}
