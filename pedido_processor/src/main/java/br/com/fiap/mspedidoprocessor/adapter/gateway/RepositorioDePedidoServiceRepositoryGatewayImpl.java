package br.com.fiap.mspedidoprocessor.adapter.gateway;

import br.com.fiap.mspedidoprocessor.adapter.mapper.PedidoProcessorMapper;
import br.com.fiap.mspedidoprocessor.adapter.persistence.entity.PedidoProcessorEntity;
import br.com.fiap.mspedidoprocessor.adapter.persistence.repository.PedidoRepositoryJpa;
import br.com.fiap.mspedidoprocessor.core.domain.PagamentoCallback;
import br.com.fiap.mspedidoprocessor.core.domain.PedidoProcessor;
import br.com.fiap.mspedidoprocessor.core.gateways.ConsultaPedidoProcessorServiceGateway;
import br.com.fiap.mspedidoprocessor.core.gateways.PedidoRepositoryGateway;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@AllArgsConstructor
public class RepositorioDePedidoServiceRepositoryGatewayImpl implements PedidoRepositoryGateway, ConsultaPedidoProcessorServiceGateway {

    private final PedidoRepositoryJpa pedidoRepositoryJpa;
    private final PedidoProcessorMapper pedidoProcessorMapper;

    @Override
    public PedidoProcessor salvar(PedidoProcessor pedidoProcessor) {
        if (pedidoProcessor.getId() == null) {
            pedidoProcessor.setId(UUID.randomUUID());
        }
        PedidoProcessorEntity pedidoEntity = pedidoProcessorMapper.toEntity(pedidoProcessor);

        PedidoProcessorEntity save = pedidoRepositoryJpa.save(pedidoEntity);
        return pedidoProcessorMapper.toModel(save);
    }

    @Override
    public PagamentoCallback consultarPagamentoCallback(Long pedidoId) {
        Optional<PedidoProcessorEntity> pedidoReciverId = pedidoRepositoryJpa.findByPedidoReciverId(pedidoId);
        if (pedidoReciverId.isPresent()) {
            PedidoProcessorEntity pedidoEntity = pedidoReciverId.get();
            return pedidoProcessorMapper.toPagamentoCallback(pedidoEntity);
        }
        return null;
    }

}
