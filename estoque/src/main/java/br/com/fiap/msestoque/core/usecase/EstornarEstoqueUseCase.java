package br.com.fiap.msestoque.core.usecase;

import br.com.fiap.msestoque.core.exception.EstoqueException;
import br.com.fiap.msestoque.core.gateways.EstoqueGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class EstornarEstoqueUseCase {

    private final EstoqueGateway estoqueGateway;

    public void executar(String sku, Integer quantidade) {
        if (quantidade == null) {
            throw new EstoqueException("Quantidade não pode ser nulo.");
        }
        if (sku == null || sku.isEmpty()) {
            throw new EstoqueException("SKU não pode ser nulo ou vazio.");
        }
        var estoque = estoqueGateway.findBySku(sku)
                .orElseThrow(() -> new EstoqueException("Estoque não encontrado para o SKU: " + sku));

        int novaQuantidade = estoque.getQuantidade() + quantidade;

        estoque.setQuantidade(novaQuantidade);
        estoque.setAtualizadoEm(LocalDateTime.now());
        estoqueGateway.save(estoque);
    }

}
