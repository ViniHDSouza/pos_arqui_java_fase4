package br.com.fiap.tech_challenge_4a_fase_pagamento.adapter.persistence.repository;

import br.com.fiap.tech_challenge_4a_fase_pagamento.adapter.persistence.entity.PagamentoEntity;
import br.com.fiap.tech_challenge_4a_fase_pagamento.core.domain.StatusPagamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface PagamentoJpaRepository extends JpaRepository<PagamentoEntity, Long> {

    List<PagamentoEntity> findByPedidoId(Long pedidoId);
    List<PagamentoEntity> findByClienteId(Long clienteId);
    List<PagamentoEntity> findByStatus(StatusPagamento status);
    Optional<PagamentoEntity> findByTransacaoExternaId(String transacaoExternaId);
}
