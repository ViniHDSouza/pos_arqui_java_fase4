package br.com.fiap.mspedidoreciver.adapter.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Map;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.test.util.ReflectionTestUtils;

import br.com.fiap.mspedidoreciver.core.domain.Pedido;

class KafkaConfigTest {

    private KafkaConfig kafkaConfig;

    @BeforeEach
    void setUp() {
        kafkaConfig = new KafkaConfig();
        ReflectionTestUtils.setField(kafkaConfig, "bootstrapServers", "localhost:9092");
    }

    @Test
    void deveCriarProducerFactoryComConfiguracoesCorretas() {
        ProducerFactory<String, Pedido> factory = kafkaConfig.producerFactory();
        assertNotNull(factory);

        Map<String, Object> configMap = factory.getConfigurationProperties();

        assertEquals("localhost:9092", configMap.get(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG));
        assertEquals(StringSerializer.class, configMap.get(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG));
        assertEquals(JsonSerializer.class, configMap.get(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG));
        assertEquals(true, configMap.get(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG));
        assertEquals("all", configMap.get(ProducerConfig.ACKS_CONFIG));
        assertEquals(Integer.MAX_VALUE, configMap.get(ProducerConfig.RETRIES_CONFIG));
    }

    @Test
    void deveCriarKafkaTemplate() {
        KafkaTemplate<String, Pedido> kafkaTemplate = kafkaConfig.kafkaTemplate();
        assertNotNull(kafkaTemplate);
    }
}
