CREATE TABLE clientes (
      id BIGINT PRIMARY KEY AUTO_INCREMENT,
      nome VARCHAR(255) NOT NULL,
      cpf VARCHAR(14) NOT NULL UNIQUE,
      data_nascimento DATE,
      endereco_logradouro VARCHAR(255),
      endereco_numero VARCHAR(255),
      endereco_complemento VARCHAR(255),
      endereco_bairro VARCHAR(255),
      endereco_cidade VARCHAR(255),
      endereco_estado VARCHAR(255),
      endereco_cep VARCHAR(10)
);