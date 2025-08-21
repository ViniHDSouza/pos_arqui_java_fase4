package br.com.fiap.mspedidoprocessor.core.usecase.pedidoprocessor;

import br.com.fiap.mspedidoprocessor.adapter.external.produtoservice.dto.ProdutoDtoResponse;
import br.com.fiap.mspedidoprocessor.core.domain.ItemPedidoProcessor;
import br.com.fiap.mspedidoprocessor.core.domain.PedidoProcessor;
import br.com.fiap.mspedidoprocessor.core.gateways.ProdutoServiceGateway;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Slf4j
@Component
@AllArgsConstructor
public class CalculoUseCase {

    private final ProdutoServiceGateway produtoGateway;

    public void calcularTotal(PedidoProcessor pedidoProcessor) {
        log.info("Iniciando cálculo do total para pedido ID: {}", pedidoProcessor.getId());
        BigDecimal total = BigDecimal.ZERO;

        for (ItemPedidoProcessor item : pedidoProcessor.getItens()) {
            ProdutoDtoResponse produtoDtoResponse = produtoGateway.buscarProduto(item.getSku());

            BigDecimal preco = produtoDtoResponse.preco();

            item.setPrecoUnitario(preco);
            BigDecimal precoTotal = preco.multiply(BigDecimal.valueOf(item.getQuantidade()));
            item.setPrecoTotal(precoTotal);

            log.debug("Item SKU: {} | Qtd: {} | Preço unitário: {} | Preço total: {}", item.getSku(), item.getQuantidade(), preco, precoTotal);

            total = total.add(precoTotal);
        }

        pedidoProcessor.setTotal(total);
        log.info("Cálculo finalizado para pedido ID: {}. Valor total: {}", pedidoProcessor.getId(), total);
    }

}
