package br.com.fiap.mspedidoprocessor.core.usecase.pedidoprocessor;

import br.com.fiap.mspedidoprocessor.adapter.external.pagamentoservice.PagamentoServiceClient;
import br.com.fiap.mspedidoprocessor.adapter.external.pagamentoservice.dto.PagamentoRequestDTO;
import br.com.fiap.mspedidoprocessor.adapter.external.pagamentoservice.dto.PagamentoResponseDTO;
import br.com.fiap.mspedidoprocessor.core.domain.PagamentoDTO;
import br.com.fiap.mspedidoprocessor.core.domain.PedidoProcessor;
import br.com.fiap.mspedidoprocessor.core.domain.PedidoStatus;
import br.com.fiap.mspedidoprocessor.core.exception.PagamentoException;
import br.com.fiap.mspedidoprocessor.core.gateways.PagamentoServiceGateway;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@AllArgsConstructor
public class PagamentoUseCase {

    private final PagamentoServiceGateway pagamentoGateway;

    public PagamentoDTO processarPagamento(PedidoProcessor pedidoProcessor) {
        log.info("Iniciando processamento de pagamento para pedido ID: {}", pedidoProcessor.getId());
        PagamentoDTO request = new PagamentoDTO(
                pedidoProcessor.getId().toString(),
                pedidoProcessor.getTotal(),
                pedidoProcessor.getNumeroCartao(),
                PedidoStatus.ABERTO
        );

        PagamentoRequestDTO request2 = new PagamentoRequestDTO(
                pedidoProcessor.getId().toString(),
                pedidoProcessor.getTotal(),
                pedidoProcessor.getNumeroCartao()
        );


        try {
            PagamentoResponseDTO response = pagamentoGateway.solicitarPagamento(request2);
            if (response == null) {
                log.error("Resposta de pagamento nula para pedido ID: {}", pedidoProcessor.getId());
                throw new PagamentoException("Resposta de pagamento nula");
            }
            log.info("Pagamento processado para pedido ID: {}. Status: {}", pedidoProcessor.getId(), response.status());
            PagamentoDTO pagamentoDTO = new PagamentoDTO(
                    response.pedidoId().toString(),
                    response.valor(),
                    pedidoProcessor.getNumeroCartao(),
                    PedidoStatus.valueOf(response.status())
            );
            return pagamentoDTO;
        } catch (Exception e) {
            log.error("Erro ao processar pagamento para pedido ID: {}. Motivo: {}", pedidoProcessor.getId(), e.getMessage());
            throw e;
        }
    }

}
