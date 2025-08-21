package br.com.fiap.msestoque.adapter.controller;

import br.com.fiap.msestoque.adapter.controller.request.BaixaEstoqueRequestDTO;
import br.com.fiap.msestoque.adapter.controller.response.EstoqueResponseDTO;
import br.com.fiap.msestoque.core.exception.EstoqueException;
import br.com.fiap.msestoque.core.usecase.BaixaEstoqueUseCase;
import br.com.fiap.msestoque.core.usecase.ConsultaEstoqueUseCase;
import br.com.fiap.msestoque.core.usecase.EstornarEstoqueUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/estoque")
@RequiredArgsConstructor
public class EstoqueApiController implements EstoqueController {

	private final BaixaEstoqueUseCase baixaEstoqueUseCase;
	private final EstornarEstoqueUseCase estornarEstoqueUseCase;
	private final ConsultaEstoqueUseCase consultaEstoqueUseCase;

	@PutMapping("/baixa")
	@Override
	public ResponseEntity<Void> baixaEstoque(@Valid @RequestBody BaixaEstoqueRequestDTO request) {
		try {
			baixaEstoqueUseCase.executar(request.sku(), request.quantidade());
			return ResponseEntity.ok().build();
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().build();
		}
	}

	@PutMapping("/estorno")
	@Override
	public ResponseEntity<Void> estornoEstoque(@Valid @RequestBody BaixaEstoqueRequestDTO request) {
		try {
			estornarEstoqueUseCase.executar(request.sku(), request.quantidade());
			return ResponseEntity.ok().build();
		} catch (EstoqueException | IllegalArgumentException e) {
			return ResponseEntity.badRequest().build();
		}
	}

	@GetMapping("/consulta/{sku}")
	public ResponseEntity<EstoqueResponseDTO> consultarEstoque(@Valid @PathVariable String sku) {
		try {
			int quantidade = consultaEstoqueUseCase.executar(sku);
			return ResponseEntity.ok(new EstoqueResponseDTO(sku, quantidade));
		} catch (EstoqueException | IllegalArgumentException e) {
			return ResponseEntity.badRequest().build();
		}
	}
}
