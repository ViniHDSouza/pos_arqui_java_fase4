# Tech Challenge 4ª Fase - Microsserviço de Cliente

Este projeto é um microsserviço para cadastro e gestão de clientes, desenvolvido como parte do Tech Challenge da FIAP (4ª fase). O projeto foi construído seguindo os princípios da Arquitetura Limpa (Clean Architecture), garantindo a separação de responsabilidades, independência de frameworks e testabilidade.

## Arquitetura do Projeto

O projeto segue a Clean Architecture, dividida em camadas concêntricas:

### Core (Centro da Aplicação)
- **Domain**: Contém as entidades de negócio (Cliente, Endereço) e regras de negócio centrais
- **Use Cases**: Implementa as regras de aplicação e orquestra o fluxo de dados
- **Ports**: Define as interfaces (ports) que permitem a comunicação com o mundo externo

### Adapters (Camada Externa)
- **Controllers**: Adaptadores da API REST
- **Repositories**: Implementações de persistência
- **DTOs**: Objetos de transferência de dados
- **Mappers**: Conversores entre DTOs e entidades de domínio

## Princípios Arquiteturais

1. **Independência de Frameworks**: O core da aplicação não depende de frameworks externos
2. **Testabilidade**: Arquitetura altamente testável devido à inversão de dependências
3. **Independência de UI**: A lógica de negócios não depende da interface (API REST)
4. **Independência de Banco de Dados**: O domínio não conhece a implementação do banco
5. **Independência de Agentes Externos**: O núcleo da aplicação é isolado de serviços externos

## Sumário

