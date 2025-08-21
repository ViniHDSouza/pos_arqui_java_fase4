package br.com.fiap.mspedidoreciver.core.usecase.pedidoreciver;

import br.com.fiap.mspedidoreciver.adapter.mapper.PedidoMapper;
import br.com.fiap.mspedidoreciver.adapter.persistence.entity.PedidoEntity;
import br.com.fiap.mspedidoreciver.core.domain.Pedido;
import br.com.fiap.mspedidoreciver.core.gateways.PedidoReciverGateway;
import br.com.fiap.mspedidoreciver.core.gateways.ProdutorPedidoMensageriaGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CriarPedidoReciverUseCase {

    private final PedidoReciverGateway pedidoReciverGateway;
    private final PedidoMapper pedidoMapper;
    private final ProdutorPedidoMensageriaGateway produtorPedidoMensageriaGateway;

    public Pedido execute(Pedido pedido) {
        pedido.setStatus("ABERTO");
        pedido.setDataCriacao(LocalDateTime.now());
        Pedido pedidoSalvo = pedidoReciverGateway.processarPedidoReciver(pedido);
        produtorPedidoMensageriaGateway.publicarPedido(pedidoSalvo);
        return pedidoSalvo;
    }
}
