package br.com.fiap.tech_challenge_4a_fase_pagamento;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TechChallenge4aFasePagamentoApplication {

	private static final Logger log = LoggerFactory.getLogger(TechChallenge4aFasePagamentoApplication.class);

	public static void main(String[] args) {
		log.info("=== Iniciando Microsserviço de Pagamento ===");
		log.info("Tech Challenge Fase 4 - Arquitetura Limpa");
		log.info("Porta: 8083");
		log.info("Banco: MySQL");
		log.info("==========================================");

		SpringApplication.run(TechChallenge4aFasePagamentoApplication.class, args);

		log.info("Microsserviço de Pagamento iniciado com sucesso!");
	}

}
