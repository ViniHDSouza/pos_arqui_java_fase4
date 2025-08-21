# Tech Challenge 4ª Fase - Microserviço de Pagamento

## Objetivo do Projeto

Este projeto é um microserviço de pagamento desenvolvido para o Tech Challenge da FIAP, Fase 4. O objetivo é simular e gerenciar o processamento de pagamentos, incluindo integração (mockada) com sistemas externos, consulta de status e estorno de transações, seguindo os princípios da arquitetura limpa (Clean Architecture).

## Arquitetura Limpa (Clean Architecture)

O projeto foi estruturado para garantir alta coesão, baixo acoplamento e fácil manutenção, conforme os princípios da Clean Architecture:

- **Camada de Domínio (`core/domain`)**: Contém as entidades de negócio (ex: `Pagamento`), totalmente isoladas de frameworks e detalhes de infraestrutura.
- **Casos de Uso (`core/usecase`)**: Implementa a lógica de aplicação, orquestrando as operações do domínio sem depender de detalhes externos.
- **Gateways (`core/gateways`)**: Define interfaces para comunicação com sistemas externos e persistência, permitindo fácil substituição de implementações.
- **Adapters (`adapter`)**: Implementa as interfaces dos gateways, conectando o domínio a frameworks, bancos de dados, APIs externas e controladores REST.
  - `adapter.controller`: Pontos de entrada/saída da aplicação (REST Controllers).
  - `adapter.external`: Integração com sistemas externos de pagamento (mock).
  - `adapter.persistence`: Implementação da persistência de dados.
  - `adapter.mapper`: Conversão entre entidades e DTOs.
- **Configuração (`config`)**: Configurações de beans, injeção de dependências e inicialização do Spring.

### Benefícios dessa abordagem
- O domínio não depende de frameworks, facilitando testes e evolução.
- Mudanças em frameworks, banco de dados ou integrações externas não afetam o núcleo do negócio.
- Facilidade para substituir/adaptar integrações externas e persistência.

## Estrutura de Pastas

```
src/main/java/br/com/fiap/tech_challenge_4a_fase_pagamento/
├── adapter/
│   ├── controller/         # Controllers REST
│   ├── external/           # Integração com sistemas externos (mock)
│   ├── gateway/            # Implementação dos gateways
│   ├── mapper/             # Conversores entre entidades e DTOs
│   └── persistence/        # Persistência de dados
├── config/                 # Configurações do Spring
└── core/
    ├── domain/             # Entidades de negócio
    ├── exception/          # Exceções do domínio
    ├── gateways/           # Interfaces para gateways
    └── usecase/            # Casos de uso
```

## Banco de Dados

O projeto utiliza um banco de dados relacional (ex: PostgreSQL) para persistência dos pagamentos e transações. As migrações de schema estão localizadas em `src/main/resources/db/migration` e são aplicadas automaticamente ao subir a aplicação.

- As configurações de acesso ao banco podem ser ajustadas no arquivo `application.properties`.
- O banco é inicializado automaticamente via docker-compose.

## Como Executar

### Subindo com Docker Compose

1. Certifique-se de ter Docker e Docker Compose instalados.
2. Execute o comando na raiz do projeto:
   ```
   docker-compose up -d
   ```
3. O serviço de pagamento e o banco de dados serão inicializados automaticamente.
4. O serviço estará disponível em `http://localhost:8080`.

### Execução Local (sem Docker)

1. Certifique-se de ter o Java 17+ e o Maven instalados.
2. Execute o comando:
   ```
   ./mvnw spring-boot:run
   ```
3. O serviço estará disponível em `http://localhost:8080`.

## O que é Callback?

Callback é um mecanismo em que um sistema externo faz uma requisição HTTP para um endpoint do seu sistema, notificando sobre um evento ocorrido (por exemplo, aprovação ou rejeição de pagamento). É muito utilizado em integrações com gateways de pagamento, onde o status da transação pode mudar de forma assíncrona e o sistema precisa ser informado automaticamente.

## Endpoints do Controller

### `POST /api/pagamentos`
Processa um novo pagamento.
- **Request:** JSON com dados do pagamento (valor, número do cartão, etc).
- **Response:** Dados do pagamento criado, incluindo status e ID da transação externa.
- **Caso de uso:** `ProcessarPagamentoUseCase`.

### `GET /api/pagamentos/{id}`
Consulta um pagamento pelo ID interno.
- **Request:** ID do pagamento na URL.
- **Response:** Dados do pagamento, status e informações da transação.
- **Caso de uso:** `ConsultarPagamentoUseCase`.

### `GET /api/pagamentos`
Lista todos os pagamentos cadastrados.
- **Response:** Lista de pagamentos.
- **Caso de uso:** `ConsultarPagamentoUseCase`.

### `GET /api/pagamentos/pedido/{pedidoId}`
Lista todos os pagamentos de um pedido específico.
- **Request:** ID do pedido na URL.
- **Response:** Lista de pagamentos do pedido.
- **Caso de uso:** `ConsultarPagamentoUseCase`.

### `GET /api/pagamentos/cliente/{clienteId}`
Lista todos os pagamentos de um cliente específico.
- **Request:** ID do cliente na URL.
- **Response:** Lista de pagamentos do cliente.
- **Caso de uso:** `ConsultarPagamentoUseCase`.

### `GET /api/pagamentos/status/{status}`
Lista todos os pagamentos com um determinado status.
- **Request:** Status do pagamento na URL (ex: APROVADO, REJEITADO).
- **Response:** Lista de pagamentos com o status informado.
- **Caso de uso:** `ConsultarPagamentoUseCase`.

### `POST /api/pagamentos/{id}/estorno`
Solicita o estorno de um pagamento aprovado.
- **Request:** ID do pagamento na URL.
- **Response:** Dados do pagamento estornado.
- **Caso de uso:** `EstornarPagamentoUseCase`.

### `POST /api/pagamentos/callback2`
Recebe callback de atualização de status de pagamento (simulação de integração externa).
- **Request:** JSON contendo o `transacaoExternaId` e o novo status do pagamento.
- **Response:** 200 OK se o pagamento for encontrado, 404 se não existir, 500 em caso de erro.
- **Caso de uso:** `ConsultarPagamentoUseCase`.
- **Observação:** Apenas verifica se o pagamento existe, não atualiza o status.

### `POST /api/pagamentos/callback`
Recebe callback e atualiza o status do pagamento de acordo com o status informado.
- **Request:** JSON contendo o `transacaoExternaId`, `status` e `motivo`.
- **Response:** 200 OK se o pagamento for atualizado, 404 se não existir.
- **Caso de uso:** `AtualizarPagamentoUseCase`.
- **Observação:** Este endpoint efetivamente altera o status do pagamento no sistema.

### `GET /api/pagamentos/health`
Endpoint de health check.
- **Response:** String indicando que o serviço está funcionando.

---

Esses endpoints cobrem todas as operações de consulta, criação, estorno e atualização de status de pagamentos, além de simular callbacks externos, seguindo os princípios da arquitetura limpa.

Projeto desenvolvido para fins acadêmicos, seguindo as melhores práticas de arquitetura de software.