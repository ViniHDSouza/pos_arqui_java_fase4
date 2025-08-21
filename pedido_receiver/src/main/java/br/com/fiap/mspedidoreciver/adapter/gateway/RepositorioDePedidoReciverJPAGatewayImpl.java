package br.com.fiap.mspedidoreciver.adapter.gateway;

import br.com.fiap.mspedidoreciver.adapter.mapper.PedidoMapper;
import br.com.fiap.mspedidoreciver.adapter.persistence.entity.PedidoEntity;
import br.com.fiap.mspedidoreciver.adapter.persistence.repository.PedidoReciverJpaRepository;
import br.com.fiap.mspedidoreciver.core.domain.Pedido;
import br.com.fiap.mspedidoreciver.core.gateways.PedidoReciverGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RepositorioDePedidoReciverJPAGatewayImpl implements PedidoReciverGateway {

    private final PedidoReciverJpaRepository pedidoReciverRepository;
    private final PedidoMapper pedidoMapper;

    @Override
    public Pedido processarPedidoReciver(Pedido pedido) {
        PedidoEntity pedidoEntity = pedidoMapper.toPedidoEntity(pedido);
        PedidoEntity save = pedidoReciverRepository.save(pedidoEntity);
        return pedidoMapper.toPedidoDomain(save);
    }

}
