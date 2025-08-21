CREATE TABLE pagamentos (
        id BIGINT AUTO_INCREMENT PRIMARY KEY,
        pedido_id BIGINT NOT NULL,
        cliente_id BIGINT NOT NULL,
        numero_cartao VARCHAR(50) NOT NULL,
        valor DECIMAL(12,2) NOT NULL,
        status VARCHAR(30) NOT NULL,
        transacao_externa_id VARCHAR(100),
        motivo_rejeicao VARCHAR(500),
        data_criacao DATETIME NOT NULL,
        data_atualizacao DATETIME NOT NULL
);