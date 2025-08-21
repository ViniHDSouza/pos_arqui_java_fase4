package br.com.fiap.mspedidoprocessor.adapter.external.pagamentoservice;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import br.com.fiap.mspedidoprocessor.adapter.external.pagamentoservice.dto.PagamentoRequestDTO;
import br.com.fiap.mspedidoprocessor.adapter.external.pagamentoservice.dto.PagamentoResponseDTO;
import br.com.fiap.mspedidoprocessor.core.gateways.PagamentoServiceGateway;

@FeignClient(name = "pagamento-service", url = "${services.pagamento}")
public interface PagamentoServiceClient extends PagamentoServiceGateway {

    @PostMapping("/pagamentos")
    PagamentoResponseDTO solicitarPagamento(@RequestBody PagamentoRequestDTO request);

}