package br.com.fiap.mspedidoprocessor.core.usecase.pedidoprocessor;

import br.com.fiap.mspedidoprocessor.core.domain.PagamentoCallback;
import br.com.fiap.mspedidoprocessor.core.gateways.ConsultaPedidoProcessorServiceGateway;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@AllArgsConstructor
public class ConsultaPedidoProcessorUseCase {

    private final ConsultaPedidoProcessorServiceGateway consultaPedidoProcessorServiceGateway;

    public PagamentoCallback consultarPagamentoCallback(Long pedidoId) {
        log.info("Consultando pagamento callback para pedido ID: {}", pedidoId);
        PagamentoCallback callback = consultaPedidoProcessorServiceGateway.consultarPagamentoCallback(pedidoId);
        log.info("Consulta finalizada para pedido ID: {}. Resultado: {}", pedidoId, callback);
        return callback;
    }
}
