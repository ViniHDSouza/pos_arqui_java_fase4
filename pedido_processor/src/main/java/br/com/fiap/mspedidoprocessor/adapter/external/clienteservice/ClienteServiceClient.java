package br.com.fiap.mspedidoprocessor.adapter.external.clienteservice;

import br.com.fiap.mspedidoprocessor.core.gateways.ClienteServiceGateway;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@FeignClient(name = "cliente-service", url = "${services.cliente}")
public interface ClienteServiceClient extends ClienteServiceGateway {

    @GetMapping("/clientes/existe/{id}")
    boolean clienteExiste(@PathVariable("id") Long id);
}