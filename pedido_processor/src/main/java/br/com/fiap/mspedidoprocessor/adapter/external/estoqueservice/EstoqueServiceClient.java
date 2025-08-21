package br.com.fiap.mspedidoprocessor.adapter.external.estoqueservice;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import br.com.fiap.mspedidoprocessor.adapter.external.estoqueservice.dto.BaixaEstoqueRequestDTO;
import br.com.fiap.mspedidoprocessor.adapter.external.estoqueservice.dto.EstoqueResponseDTO;
import br.com.fiap.mspedidoprocessor.core.gateways.EstoqueServiceGateway;

@FeignClient(name = "estoque-service", url = "${services.estoque}")
public interface EstoqueServiceClient extends EstoqueServiceGateway {

    @PutMapping("/estoque/baixa")
    void baixaEstoque(@RequestBody BaixaEstoqueRequestDTO request);

    @PutMapping("/estoque/estorno")
    void reverterEstoque(@RequestBody BaixaEstoqueRequestDTO request);

    @GetMapping("/estoque/consulta/{sku}")
    EstoqueResponseDTO consultaProduto(@PathVariable("sku") String sku);

}