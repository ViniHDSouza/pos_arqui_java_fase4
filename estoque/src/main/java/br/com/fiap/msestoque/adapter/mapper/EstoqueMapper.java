package br.com.fiap.msestoque.adapter.mapper;

import br.com.fiap.msestoque.adapter.persistence.entity.EstoqueEntity;
import br.com.fiap.msestoque.core.domain.Estoque;
import org.springframework.stereotype.Component;

@Component
public class EstoqueMapper {

    public Estoque toEstoque(EstoqueEntity entity) {
        if (entity == null) {
            return null;
        }

        return new Estoque(
            entity.getId(),
            entity.getSku(),
            entity.getQuantidade(),
            entity.getAtualizadoEm()
        );
    }

    public EstoqueEntity toEstoqueEntity(Estoque domain) {
        if (domain == null) {
            return null;
        }

        return EstoqueEntity.builder()
            .id(domain.getId())
            .sku(domain.getSku())
            .quantidade(domain.getQuantidade())
            .atualizadoEm(domain.getAtualizadoEm())
            .build();
    }
}