- [Tecnologias e Frameworks](#tecnologias-e-frameworks)
- [Estrutura de Pacotes](#estrutura-de-pacotes)
- [Configuração e Execução](#configuração-e-execução)
- [API REST](#api-rest)
- [Testes](#testes)
- [Configurações](#configurações)
- [Observabilidade](#observabilidade)

---

## Tecnologias e Frameworks

### Core da Aplicação
- Java 17
- Validação de CPF e CEP
- Object Value Patterns
- Domain-Driven Design concepts

-### Frameworks & Libraries
- Spring Boot 3.x
- Quarkus (módulo alternativo em `quarkus-app`)
- Spring Data JPA
- Spring Validation
- Spring HATEOAS
- Flyway (Migração de banco)

### Persistência
- MySQL (Produção)
- H2 Database (Testes)

### Testes
- JUnit 5
- Mockito
- AssertJ
- MockMvc

### DevOps & Infraestrutura
- Docker
- Docker Compose
- Maven

## Estrutura de Pacotes

```
tech-challenge-4a-fase_cliente/
├── src/
│   ├── main/
│   │   ├── java/br/com/fiap/tech_challenge_4a_fase_cliente/
│   │   │   ├── core/                           # Núcleo da aplicação (regras de negócio)
│   │   │   │   ├── domain/                     # Entidades e regras de domínio
│   │   │   │   │   ├── entities/
│   │   │   │   │   │   ├── cliente/
│   │   │   │   │   │   └── endereco/
│   │   │   │   │   └── vo/                     # Value Objects (CPF, CEP)
│   │   │   │   ├── exception/                  # Exceções de domínio
│   │   │   │   ├── gateways/                   # Interfaces de repositório
│   │   │   │   └── usecases/                   # Casos de uso da aplicação
│   │   │   │       └── cliente/                # Implementações dos casos de uso
│   │   │   ├── adapter/                        # Adaptadores externos
│   │   │   │   ├── controller/                 # Controladores REST
│   │   │   │   │   ├── request/               # DTOs de entrada
│   │   │   │   │   └── response/              # DTOs de saída
│   │   │   │   ├── exception/                  # Tratamento de exceções
│   │   │   │   ├── gateway/                    # Implementações dos gateways
│   │   │   │   ├── mapper/                     # Conversores DTO <-> Entidade
│   │   │   │   └── persistence/                # Camada de persistência
│   │   │   │       ├── entity/                 # Entidades JPA
│   │   │   │       └── repository/             # Repositórios Spring Data
│   │   │   └── TechChallenge4aFaseClienteApplication.java
│   │   └── resources/
│   │       ├── application.properties
│   │       └── db/migration/                   # Scripts Flyway
│   └── test/
│       └── java/br/com/fiap/tech_challenge_4a_fase_cliente/
│           ├── core/
│           │   └── usecases/                   # Testes unitários dos casos de uso
│           │       └── cliente/
│           └── adapter/
│               ├── controller/                 # Testes de integração da API
│               ├── exception/                  # Testes do handler de exceções
│               └── mapper/                     # Testes dos conversores
├── docker-compose.yml
├── Dockerfile
├── pom.xml
└── README.md
```

---

## Configuração e Execução

### Pré-requisitos

- Java 17+
- Maven 3.9+
- Docker e Docker Compose
- MySQL (ou Docker para rodar o MySQL)

### Executando com Docker Compose

O modo mais simples de executar a aplicação é usando Docker Compose:

```sh
# Constrói a imagem da aplicação e inicia os containers
docker-compose up --build -d

# Para verificar os logs
docker-compose logs -f
```

### Execução Manual

1. Clone o repositório:
```sh
git clone <url-do-repositorio>
cd tech-challenge-4a-fase_cliente
```

2. Inicie o MySQL:
```sh
docker-compose up -d mysql
```

3. Configure o ambiente (ver seção [Configurações](#configurações))

4. Execute a aplicação:
```sh
./mvnw clean install
./mvnw spring-boot:run
```

### Executando o módulo Quarkus

Para compilar e executar a versão em Quarkus (localizada em `quarkus-app`):

```sh
mvn -f quarkus-app/pom.xml quarkus:dev
```

5. Verifique a saúde da aplicação:
```sh
curl http://localhost:8080/actuator/health
```

## API REST

### Endpoints

| Método | Endpoint           | Use Case                  | Descrição                    |
|--------|-------------------|---------------------------|------------------------------|
| GET    | /clientes         | ListarTodosClienteUseCase | Retorna lista paginada      |
| GET    | /clientes/{id}    | BuscarClienteUseCase      | Busca por ID                |
| POST   | /clientes         | CriarClienteUseCase       | Cadastra novo cliente       |
| PUT    | /clientes/{id}    | AtualizarClienteUseCase   | Atualiza dados existentes   |
| DELETE | /clientes/{id}    | DeletarClienteUseCase     | Remove cadastro             |

### Exemplo de Requisições

#### Criar Cliente
```json
POST /clientes
{
  "nome": "João da Silva",
  "cpf": "123.456.789-10",
  "dataNascimento": "1990-01-01",
  "endereco": {
    "logradouro": "Avenida Paulista",
    "numero": "1000",
    "complemento": "Sala 123",
    "bairro": "Bela Vista",
    "cidade": "São Paulo",
    "estado": "SP",
    "cep": "01.310-100"
  }
}
```

#### Resposta de Sucesso
```json
201 Created
{
  "id": 1,
  "nome": "João da Silva",
  "cpf": "123.456.789-10",
  "dataNascimento": "1990-01-01",
  "endereco": {
    "logradouro": "Avenida Paulista",
    "numero": "1000",
    "complemento": "Sala 123",
    "bairro": "Bela Vista",
    "cidade": "São Paulo",
    "estado": "SP",
    "cep": "01.310-100"
  },
  "_links": {
    "self": { "href": "/clientes/1" },
    "atualizar": { "href": "/clientes/1" },
    "remover": { "href": "/clientes/1" }
  }
}

---

## Testes

O projeto possui uma extensa suíte de testes seguindo a mesma estrutura da Clean Architecture:

### Testes Unitários
- **Domain**: Testes das regras de negócio e validações
- **Use Cases**: Testes dos casos de uso com mocks dos ports
- **Mappers**: Testes das conversões entre DTOs e entidades

### Testes de Integração
- **Controllers**: Testes end-to-end usando MockMvc
- **Repositories**: Testes da camada de persistência
- **Exception Handling**: Testes do tratamento de erros

### Executando os Testes

```sh
# Todos os testes
./mvnw test

# Apenas testes unitários
./mvnw test -DexcludedGroups="integration"

# Apenas testes de integração
./mvnw test -Dgroups="integration"

# Relatório de cobertura (JaCoCo)
./mvnw verify
```

## Persistência de Dados

### Migrações (Flyway)

O versionamento do banco de dados é gerenciado com Flyway:

```
src/main/resources/db/migration/
├── V1__create_cliente_table.sql
├── V2__create_endereco_table.sql
└── V3__add_audit_columns.sql
```

As migrações são executadas automaticamente na inicialização.

### Modelos de Persistência

- **ClienteEntity**: Representa o cliente no banco
- **EnderecoEntity**: Representa o endereço do cliente
- JPA com mapeamento bidirecional e cascade

## Configurações

### Variáveis de Ambiente

| Variável | Descrição | Valor Padrão |
|----------|-----------|--------------|
| `SPRING_PROFILES_ACTIVE` | Perfil ativo | `default` |
| `SERVER_PORT` | Porta da aplicação | `8080` |
| `MYSQL_HOST` | Host do MySQL | `localhost` |
| `MYSQL_PORT` | Porta do MySQL | `3306` |
| `MYSQL_DATABASE` | Nome do banco | `clientes` |
| `MYSQL_USER` | Usuário MySQL | `root` |
| `MYSQL_PASSWORD` | Senha MySQL | `root` |

### application.properties

```properties
# Banco de Dados
spring.datasource.url=jdbc:mysql://${MYSQL_HOST}:${MYSQL_PORT}/${MYSQL_DATABASE}
spring.datasource.username=${MYSQL_USER}
spring.datasource.password=${MYSQL_PASSWORD}

# JPA/Hibernate
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect

# Flyway
spring.flyway.enabled=true
spring.flyway.baseline-on-migrate=true

# Actuator
management.endpoints.web.exposure.include=health,metrics,info
```

## Observabilidade

### Health Check
```sh
curl http://localhost:8080/actuator/health
```

### Métricas
```sh
curl http://localhost:8080/actuator/metrics
```

### Logs
- Utiliza SLF4J + Logback
- Logs estruturados em JSON
- Diferentes níveis por ambiente

