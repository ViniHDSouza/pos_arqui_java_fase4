package br.com.fiap.mspedidoprocessor.core.gateways;

import br.com.fiap.mspedidoprocessor.adapter.external.estoqueservice.dto.BaixaEstoqueRequestDTO;
import br.com.fiap.mspedidoprocessor.adapter.external.estoqueservice.dto.EstoqueResponseDTO;
import br.com.fiap.mspedidoprocessor.core.domain.ItemPedidoProcessor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

public interface EstoqueServiceGateway {
    void baixaEstoque(BaixaEstoqueRequestDTO request);
    void reverterEstoque(BaixaEstoqueRequestDTO request);
    EstoqueResponseDTO consultaProduto(String sku);
}