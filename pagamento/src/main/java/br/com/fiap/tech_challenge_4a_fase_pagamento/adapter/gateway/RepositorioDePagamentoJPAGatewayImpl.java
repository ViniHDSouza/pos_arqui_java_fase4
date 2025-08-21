package br.com.fiap.tech_challenge_4a_fase_pagamento.adapter.gateway;

import br.com.fiap.tech_challenge_4a_fase_pagamento.adapter.persistence.entity.PagamentoEntity;
import br.com.fiap.tech_challenge_4a_fase_pagamento.adapter.persistence.repository.PagamentoJpaRepository;
import br.com.fiap.tech_challenge_4a_fase_pagamento.core.domain.Pagamento;
import br.com.fiap.tech_challenge_4a_fase_pagamento.core.domain.StatusPagamento;
import br.com.fiap.tech_challenge_4a_fase_pagamento.core.gateways.PagamentoGateway;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Component
public class RepositorioDePagamentoJPAGatewayImpl implements PagamentoGateway {

    private final PagamentoJpaRepository jpaRepository;

    @Override
    public Pagamento salvar(Pagamento pagamento) {
        PagamentoEntity entity = new PagamentoEntity(pagamento);
        PagamentoEntity savedEntity = jpaRepository.save(entity);
        return savedEntity.toDomain();
    }

    @Override
    public Optional<Pagamento> buscarPorId(Long id) {
        return jpaRepository.findById(id)
                .map(PagamentoEntity::toDomain);
    }

    @Override
    public List<Pagamento> buscarPorPedidoId(Long pedidoId) {
        return jpaRepository.findByPedidoId(pedidoId)
                .stream()
                .map(PagamentoEntity::toDomain)
                .toList();
    }

    @Override
    public List<Pagamento> buscarPorClienteId(Long clienteId) {
        return jpaRepository.findByClienteId(clienteId)
                .stream()
                .map(PagamentoEntity::toDomain)
                .toList();
    }

    @Override
    public List<Pagamento> buscarPorStatus(StatusPagamento status) {
        return jpaRepository.findByStatus(status)
                .stream()
                .map(PagamentoEntity::toDomain)
                .toList();
    }

    @Override
    public Optional<Pagamento> buscarPorTransacaoExternaId(String transacaoExternaId) {
        return jpaRepository.findByTransacaoExternaId(transacaoExternaId)
                .map(PagamentoEntity::toDomain);
    }

    @Override
    public List<Pagamento> listarTodos() {
        return jpaRepository.findAll()
                .stream()
                .map(PagamentoEntity::toDomain)
                .toList();
    }
}
