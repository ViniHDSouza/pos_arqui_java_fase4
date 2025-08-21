package br.com.fiap.mspedidoreciver.adapter.persistence.repository;

import br.com.fiap.mspedidoreciver.adapter.persistence.entity.PedidoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PedidoReciverJpaRepository extends JpaRepository<PedidoEntity, Long> {
}
