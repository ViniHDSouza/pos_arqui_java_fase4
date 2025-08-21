package br.com.fiap.tech_challenge_4a_fase_pagamento.config;



import br.com.fiap.tech_challenge_4a_fase_pagamento.adapter.external.SistemaExternoPagamentoMock;
import br.com.fiap.tech_challenge_4a_fase_pagamento.adapter.gateway.RepositorioDePagamentoJPAGatewayImpl;
import br.com.fiap.tech_challenge_4a_fase_pagamento.adapter.persistence.repository.PagamentoJpaRepository;
import br.com.fiap.tech_challenge_4a_fase_pagamento.core.gateways.PagamentoGateway;
import br.com.fiap.tech_challenge_4a_fase_pagamento.core.gateways.SistemaExternoPagamento;
import br.com.fiap.tech_challenge_4a_fase_pagamento.core.usecase.pagamento.AtualizarPagamentoUseCase;
import br.com.fiap.tech_challenge_4a_fase_pagamento.core.usecase.pagamento.ConsultarPagamentoUseCase;
import br.com.fiap.tech_challenge_4a_fase_pagamento.core.usecase.pagamento.EstornarPagamentoUseCase;
import br.com.fiap.tech_challenge_4a_fase_pagamento.core.usecase.pagamento.ProcessarPagamentoUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PagamentoConfig {

    @Bean
    public PagamentoGateway pagamentoGateway(PagamentoJpaRepository jpaRepository) {
        return new RepositorioDePagamentoJPAGatewayImpl(jpaRepository);
    }

    @Bean
    public SistemaExternoPagamento sistemaExternoPagamento() {
        return new SistemaExternoPagamentoMock();
    }

    @Bean
    public ProcessarPagamentoUseCase processarPagamentoUseCase(PagamentoGateway pagamentoGateway, SistemaExternoPagamento sistemaExternoPagamento) {
        return new ProcessarPagamentoUseCase(pagamentoGateway, sistemaExternoPagamento);
    }

    @Bean
    public ConsultarPagamentoUseCase consultarPagamentoUseCase(PagamentoGateway pagamentoGateway) {
        return new ConsultarPagamentoUseCase(pagamentoGateway);
    }

    @Bean
    public EstornarPagamentoUseCase estornarPagamentoUseCase(PagamentoGateway pagamentoGateway, SistemaExternoPagamento sistemaExternoPagamento) {
        return new EstornarPagamentoUseCase(pagamentoGateway, sistemaExternoPagamento);
    }

    @Bean
    public AtualizarPagamentoUseCase atualizaPagamentoUseCase(PagamentoGateway pagamentoGateway) {
        return new AtualizarPagamentoUseCase(pagamentoGateway);
    }
}