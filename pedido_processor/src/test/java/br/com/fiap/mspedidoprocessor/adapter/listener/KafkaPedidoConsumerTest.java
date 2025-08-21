package br.com.fiap.mspedidoprocessor.adapter.listener;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.kafka.support.Acknowledgment;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.fiap.mspedidoprocessor.adapter.external.clientkafka.dto.Pedido;
import br.com.fiap.mspedidoprocessor.adapter.mapper.PedidoProcessorMapper;
import br.com.fiap.mspedidoprocessor.core.domain.PedidoProcessor;
import br.com.fiap.mspedidoprocessor.core.exception.ProcessamentoMensagemException;
import br.com.fiap.mspedidoprocessor.core.usecase.pedidoprocessor.ProcessarPedidoUseCase;

class KafkaPedidoConsumerTest {

    @Mock
    private ProcessarPedidoUseCase pedidoProcessor;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private PedidoProcessorMapper pedidoProcessorMapper;

    @InjectMocks
    private KafkaPedidoConsumer consumer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveProcessarMensagemComSucesso() throws Exception {
        String json = "{\"pedidoId\":1,\"clienteId\":2,\"itens\":[]}";
        Pedido pedido = new Pedido(); // Simples, pode ser expandido conforme DTO real
        PedidoProcessor processor = new PedidoProcessor();

        ConsumerRecord<String, String> record = new ConsumerRecord<>("topico", 0, 0, null, json);
        Acknowledgment ack = mock(Acknowledgment.class);

        when(objectMapper.readValue(json, Pedido.class)).thenReturn(pedido);
        when(pedidoProcessorMapper.toPedidoProcessor(pedido)).thenReturn(processor);

        consumer.listener(record, ack);

        verify(pedidoProcessor).processar(processor);
        verify(ack).acknowledge();
    }

    @Test
    void deveLancarExcecaoQuandoJsonInvalido() throws Exception {
        String jsonInvalido = "{json: invalido}";
        ConsumerRecord<String, String> record = new ConsumerRecord<>("topico", 0, 0, null, jsonInvalido);
        Acknowledgment ack = mock(Acknowledgment.class);

        when(objectMapper.readValue(jsonInvalido, Pedido.class)).thenThrow(new RuntimeException("Erro de parsing"));

        ProcessamentoMensagemException exception = assertThrows(
                ProcessamentoMensagemException.class,
                () -> consumer.listener(record, ack)
        );

        assertTrue(exception.getMessage().contains("Erro ao processar mensagem"));
        verify(ack, never()).acknowledge();
    }
}
