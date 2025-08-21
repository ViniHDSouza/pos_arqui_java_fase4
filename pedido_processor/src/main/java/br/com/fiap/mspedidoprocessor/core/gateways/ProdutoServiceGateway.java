package br.com.fiap.mspedidoprocessor.core.gateways;

import br.com.fiap.mspedidoprocessor.adapter.external.produtoservice.dto.ProdutoDtoResponse;
import org.springframework.web.bind.annotation.PathVariable;

import java.math.BigDecimal;

public interface ProdutoServiceGateway {
    boolean skuExiste(String sku);
    BigDecimal obterPreco(String sku);
    ProdutoDtoResponse buscarProduto(@PathVariable("sku") String sku);
}