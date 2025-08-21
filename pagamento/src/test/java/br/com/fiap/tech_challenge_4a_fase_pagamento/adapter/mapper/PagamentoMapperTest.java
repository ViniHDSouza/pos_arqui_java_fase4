package br.com.fiap.tech_challenge_4a_fase_pagamento.adapter.mapper;

import br.com.fiap.tech_challenge_4a_fase_pagamento.adapter.persistence.entity.PagamentoEntity;
import br.com.fiap.tech_challenge_4a_fase_pagamento.core.domain.Pagamento;
import br.com.fiap.tech_challenge_4a_fase_pagamento.core.domain.StatusPagamento;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes unitários para PagamentoMapper")
class PagamentoMapperTest {
    private final PagamentoMapper mapper = new PagamentoMapper();

    @Test
    @DisplayName("Deve mapear Pagamento (domain) para PagamentoEntity corretamente")
    void deveMapearDomainParaEntity() {
        LocalDateTime agora = LocalDateTime.now();
        Pagamento domain = Pagamento.builder()
                .id(1L)
                .pedidoId(2L)
                .clienteId(3L)
                .numeroCartao("1234-5678-9012-3456")
                .valor(new BigDecimal("100.50"))
                .status(StatusPagamento.APROVADO)
                .transacaoExternaId("tx-999")
                .dataCriacao(agora)
                .dataAtualizacao(agora)
                .build();

        PagamentoEntity entity = mapper.toEntity(domain);
        assertNotNull(entity);
        assertEquals(domain.getId(), entity.getId());
        assertEquals(domain.getPedidoId(), entity.getPedidoId());
        assertEquals(domain.getClienteId(), entity.getClienteId());
        assertEquals(domain.getNumeroCartao(), entity.getNumeroCartao());
        assertEquals(domain.getValor(), entity.getValor());
        assertEquals(domain.getStatus(), entity.getStatus());
        assertEquals(domain.getTransacaoExternaId(), entity.getTransacaoExternaId());
        assertEquals(domain.getDataCriacao(), entity.getDataCriacao());
        assertEquals(domain.getDataAtualizacao(), entity.getDataAtualizacao());
    }

    @Test
    @DisplayName("Deve mapear PagamentoEntity para Pagamento (domain) corretamente")
    void deveMapearEntityParaDomain() {
        LocalDateTime agora = LocalDateTime.now();
        PagamentoEntity entity = PagamentoEntity.builder()
                .id(10L)
                .pedidoId(20L)
                .clienteId(30L)
                .numeroCartao("9999-8888-7777-6666")
                .valor(new BigDecimal("250.00"))
                .status(StatusPagamento.REJEITADO)
                .transacaoExternaId("tx-abc")
                .motivoRejeicao("Cartão bloqueado")
                .dataCriacao(agora)
                .dataAtualizacao(agora)
                .build();

        Pagamento domain = mapper.toDomain(entity);
        assertNotNull(domain);
        assertEquals(entity.getId(), domain.getId());
        assertEquals(entity.getPedidoId(), domain.getPedidoId());
        assertEquals(entity.getClienteId(), domain.getClienteId());
        assertEquals(entity.getNumeroCartao(), domain.getNumeroCartao());
        assertEquals(entity.getValor(), domain.getValor());
        assertEquals(entity.getStatus(), domain.getStatus());
        assertEquals(entity.getTransacaoExternaId(), domain.getTransacaoExternaId());
        assertEquals(entity.getMotivoRejeicao(), domain.getMotivoRejeicao());
        assertEquals(entity.getDataCriacao(), domain.getDataCriacao());
        assertEquals(entity.getDataAtualizacao(), domain.getDataAtualizacao());
    }

    @Test
    @DisplayName("Deve retornar null ao mapear domain/entity nulo")
    void deveRetornarNullParaEntradasNulas() {
        assertNull(mapper.toEntity(null));
        assertNull(mapper.toDomain(null));
    }
}
