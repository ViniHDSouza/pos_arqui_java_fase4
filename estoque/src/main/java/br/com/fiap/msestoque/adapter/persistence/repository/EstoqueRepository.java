package br.com.fiap.msestoque.adapter.persistence.repository;

import br.com.fiap.msestoque.adapter.persistence.entity.EstoqueEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EstoqueRepository extends JpaRepository<EstoqueEntity, Long> {
    EstoqueEntity findBySku(String sku);
}
