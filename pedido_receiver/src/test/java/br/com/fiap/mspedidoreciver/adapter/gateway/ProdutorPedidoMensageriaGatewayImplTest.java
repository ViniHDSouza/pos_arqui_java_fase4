package br.com.fiap.mspedidoreciver.adapter.gateway;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.concurrent.CompletableFuture;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.test.util.ReflectionTestUtils;

import br.com.fiap.mspedidoreciver.core.domain.Pedido;

class ProdutorPedidoMensageriaGatewayImplTest {

    @Mock
    private KafkaTemplate<String, Pedido> kafkaTemplate;

    @InjectMocks
    private ProdutorPedidoMensageriaGatewayImpl gateway;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(gateway, "topicName", "test-topic");
    }

    @Test
    void devePublicarPedidoNoKafka() {
        Pedido pedidoMock = new Pedido();

        @SuppressWarnings("unchecked")
        SendResult<String, Pedido> sendResultMock = mock(SendResult.class);

        CompletableFuture<SendResult<String, Pedido>> future = CompletableFuture.completedFuture(sendResultMock);

        when(kafkaTemplate.send(anyString(), any(Pedido.class))).thenReturn(future);

        gateway.publicarPedido(pedidoMock);

        verify(kafkaTemplate, times(1)).send("test-topic", pedidoMock);
    }
}
