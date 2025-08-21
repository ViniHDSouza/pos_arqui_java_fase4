package br.com.fiap.mspedidoprocessor.core.usecase.pedidoprocessor;

import br.com.fiap.mspedidoprocessor.adapter.external.estoqueservice.dto.BaixaEstoqueRequestDTO;
import br.com.fiap.mspedidoprocessor.core.domain.ItemPedidoProcessor;
import br.com.fiap.mspedidoprocessor.core.domain.PedidoProcessor;
import br.com.fiap.mspedidoprocessor.core.exception.EstoqueInsuficienteException;
import br.com.fiap.mspedidoprocessor.core.gateways.EstoqueServiceGateway;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Slf4j
@Component
@AllArgsConstructor
public class EstoqueUseCase {

    private final EstoqueServiceGateway estoqueGateway;

    public void baixarEstoque(PedidoProcessor pedidoProcessor) {
        log.info("Iniciando baixa de estoque para pedido ID: {}", pedidoProcessor.getId());
        for (var item : pedidoProcessor.getItens()) {
            try {
                log.debug("Baixando estoque do SKU: {} | Qtd: {}", item.getSku(), item.getQuantidade());
                ItemPedidoProcessor itemEstoque = new ItemPedidoProcessor(item.getSku(), item.getQuantidade(), BigDecimal.ZERO,BigDecimal.ZERO);
                BaixaEstoqueRequestDTO itemEstoque2 = new BaixaEstoqueRequestDTO(item.getSku(), item.getQuantidade());
                estoqueGateway.baixaEstoque(itemEstoque2);
                log.debug("Estoque baixado para SKU: {}", item.getSku());
            } catch (Exception e) {
                log.error("Erro ao baixar estoque do SKU: {}. Revertendo estoque.", item.getSku());
                reverterEstoque(pedidoProcessor);
                throw new EstoqueInsuficienteException(item.getSku());
            }
        }
        log.info("Baixa de estoque finalizada para pedido ID: {}", pedidoProcessor.getId());
    }

    public void reverterEstoque(PedidoProcessor pedidoProcessor) {
        log.info("Revertendo estoque para pedido ID: {}", pedidoProcessor.getId());
        pedidoProcessor.getItens().forEach(item -> {
            try {
                log.debug("Revertendo estoque do SKU: {} | Qtd: {}", item.getSku(), item.getQuantidade());
                ItemPedidoProcessor itemEstoque = new ItemPedidoProcessor(item.getSku(), item.getQuantidade(), BigDecimal.ZERO,BigDecimal.ZERO);
                BaixaEstoqueRequestDTO itemEstoque2 = new BaixaEstoqueRequestDTO(item.getSku(), item.getQuantidade());
                estoqueGateway.reverterEstoque(itemEstoque2);
                log.debug("Estoque revertido para SKU: {}", item.getSku());
            } catch (Exception e) {
                log.error("Erro ao reverter estoque do SKU: {}", item.getSku());
            }
        });
        log.info("Reversão de estoque finalizada para pedido ID: {}", pedidoProcessor.getId());
    }

}
