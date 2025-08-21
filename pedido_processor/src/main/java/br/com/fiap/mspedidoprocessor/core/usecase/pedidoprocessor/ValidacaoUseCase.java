package br.com.fiap.mspedidoprocessor.core.usecase.pedidoprocessor;

import br.com.fiap.mspedidoprocessor.adapter.external.estoqueservice.dto.EstoqueResponseDTO;
import br.com.fiap.mspedidoprocessor.core.domain.PedidoProcessor;
import br.com.fiap.mspedidoprocessor.core.domain.PedidoStatus;
import br.com.fiap.mspedidoprocessor.core.exception.ClienteNaoEncontradoException;
import br.com.fiap.mspedidoprocessor.core.exception.EstoqueInsuficienteException;
import br.com.fiap.mspedidoprocessor.core.exception.ProdutoNaoEncontradoException;
import br.com.fiap.mspedidoprocessor.core.gateways.ClienteServiceGateway;
import br.com.fiap.mspedidoprocessor.core.gateways.EstoqueServiceGateway;
import br.com.fiap.mspedidoprocessor.core.gateways.ProdutoServiceGateway;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@AllArgsConstructor
public class ValidacaoUseCase {

    private final ClienteServiceGateway clienteGateway;
    private final ProdutoServiceGateway produtoGateway;
    private final EstoqueServiceGateway estoqueGateway;


    public void validarCliente(PedidoProcessor pedidoProcessor) {
        log.info("Validando cliente para pedido ID: {} | Cliente ID: {}", pedidoProcessor.getPedidoReciverId(), pedidoProcessor.getClienteId());
        if (!clienteGateway.clienteExiste(pedidoProcessor.getClienteId())) {
            log.warn("Cliente não encontrado para pedido ID: {} | Cliente ID: {}", pedidoProcessor.getPedidoReciverId(), pedidoProcessor.getClienteId());
            pedidoProcessor.setStatus(PedidoStatus.FALHA_CLIENTE_NAOENCONTRADO);
            throw new ClienteNaoEncontradoException(pedidoProcessor.getClienteId());
        }
        log.info("Cliente validado para pedido ID: {}", pedidoProcessor.getPedidoReciverId());
    }

    public void validarProdutos(PedidoProcessor pedidoProcessor) {
        log.info("Validando produtos para pedido ID: {}", pedidoProcessor.getPedidoReciverId());
        pedidoProcessor.getItens().forEach(item -> {
            if (!produtoGateway.skuExiste(item.getSku())) {
                log.warn("Produto não encontrado para pedido ID: {} | SKU: {}", pedidoProcessor.getPedidoReciverId(), item.getSku());
                pedidoProcessor.setStatus(PedidoStatus.FALHA_PRODUTO_NAOENCONTRADO);
                throw new ProdutoNaoEncontradoException(item.getSku());
            }
            int  estoqueAtual = estoqueGateway.consultaProduto(item.getSku()).quantidade();
            if (estoqueAtual < item.getQuantidade()) {
                log.warn("Estoque insuficiente para pedido ID: {} | SKU: {} | Quantidade solicitada: {} | Estoque atual: {}", pedidoProcessor.getPedidoReciverId(), item.getSku(), item.getQuantidade(), estoqueAtual);
                pedidoProcessor.setStatus(PedidoStatus.FECHADO_SEM_ESTOQUE);
                throw new EstoqueInsuficienteException(item.getSku());
            }
            log.debug("Produto validado para pedido ID: {} | SKU: {}", pedidoProcessor.getPedidoReciverId(), item.getSku());
        });
        log.info("Todos os produtos validados para pedido ID: {}", pedidoProcessor.getPedidoReciverId());
    }
}
