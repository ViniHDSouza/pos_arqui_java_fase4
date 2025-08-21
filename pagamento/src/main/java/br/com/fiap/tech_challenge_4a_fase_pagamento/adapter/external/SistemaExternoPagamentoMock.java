package br.com.fiap.tech_challenge_4a_fase_pagamento.adapter.external;

import br.com.fiap.tech_challenge_4a_fase_pagamento.adapter.exception.TransacaoNaoEncontradaException;
import br.com.fiap.tech_challenge_4a_fase_pagamento.adapter.external.dto.TransacaoMock;
import br.com.fiap.tech_challenge_4a_fase_pagamento.core.domain.Pagamento;
import br.com.fiap.tech_challenge_4a_fase_pagamento.core.domain.ResultadoConsultaPagamento;
import br.com.fiap.tech_challenge_4a_fase_pagamento.core.gateways.SistemaExternoPagamento;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

@Component
public class SistemaExternoPagamentoMock implements SistemaExternoPagamento {

    private static final Logger log = LoggerFactory.getLogger(SistemaExternoPagamentoMock.class);

    private final Random random = new Random();
    private final Map<String, TransacaoMock> transacoes = new HashMap<>();

    @Override
    public String processarPagamento(Pagamento pagamento) {

        String transacaoId = "TXN_" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        boolean aprovado = determinarAprovacao(pagamento);
        String status = aprovado ? "APROVADO" : "REJEITADO";
        String motivo = aprovado ? "Pagamento aprovado com sucesso" : determinarMotivoRejeicao(pagamento);


        TransacaoMock transacao = new TransacaoMock(transacaoId, status, motivo, aprovado, pagamento.getValor());
        transacoes.put(transacaoId, transacao);

        log.info("Sistema Externo Mock - Processando pagamento:");
        log.info("  Transação ID: {}", transacaoId);
        log.info("  Valor: R$ {}", pagamento.getValor());
        log.info("  Status: {}", status);
        log.info("  Motivo: {}", motivo);

        return transacaoId;
    }

    @Override
    public ResultadoConsultaPagamento consultarStatusPagamento(String transacaoExternaId) {

        TransacaoMock transacao = transacoes.get(transacaoExternaId);

        if (transacao == null) {
            log.warn("Sistema Externo Mock - Transação não encontrada: {}", transacaoExternaId);
            return new ResultadoConsultaPagamento("NAO_ENCONTRADO", "Transação não encontrada", false);
        }

        log.info("Sistema Externo Mock - Consultando transação: {}", transacaoExternaId);
        log.info("  Status: {}", transacao.getStatus());
        log.info("  Motivo: {}", transacao.getMotivo());

        return new ResultadoConsultaPagamento(transacao.getStatus(), transacao.getMotivo(), transacao.isAprovado());
    }

    @Override
    public boolean estornarPagamento(String transacaoExternaId) {

        TransacaoMock transacao = transacoes.get(transacaoExternaId);

        if (transacao == null) {
            throw new TransacaoNaoEncontradaException("Sistema Externo Mock - Estorno falhou: Transação não encontrada " + transacaoExternaId);
        }

        if (!transacao.isAprovado()) {
            throw new TransacaoNaoEncontradaException("Sistema Externo Mock - Estorno falhou: Transação não foi aprovada " + transacaoExternaId);
        }

        if (transacao.isEstornado()) {
            throw new TransacaoNaoEncontradaException("Sistema Externo Mock - Estorno falhou: Transação já foi estornada " + transacaoExternaId);
        }

        if (random.nextInt(100) < 5) {
            throw new TransacaoNaoEncontradaException("Sistema Externo Mock - Estorno falhou: Erro simulado no sistema externo " + transacaoExternaId);
        }

        transacao.setEstornado(true);
        transacao.setStatus("ESTORNADO");
        transacao.setMotivo("Pagamento estornado com sucesso");

        log.info("Sistema Externo Mock - Estorno realizado com sucesso:");
        log.info("  Transação ID: {}", transacaoExternaId);
        log.info("  Valor estornado: R$ {}", transacao.getValor());

        return true;
    }

    private boolean determinarAprovacao(Pagamento pagamento) {
    
        if (pagamento.getNumeroCartao().endsWith("0000")) {
            return false;

        }
        if (pagamento.getValor().compareTo(new BigDecimal("10000")) > 0) {
            return random.nextInt(100) >= 30;
        }

        if (pagamento.getValor().compareTo(new BigDecimal("5000")) > 0) {
            return random.nextInt(100) >= 15;

        }
        return random.nextInt(100) >= 5;
    }

    private String determinarMotivoRejeicao(Pagamento pagamento) {
        if (pagamento.getNumeroCartao().endsWith("0000")) {
            return "Cartão de teste não é válido";
        }

        if (pagamento.getValor().compareTo(new BigDecimal("10000")) > 0) {
            return "Valor excede limite para transações online";
        }

        String[] motivos = {
                "Saldo insuficiente",
                "Cartão bloqueado",
                "Dados do cartão inválidos",
                "Transação não autorizada pelo banco",
                "Limite de crédito excedido"
        };

        return motivos[random.nextInt(motivos.length)];
    }

}