package br.com.fiap.mspedidoreciver.adapter.gateway;

import br.com.fiap.mspedidoreciver.core.domain.Pedido;
import br.com.fiap.mspedidoreciver.core.gateways.ProdutorPedidoMensageriaGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

@Service
@RequiredArgsConstructor
public class ProdutorPedidoMensageriaGatewayImpl implements ProdutorPedidoMensageriaGateway {

    @Value("${app.kafka.topic.name}")
    private String topicName;

    private final KafkaTemplate<String, Pedido> kafkaTemplate;

    @Override
    public void publicarPedido(Pedido pedido) {
        kafkaTemplate.send(topicName, pedido);
    }
}
