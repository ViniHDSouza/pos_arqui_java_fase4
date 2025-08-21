package br.com.fiap.msestoque.core.usecase;

import br.com.fiap.msestoque.core.exception.EstoqueException;
import br.com.fiap.msestoque.core.gateways.EstoqueGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ConsultaEstoqueUseCase {

    private final EstoqueGateway estoqueGateway;

    public int executar(String sku) {

        if (sku == null || sku.isEmpty()) {
            throw new EstoqueException("SKU não pode ser nulo ou vazio.");
        }
        var estoque = estoqueGateway.findBySku(sku)
                .orElseThrow(() -> new EstoqueException("Estoque não encontrado para o SKU: " + sku));

        return estoque.getQuantidade();
    }

}
