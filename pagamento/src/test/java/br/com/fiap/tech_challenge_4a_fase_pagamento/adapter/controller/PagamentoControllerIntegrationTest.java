package br.com.fiap.tech_challenge_4a_fase_pagamento.adapter.controller;


import br.com.fiap.tech_challenge_4a_fase_pagamento.TechChallenge4aFasePagamentoApplication;
import br.com.fiap.tech_challenge_4a_fase_pagamento.adapter.controller.request.SolicitacaoPagamentoRequestDTO;
import br.com.fiap.tech_challenge_4a_fase_pagamento.adapter.external.dto.CallbackPagamentoDTO;
import br.com.fiap.tech_challenge_4a_fase_pagamento.adapter.persistence.repository.PagamentoJpaRepository;
import br.com.fiap.tech_challenge_4a_fase_pagamento.core.domain.Pagamento;
import br.com.fiap.tech_challenge_4a_fase_pagamento.core.domain.StatusPagamento;
import br.com.fiap.tech_challenge_4a_fase_pagamento.core.gateways.PagamentoGateway;
import br.com.fiap.tech_challenge_4a_fase_pagamento.core.usecase.pagamento.ProcessarPagamentoUseCase;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest(classes = TechChallenge4aFasePagamentoApplication.class)
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
@DisplayName("Testes de integração do controlador de pagamentos que verifica todas as operações REST relacionadas a pagamentos")
class PagamentoControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PagamentoGateway pagamentoGateway;

    @Autowired
    private PagamentoJpaRepository pagamentoJpaRepository;

    @Autowired
    private ProcessarPagamentoUseCase processarPagamentoUseCase;

    @BeforeEach
    void setUp() {
        pagamentoJpaRepository.deleteAll();

    }

    @Test
    @DisplayName("Deve criar um pagamento com dados válidos, retornando status 201 e validando todos os campos esperados no response")
    void deveProcessarPagamentoComSucesso() throws Exception {
        SolicitacaoPagamentoRequestDTO request = new SolicitacaoPagamentoRequestDTO(
                1L, 101L, "1234567890123456", new BigDecimal("100.00")
        );

        mockMvc.perform(post("/api/pagamentos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.pedidoId").value(1L))
                .andExpect(jsonPath("$.clienteId").value(101L))
                .andExpect(jsonPath("$.valor").value(100.00))
                .andExpect(jsonPath("$.status").exists());
    }

    @Test
    @DisplayName("Deve retornar status 400 (Bad Request) ao tentar criar pagamento com pedidoId nulo e número de cartão inválido")
    void deveRetornarBadRequestQuandoDadosInvalidos() throws Exception {
        SolicitacaoPagamentoRequestDTO request = new SolicitacaoPagamentoRequestDTO(
                null, 101L, "123", new BigDecimal("100.00") // pedidoId nulo e cartão inválido
        );

        mockMvc.perform(post("/api/pagamentos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Deve consultar um pagamento existente por ID, retornando status 200 e status APROVADO no response")
    void deveConsultarPagamentoPorIdComSucesso() throws Exception {

        Pagamento pagamento = new Pagamento(1L, 101L, "1234567890123456", new BigDecimal("50.00"));
        pagamento.iniciarProcessamento();
        pagamento.aprovar("TRANSACAO_EXTERNA_123"); // Simula aprovação
        pagamento = pagamentoGateway.salvar(pagamento);

        mockMvc.perform(get("/api/pagamentos/{id}", pagamento.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(pagamento.getId()))
                .andExpect(jsonPath("$.status").value(StatusPagamento.APROVADO.name()));
    }

    @Test
    @DisplayName("Deve retornar status 404 (Not Found) ao consultar um pagamento inexistente por ID")
    void deveRetornarNotFoundAoConsultarPagamentoInexistente() throws Exception {
        mockMvc.perform(get("/api/pagamentos/{id}", 999L))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Deve listar todos os pagamentos cadastrados, retornando status 200 e a quantidade correta de registros")
    void deveListarTodosOsPagamentos() throws Exception {

        pagamentoGateway.salvar(new Pagamento(2L, 102L, "1111222233334444", new BigDecimal("200.00")));
        pagamentoGateway.salvar(new Pagamento(3L, 103L, "5555666677778888", new BigDecimal("300.00")));

        mockMvc.perform(get("/api/pagamentos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2)); // Espera 2 pagamentos criados no setup
    }

    @Test
    @DisplayName("Deve estornar um pagamento previamente aprovado, retornando status 200 e status ESTORNADO no response")
    void deveEstornarPagamentoComSucesso() throws Exception {

        Pagamento pagamentoProcessado = processarPagamentoUseCase.processar(1L,10L,"1234567890123456",new BigDecimal("100.0"));

        mockMvc.perform(post("/api/pagamentos/{id}/estorno", pagamentoProcessado.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(pagamentoProcessado.getId()))
                .andExpect(jsonPath("$.status").value(StatusPagamento.ESTORNADO.name()));
    }

    @Test
    @DisplayName("Deve retornar status 409 (Conflict) ao tentar estornar um pagamento que não está aprovado")
    void deveRetornarConflitoAoEstornarPagamentoNaoAprovado() throws Exception {

        Pagamento pagamento = new Pagamento(5L, 105L, "1212343456567878", new BigDecimal("75.00"));
        pagamento = pagamentoGateway.salvar(pagamento);

        mockMvc.perform(post("/api/pagamentos/{id}/estorno", pagamento.getId()))
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("Deve receber callback de pagamento aprovado, atualizar o status do pagamento e retornar status 200")
    void deveReceberCallbackComSucesso() throws Exception {

        Pagamento pagamento = new Pagamento(6L, 106L, "1111222233334444", new BigDecimal("250.00"));
        pagamento.iniciarProcessamento();
        Pagamento pagamentoParaCallback = Pagamento.builder()
                .id(pagamento.getId())
                .pedidoId(pagamento.getPedidoId())
                .clienteId(pagamento.getClienteId())
                .numeroCartao(pagamento.getNumeroCartao())
                .valor(pagamento.getValor())
                .status(pagamento.getStatus())
                .transacaoExternaId("TXN_CALLBACK_PENDING")
                .motivoRejeicao(pagamento.getMotivoRejeicao())
                .dataCriacao(pagamento.getDataCriacao())
                .dataAtualizacao(pagamento.getDataAtualizacao())
                .build();
        pagamentoGateway.salvar(pagamentoParaCallback);

        CallbackPagamentoDTO callback = new CallbackPagamentoDTO();
        callback.setTransacaoExternaId("TXN_CALLBACK_PENDING");
        callback.setStatus("APROVADO");
        callback.setMotivo("Pagamento aprovado pelo sistema externo");

        mockMvc.perform(post("/api/pagamentos/callback")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(callback)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Deve retornar status 404 (Not Found) ao receber callback para uma transação externa inexistente")
    void deveRetornarNotFoundAoReceberCallbackParaTransacaoInexistente() throws Exception {
        CallbackPagamentoDTO callback = new CallbackPagamentoDTO();
        callback.setTransacaoExternaId("TXN_INEXISTENTE");
        callback.setStatus("APROVADO");
        callback.setMotivo("Pagamento aprovado pelo sistema externo");

        mockMvc.perform(post("/api/pagamentos/callback")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(callback)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Deve retornar status 200 e mensagem de funcionamento ao acessar o endpoint de health check do microsserviço de pagamento")
    void healthCheckDeveRetornarOk() throws Exception {
        mockMvc.perform(get("/api/pagamentos/health"))
                .andExpect(status().isOk())
                .andExpect(content().string("Microsserviço de Pagamento está funcionando"));
    
    }

    @Test
    @DisplayName("Deve listar pagamentos por pedido, retornando status 200 e todos os pagamentos do pedido informado")
    void deveListarPagamentosPorPedido() throws Exception {
        Long pedidoId = 123L;
        Pagamento pagamento1 = new Pagamento(pedidoId, 201L, "1111222233334444", new java.math.BigDecimal("50.00"));
        Pagamento pagamento2 = new Pagamento(pedidoId, 202L, "5555666677778888", new java.math.BigDecimal("75.00"));
        pagamentoGateway.salvar(pagamento1);
        pagamentoGateway.salvar(pagamento2);

        mockMvc.perform(get("/api/pagamentos/pedido/{pedidoId}", pedidoId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].pedidoId").value(pedidoId))
                .andExpect(jsonPath("$[1].pedidoId").value(pedidoId));
    }

    @Test
    @DisplayName("Deve listar pagamentos por cliente, retornando status 200 e todos os pagamentos do cliente informado")
    void deveListarPagamentosPorCliente() throws Exception {
        Long clienteId = 555L;
        Pagamento pagamento1 = new Pagamento(10L, clienteId, "1111222233334444", new java.math.BigDecimal("80.00"));
        Pagamento pagamento2 = new Pagamento(20L, clienteId, "5555666677778888", new java.math.BigDecimal("120.00"));
        pagamentoGateway.salvar(pagamento1);
        pagamentoGateway.salvar(pagamento2);

        mockMvc.perform(get("/api/pagamentos/cliente/{clienteId}", clienteId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].clienteId").value(clienteId))
                .andExpect(jsonPath("$[1].clienteId").value(clienteId));
    }

    @Test
    @DisplayName("Deve listar pagamentos por status, retornando status 200 e todos os pagamentos com o status informado")
    void deveListarPagamentosPorStatus() throws Exception {
        Pagamento pagamento1 = new Pagamento(1L, 100L, "1111222233334444", new java.math.BigDecimal("50.00"));
        pagamento1.iniciarProcessamento();
        pagamento1.aprovar("TXN1");
        pagamentoGateway.salvar(pagamento1);

        Pagamento pagamento2 = new Pagamento(2L, 101L, "5555666677778888", new java.math.BigDecimal("75.00"));
        pagamento2.iniciarProcessamento();
        pagamento2.aprovar("TXN2");
        pagamentoGateway.salvar(pagamento2);

        mockMvc.perform(get("/api/pagamentos/status/{status}", StatusPagamento.APROVADO.name()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].status").value(StatusPagamento.APROVADO.name()))
                .andExpect(jsonPath("$[1].status").value(StatusPagamento.APROVADO.name()));
    }
}



