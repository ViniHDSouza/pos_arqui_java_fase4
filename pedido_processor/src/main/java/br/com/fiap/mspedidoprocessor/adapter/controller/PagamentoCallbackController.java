package br.com.fiap.mspedidoprocessor.adapter.controller;


import br.com.fiap.mspedidoprocessor.adapter.controller.response.PagamentoCallbackResponseDTO;
import br.com.fiap.mspedidoprocessor.adapter.mapper.PedidoProcessorMapper;
import br.com.fiap.mspedidoprocessor.core.domain.PagamentoCallback;
import br.com.fiap.mspedidoprocessor.core.usecase.pedidoprocessor.ConsultaPedidoProcessorUseCase;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/pagamento")
public class PagamentoCallbackController {

    private final ConsultaPedidoProcessorUseCase consultaPedidoProcessorUseCase;

    private final PedidoProcessorMapper pedidoProcessorMapper;

    @GetMapping("/consultar/{id}")
    public ResponseEntity<PagamentoCallbackResponseDTO> consultaPagamento(@PathVariable Long id) {

        PagamentoCallback pagamentoCallback = consultaPedidoProcessorUseCase.consultarPagamentoCallback(id);
        PagamentoCallbackResponseDTO pagamentoCallbackResponseDTO = pedidoProcessorMapper.toPagamentoCallbackResponseDTO(pagamentoCallback);
        if (pagamentoCallbackResponseDTO == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(pagamentoCallbackResponseDTO);

    }

}