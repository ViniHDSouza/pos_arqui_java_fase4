package br.com.fiap.mspedidoprocessor.adapter.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.lang.reflect.Field;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;

class KafkaConfigTest {

    private KafkaConfig kafkaConfig;

    @BeforeEach
    void setUp() throws Exception {
        kafkaConfig = new KafkaConfig();
        setField(kafkaConfig, "bootstrapServers", "localhost:9092");
        setField(kafkaConfig, "groupId", "test-group");
    }

    @Test
    void deveCriarConsumerFactoryComConfiguracoesCorretas() {
        ConsumerFactory<String, String> consumerFactory = kafkaConfig.consumerFactory();
        assertNotNull(consumerFactory);

        Map<String, Object> config = consumerFactory.getConfigurationProperties();

        assertEquals("localhost:9092", config.get(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG));
        assertEquals("test-group", config.get(ConsumerConfig.GROUP_ID_CONFIG));
        assertEquals("earliest", config.get(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG));
        assertEquals(ErrorHandlingDeserializer.class, config.get(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG));
        assertEquals(ErrorHandlingDeserializer.class, config.get(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG));
        assertEquals(StringDeserializer.class, config.get("spring.deserializer.key.delegate.class"));
        assertEquals(StringDeserializer.class, config.get("spring.deserializer.value.delegate.class"));
    }

    @Test
    void deveCriarKafkaListenerContainerFactoryComAckManual() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = kafkaConfig.kafkaListenerContainerFactory();
        assertNotNull(factory);

        ContainerProperties.AckMode ackMode = factory.getContainerProperties().getAckMode();
        assertEquals(ContainerProperties.AckMode.MANUAL, ackMode);
    }

    private void setField(Object target, String fieldName, Object value) throws Exception {
        Field field = KafkaConfig.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }
}
