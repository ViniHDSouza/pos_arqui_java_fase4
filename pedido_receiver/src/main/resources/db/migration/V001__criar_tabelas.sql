CREATE TABLE IF NOT EXISTS pedido (
      id BIGINT AUTO_INCREMENT PRIMARY KEY,
      cliente_id BIGINT NOT NULL,
      numero_cartao VARCHAR(255),
      status VARCHAR(50),
      data_criacao DATETIME NOT NULL
);

CREATE TABLE IF NOT EXISTS itens_pedido (
       id BIGINT AUTO_INCREMENT PRIMARY KEY,
       sku VARCHAR(100) NOT NULL,
       quantidade INT NOT NULL,
       pedido_id BIGINT,
       CONSTRAINT fk_pedido FOREIGN KEY (pedido_id) REFERENCES pedido(id)
);