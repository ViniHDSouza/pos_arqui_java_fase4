package br.com.fiap.tech_challenge_4a_fase_pagamento.adapter.controller;


import br.com.fiap.tech_challenge_4a_fase_pagamento.adapter.controller.request.SolicitacaoPagamentoRequestDTO;
import br.com.fiap.tech_challenge_4a_fase_pagamento.adapter.controller.response.PagamentoResponseDTO;
import br.com.fiap.tech_challenge_4a_fase_pagamento.adapter.external.dto.CallbackPagamentoDTO;
import br.com.fiap.tech_challenge_4a_fase_pagamento.core.domain.Pagamento;
import br.com.fiap.tech_challenge_4a_fase_pagamento.core.domain.StatusPagamento;
import br.com.fiap.tech_challenge_4a_fase_pagamento.core.usecase.pagamento.ConsultarPagamentoUseCase;
import br.com.fiap.tech_challenge_4a_fase_pagamento.core.usecase.pagamento.EstornarPagamentoUseCase;
import br.com.fiap.tech_challenge_4a_fase_pagamento.core.usecase.pagamento.ProcessarPagamentoUseCase;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/pagamentos")
@CrossOrigin(origins = "*")
public class PagamentoController {

    private final ProcessarPagamentoUseCase processarPagamentoUseCase;
    private final ConsultarPagamentoUseCase consultarPagamentoUseCase;
    private final EstornarPagamentoUseCase estornarPagamentoUseCase;

    public PagamentoController(ProcessarPagamentoUseCase processarPagamentoUseCase,
                               ConsultarPagamentoUseCase consultarPagamentoUseCase,
                               EstornarPagamentoUseCase estornarPagamentoUseCase) {
        this.processarPagamentoUseCase = processarPagamentoUseCase;
        this.consultarPagamentoUseCase = consultarPagamentoUseCase;
        this.estornarPagamentoUseCase = estornarPagamentoUseCase;
    }

    @PostMapping
    public ResponseEntity<PagamentoResponseDTO> processarPagamento(@Valid @RequestBody SolicitacaoPagamentoRequestDTO solicitacao) {
        Pagamento pagamento = processarPagamentoUseCase.processar(
                solicitacao.getPedidoId(),
                solicitacao.getClienteId(),
                solicitacao.getNumeroCartao(),
                solicitacao.getValor()
        );
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new PagamentoResponseDTO(pagamento));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PagamentoResponseDTO> consultarPagamento(@PathVariable Long id) {
        Optional<Pagamento> pagamento = consultarPagamentoUseCase.buscarPorId(id);
        return pagamento.map(p -> ResponseEntity.ok(new PagamentoResponseDTO(p)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<PagamentoResponseDTO>> listarPagamentos() {
        List<Pagamento> pagamentos = consultarPagamentoUseCase.listarTodos();
        List<PagamentoResponseDTO> response = pagamentos.stream()
                .map(PagamentoResponseDTO::new)
                .toList();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/pedido/{pedidoId}")
    public ResponseEntity<List<PagamentoResponseDTO>> consultarPagamentosPorPedido(@PathVariable Long pedidoId) {
        List<Pagamento> pagamentos = consultarPagamentoUseCase.buscarPorPedidoId(pedidoId);
        List<PagamentoResponseDTO> response = pagamentos.stream()
                .map(PagamentoResponseDTO::new)
                .toList();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<PagamentoResponseDTO>> consultarPagamentosPorCliente(@PathVariable Long clienteId) {
        List<Pagamento> pagamentos = consultarPagamentoUseCase.buscarPorClienteId(clienteId);
        List<PagamentoResponseDTO> response = pagamentos.stream()
                .map(PagamentoResponseDTO::new)
                .toList();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<PagamentoResponseDTO>> consultarPagamentosPorStatus(@PathVariable StatusPagamento status) {
        List<Pagamento> pagamentos = consultarPagamentoUseCase.buscarPorStatus(status);
        List<PagamentoResponseDTO> response = pagamentos.stream()
                .map(PagamentoResponseDTO::new)
                .toList();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/estorno")
    public ResponseEntity<PagamentoResponseDTO> estornarPagamento(@PathVariable Long id) {
        Pagamento pagamento = estornarPagamentoUseCase.estornar(id);
        return ResponseEntity.ok(new PagamentoResponseDTO(pagamento));
    }

    @PostMapping("/callback")
    public ResponseEntity<Void> receberCallback2(@Valid @RequestBody CallbackPagamentoDTO callback) {
        try {

            Optional<Pagamento> pagamentoOpt = consultarPagamentoUseCase
                    .buscarPorTransacaoExternaId(callback.getTransacaoExternaId());

            if (pagamentoOpt.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.ok().build();

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Microsserviço de Pagamento está funcionando");
    }
}