package br.com.fiap.mspedidoprocessor.adapter.persistence.repository;

import br.com.fiap.mspedidoprocessor.adapter.persistence.entity.PedidoProcessorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PedidoRepositoryJpa extends JpaRepository<PedidoProcessorEntity, String> {
    Optional<PedidoProcessorEntity> findByPedidoReciverId(Long pedidoReciverId);
}