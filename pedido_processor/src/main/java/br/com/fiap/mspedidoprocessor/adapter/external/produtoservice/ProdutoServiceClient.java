package br.com.fiap.mspedidoprocessor.adapter.external.produtoservice;

import br.com.fiap.mspedidoprocessor.adapter.external.produtoservice.dto.ProdutoDtoResponse;
import br.com.fiap.mspedidoprocessor.core.gateways.ProdutoServiceGateway;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.math.BigDecimal;

@FeignClient(name = "produto-service", url = "${services.produto}")
public interface ProdutoServiceClient extends ProdutoServiceGateway {

    @GetMapping("/produtos/{sku}")
    default boolean skuExiste(@PathVariable("sku") String sku) {
        try {
            buscarProduto(sku);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @GetMapping("/produtos/sku/{sku}")
    ProdutoDtoResponse buscarProduto(@PathVariable("sku") String sku);

    @GetMapping("/produtos/{sku}/preco")
    BigDecimal obterPreco(@PathVariable("sku") String sku);
}