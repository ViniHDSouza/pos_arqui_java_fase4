package br.com.fiap.msestoque.core.gateways;

import br.com.fiap.msestoque.core.domain.Estoque;

import java.util.Optional;

public interface EstoqueGateway {
    Optional<Estoque> findBySku(String sku);
    Estoque save(Estoque estoque);
}
