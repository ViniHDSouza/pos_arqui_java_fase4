package br.com.fiap.mspedidoprocessor.adapter.listener;

import br.com.fiap.mspedidoprocessor.adapter.external.clientkafka.dto.Pedido;
import br.com.fiap.mspedidoprocessor.adapter.mapper.PedidoProcessorMapper;
import br.com.fiap.mspedidoprocessor.core.domain.PedidoProcessor;
import br.com.fiap.mspedidoprocessor.core.exception.ProcessamentoMensagemException;
import br.com.fiap.mspedidoprocessor.core.usecase.pedidoprocessor.ProcessarPedidoUseCase;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaPedidoConsumer {

    private final ProcessarPedidoUseCase pedidoProcessor;
    private final ObjectMapper objectMapper;
    private final PedidoProcessorMapper pedidoProcessorMapper;

    @KafkaListener(topics = "topico-pedido-reciver", groupId = "grupo-pedido-reciver")
    public void listener(ConsumerRecord<String, String> record, Acknowledgment ack) {

        try {
            processarMensagem(record);
            ack.acknowledge();
        } catch (Exception e) {
            log.error("Erro ao processar mensagem. A mensagem será reprocessada. Erro: {}", e.getMessage(), e);
            throw new ProcessamentoMensagemException("Erro ao processar mensagem:", e);
        }
    }

    private void processarMensagem(ConsumerRecord<String, String> record) throws Exception {
        String json = record.value();
        Pedido pedidoRetorno = objectMapper.readValue(json, Pedido.class);
        PedidoProcessor pedidoProcessor = pedidoProcessorMapper.toPedidoProcessor(pedidoRetorno);
        this.pedidoProcessor.processar(pedidoProcessor);
    }
}