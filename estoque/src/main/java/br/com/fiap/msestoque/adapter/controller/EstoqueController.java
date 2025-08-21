package br.com.fiap.msestoque.adapter.controller;

import br.com.fiap.msestoque.adapter.controller.request.BaixaEstoqueRequestDTO;
import br.com.fiap.msestoque.adapter.controller.response.EstoqueResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

public interface EstoqueController {

    public ResponseEntity<Void> baixaEstoque(BaixaEstoqueRequestDTO request);

    public ResponseEntity<Void> estornoEstoque(@RequestBody BaixaEstoqueRequestDTO request);

    public ResponseEntity<EstoqueResponseDTO> consultarEstoque(@PathVariable String sku);
}
