package br.com.fiap.mspedidoreciver.adapter.controller;

import br.com.fiap.mspedidoreciver.adapter.controller.request.PedidoReciverResponseDTO;
import br.com.fiap.mspedidoreciver.adapter.controller.response.PedidoReciverRequestDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

public interface PedidoReciverController {
    public ResponseEntity<PedidoReciverRequestDTO> criarPedido(@RequestBody PedidoReciverResponseDTO pedido);

}
