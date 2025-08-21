package br.com.fiap.mspedidoreciver.adapter.controller;

import br.com.fiap.mspedidoreciver.adapter.controller.request.PedidoReciverResponseDTO;
import br.com.fiap.mspedidoreciver.adapter.controller.response.PedidoReciverRequestDTO;
import br.com.fiap.mspedidoreciver.adapter.mapper.PedidoMapper;
import br.com.fiap.mspedidoreciver.core.domain.Pedido;
import br.com.fiap.mspedidoreciver.core.usecase.pedidoreciver.CriarPedidoReciverUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/pedidos-reciver")
public class PedidoReciverApiController implements PedidoReciverController{


    private final CriarPedidoReciverUseCase criarPedidoReciverUseCase;
    private final PedidoMapper pedidoMapper;
    @PostMapping
    @Override
    public ResponseEntity<PedidoReciverRequestDTO> criarPedido(PedidoReciverResponseDTO pedidoReciverResponseDTO) {
        Pedido pedidoInput = pedidoMapper.toPedidoDomain(pedidoReciverResponseDTO);
        Pedido pedidoRetorno = criarPedidoReciverUseCase.execute(pedidoInput);
        PedidoReciverRequestDTO pedidoReciverRequestDTO = pedidoMapper.toPedidoReciverRequestDTO(pedidoRetorno);

        return ResponseEntity.ok(pedidoReciverRequestDTO);
    }
}
