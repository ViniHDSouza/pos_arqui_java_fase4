package br.com.fiap.mspedidoprocessor.core.usecase.pedidoprocessor;

import org.springframework.stereotype.Component;

import br.com.fiap.mspedidoprocessor.adapter.mapper.PedidoProcessorMapper;
import br.com.fiap.mspedidoprocessor.core.domain.PagamentoDTO;
import br.com.fiap.mspedidoprocessor.core.domain.PedidoProcessor;
import br.com.fiap.mspedidoprocessor.core.domain.PedidoStatus;
import br.com.fiap.mspedidoprocessor.core.exception.ClienteNaoEncontradoException;
import br.com.fiap.mspedidoprocessor.core.exception.EstoqueException;
import br.com.fiap.mspedidoprocessor.core.exception.PagamentoException;
import br.com.fiap.mspedidoprocessor.core.exception.ProdutoNaoEncontradoException;
import br.com.fiap.mspedidoprocessor.core.gateways.ClienteServiceGateway;
import br.com.fiap.mspedidoprocessor.core.gateways.EstoqueServiceGateway;
import br.com.fiap.mspedidoprocessor.core.gateways.PagamentoServiceGateway;
import br.com.fiap.mspedidoprocessor.core.gateways.PedidoRepositoryGateway;
import br.com.fiap.mspedidoprocessor.core.gateways.ProdutoServiceGateway;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@AllArgsConstructor
public class ProcessarPedidoUseCase {

    private final PedidoRepositoryGateway pedidoRepositoryGateway;
    private final ClienteServiceGateway clienteGateway;
    private final ProdutoServiceGateway produtoGateway;
    private final EstoqueServiceGateway estoqueGateway;
    private final PagamentoServiceGateway pagamentoGateway;
    private final PedidoProcessorMapper pedidoProcessorMapper;


    private final ValidacaoUseCase validacaoService;
    private final CalculoUseCase calculoService;
    private final EstoqueUseCase estoqueService;
    private final PagamentoUseCase pagamentoService;

    public void processar(PedidoProcessor pedidoProcessor) {
        log.info("Iniciando processamento do pedido ID: {}", pedidoProcessor.getId());
        try {
            inicializarPedido(pedidoProcessor);
            validarDados(pedidoProcessor);
            calcularTotais(pedidoProcessor);
            processarEstoque(pedidoProcessor);
            processarPagamento(pedidoProcessor);
            log.info("Processamento do pedido ID: {} finalizado com sucesso", pedidoProcessor.getPedidoReciverId());
        } catch (Exception e) {
            log.error("Erro ao processar pedido ID: {}. Motivo: {}", pedidoProcessor.getPedidoReciverId(), e.getMessage());
            tratarErro(pedidoProcessor, e);
            throw e;
        }
    }

    private void inicializarPedido(PedidoProcessor pedidoProcessor) {
        log.info("Inicializando pedido ID: {}", pedidoProcessor.getId());
        pedidoProcessor.setStatus(PedidoStatus.ABERTO);
        pedidoRepositoryGateway.salvar(pedidoProcessor);
        log.info("Pedido ID: {} inicializado com status ABERTO", pedidoProcessor.getId());
    }

    private void processarEstoque(PedidoProcessor pedidoProcessor) {
        log.info("Processando estoque para pedido ID: {}", pedidoProcessor.getId());
        estoqueService.baixarEstoque(pedidoProcessor);
        log.info("Estoque processado para pedido ID: {}", pedidoProcessor.getId());
    }

    private void processarPagamento(PedidoProcessor pedidoProcessor) {
        log.info("Processando pagamento para pedido ID: {}", pedidoProcessor.getId());
        var pagamentoResponse = pagamentoService.processarPagamento(pedidoProcessor);
        log.info("Pagamento processado para pedido ID: {}. Status: {}", pedidoProcessor.getId(), pagamentoResponse.status());
        atualizarStatusFinal(pedidoProcessor, pagamentoResponse);
        pedidoRepositoryGateway.salvar(pedidoProcessor);
    }

    private void validarDados(PedidoProcessor pedidoProcessor) {
        log.info("Validando dados do pedido ID: {}", pedidoProcessor.getPedidoReciverId());
        validacaoService.validarCliente(pedidoProcessor);
        validacaoService.validarProdutos(pedidoProcessor);
        log.info("Dados validados para pedido ID: {}", pedidoProcessor.getPedidoReciverId());
    }

    private void calcularTotais(PedidoProcessor pedidoProcessor) {
        log.info("Calculando totais para pedido ID: {}", pedidoProcessor.getId());
        calculoService.calcularTotal(pedidoProcessor);
        pedidoRepositoryGateway.salvar(pedidoProcessor);
        log.info("Totais calculados para pedido ID: {}. Valor total: {}", pedidoProcessor.getId(), pedidoProcessor.getTotal());
    }

    private void atualizarStatusFinal(PedidoProcessor pedidoProcessor, PagamentoDTO response) {
        log.info("Atualizando status final do pedido ID: {} conforme pagamento: {}", pedidoProcessor.getId(), response.status());
        PedidoStatus status = switch (response.status()) {
            case APROVADO -> PedidoStatus.APROVADO;
            case PROCESSANDO -> PedidoStatus.PROCESSANDO;
            case RECUSADO -> PedidoStatus.RECUSADO;
            default -> throw new PagamentoException("Status de pagamento inválido: " + response.status());
        };
        pedidoProcessor.setStatus(status);
        log.info("Status final do pedido ID: {} atualizado para {}", pedidoProcessor.getId(), status);
    }

    private void tratarErro(PedidoProcessor pedidoProcessor, Exception e) {
        log.warn("Tratando erro para pedido ID: {}. Tipo: {}", pedidoProcessor.getId(), e.getClass().getSimpleName());
        if (e instanceof EstoqueException) {
            pedidoProcessor.setStatus(PedidoStatus.FECHADO_SEM_ESTOQUE);
        } else if (e instanceof PagamentoException) {
            pedidoProcessor.setStatus(PedidoStatus.FALHA_PAGAMENTO);
            estoqueService.reverterEstoque(pedidoProcessor);
        } else if (e instanceof ClienteNaoEncontradoException) {
            pedidoProcessor.setStatus(PedidoStatus.FALHA_CLIENTE_NAOENCONTRADO);
        } else if (e instanceof ProdutoNaoEncontradoException) {
            pedidoProcessor.setStatus(PedidoStatus.FALHA_PRODUTO_NAOENCONTRADO);
        }
        pedidoRepositoryGateway.salvar(pedidoProcessor);
        log.warn("Pedido ID: {} finalizado com status de erro: {}", pedidoProcessor.getId(), pedidoProcessor.getStatus());
    }

}