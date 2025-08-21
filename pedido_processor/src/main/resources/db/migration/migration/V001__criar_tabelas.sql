CREATE TABLE pedido_processor (
    id VARCHAR(36) PRIMARY KEY,
    pedido_reciver_id BIGINT,
    cliente_id BIGINT ,
    total DECIMAL(10,2),
    status VARCHAR(50),
    criado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE item_pedido_processor (
     id BIGINT AUTO_INCREMENT PRIMARY KEY,
     pedidoprocessor_id VARCHAR(36) ,
     produto_id BIGINT ,
     sku VARCHAR(100) ,
     quantidade INT ,
     preco_unitario DECIMAL(10,2) ,
     preco_total DECIMAL(10,2) ,
     FOREIGN KEY (pedidoprocessor_id) REFERENCES pedido_processor(id) ON DELETE CASCADE
);
