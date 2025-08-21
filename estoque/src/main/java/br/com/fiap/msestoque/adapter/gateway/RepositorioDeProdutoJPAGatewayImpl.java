package br.com.fiap.msestoque.adapter.gateway;

import br.com.fiap.msestoque.adapter.mapper.EstoqueMapper;
import br.com.fiap.msestoque.adapter.persistence.entity.EstoqueEntity;
import br.com.fiap.msestoque.adapter.persistence.repository.EstoqueRepository;
import br.com.fiap.msestoque.core.domain.Estoque;
import br.com.fiap.msestoque.core.gateways.EstoqueGateway;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class RepositorioDeProdutoJPAGatewayImpl implements EstoqueGateway {

    private final EstoqueRepository estoqueRepository;
    private final EstoqueMapper estoqueMapper;

    public RepositorioDeProdutoJPAGatewayImpl(EstoqueRepository estoqueRepository, EstoqueMapper estoqueMapper) {
        this.estoqueRepository = estoqueRepository;
        this.estoqueMapper = estoqueMapper;
    }

    @Override
    public Optional<Estoque> findBySku(String sku) {
        EstoqueEntity estoqueEntity = estoqueRepository.findBySku(sku);
        return Optional.ofNullable(estoqueEntity)
                .map(estoqueMapper::toEstoque);
    }

    @Override
    public Estoque save(Estoque estoque) {
        EstoqueEntity estoqueEntity = estoqueMapper.toEstoqueEntity(estoque);
        EstoqueEntity estoqueSalvo = estoqueRepository.save(estoqueEntity);
        return estoqueMapper.toEstoque(estoqueSalvo);
    }

}
