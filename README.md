# geofinance-smart-app

Sistema Quarkus para gerenciar uma watchlist de ativos e integrar dados urbanos do IBGE. Esta documentação explica, de forma clara, o que cada endpoint faz e descreve a estrutura do código do projeto.

Sumário
- Visão Geral
- Estrutura do Código (arquitetura)
- Endpoints (com exemplos)
- Como executar
- Configurações úteis (OpenAPI/Swagger)

Visão Geral
- CRUD de watchlist: permite criar, listar, buscar por id, atualizar e remover itens monitorados.
- Integração IBGE: valida um município pelo código (id) e resolve o código a partir de UF + nome do município.

Estrutura do Código
A organização segue uma separação por camadas, alinhada a princípios de Clean Architecture:
- app/resource: classes REST (endpoints HTTP, validação básica e documentação OpenAPI).
  - CitiesResource: expõe endpoints de cidades (IBGE).
  - WatchlistResource: expõe endpoints da watchlist.
- app/service: serviços de aplicação que orquestram casos de uso do domínio.
  - CitiesService e WatchlistService: interfaces/implementações que chamam use cases.
- domain/usecase: regras de negócio e casos de uso.
  - CitiesUseCase, WatchUseCase e suas implementações (CitiesUseCaseImpl, WatchUseCaseImpl).
- domain/gateway: portas de saída para integrações externas.
  - IbgeGateway e IbgeGatewayImpl: abstração e implementação do cliente IBGE.
- infra/restClient: cliente HTTP para IBGE usando MicroProfile Rest Client (IbgeClient).
- infra/db: persistência com JPA/Hibernate.
  - model/WatchlistEntity: entidade JPA que representa a tabela watchlist.
  - repository/WatchRepository: acesso a dados (CRUD com Panache/EntityManager).
- cross/mapper: conversores entre entidade e DTOs (MapperWatch).
- app/dto: contratos de entrada e saída (request/response) usados pelos recursos REST.
- resources/db/migration: migrações Flyway (V1__init_watchlist.sql).

Endpoints
Base path: /api

1) Watchlist
Recurso: /api/watchlist
- POST /api/watchlist
  - O que faz: cria um novo item na watchlist.
  - Body (JSON): WatchlistCreateRequest (campos como symbol, cityId, targetPrice, notes conforme o seu DTO).
  - Resposta: 201 Created com corpo WatchlistItemEnrichedResponse e Location para o novo recurso.
  - Exemplo:
    curl -X POST http://localhost:8080/api/watchlist \
      -H "Content-Type: application/json" \
      -d '{"symbol":"PETR4.SA","cityId":3550308,"targetPrice":42.5,"notes":"Acompanhar resultado trimestral"}'

- GET /api/watchlist?page=0&size=20
  - O que faz: lista itens paginados da watchlist.
  - Parâmetros query: page (default 0), size (default 20, mínimo 1).
  - Resposta: 200 OK com lista de WatchlistItemEnrichedResponse.
  - Exemplo:
    curl "http://localhost:8080/api/watchlist?page=0&size=20"

- GET /api/watchlist/{id}
  - O que faz: busca um item da watchlist pelo id.
  - Resposta: 200 OK com WatchlistItemEnrichedResponse ou 404 se não encontrado.
  - Exemplo:
    curl http://localhost:8080/api/watchlist/1

- PUT /api/watchlist/{id}
  - O que faz: atualiza campos de um item existente.
  - Body (JSON): WatchlistUpdateRequest (campos editáveis, p.ex. targetPrice, notes).
  - Resposta: 200 OK com WatchlistItemEnrichedResponse atualizado; 404 se não encontrado.
  - Exemplo:
    curl -X PUT http://localhost:8080/api/watchlist/1 \
      -H "Content-Type: application/json" \
      -d '{"targetPrice":44.0,"notes":"Ajuste após guidance"}'

- DELETE /api/watchlist/{id}
  - O que faz: remove um item da watchlist pelo id.
  - Resposta: 204 No Content.
  - Exemplo:
    curl -X DELETE http://localhost:8080/api/watchlist/1

2) Cidades (IBGE)
Recurso: /api/cities
- GET /api/cities/{id}
  - O que faz: valida se o código de município (id) existe no IBGE e retorna informações básicas.
  - Parâmetro path: id (inteiro do município no IBGE).
  - Resposta: 200 OK com CityInfo; 4xx/5xx em caso de erro.
  - Exemplo:
    curl http://localhost:8080/api/cities/3550308

- GET /api/cities/resolve?uf=SP&name=Sao%20Paulo
  - O que faz: resolve o código do município (ID IBGE) a partir da UF e do nome do município.
  - Parâmetros query: uf (sigla), name (nome do município). Ambos obrigatórios.
  - Resposta: 200 OK com CityInfo; 400 se faltar parâmetro.
  - Exemplo:
    curl "http://localhost:8080/api/cities/resolve?uf=SP&name=Sao%20Paulo"

Como executar
- Pré‑requisitos: Java 21, Maven, PostgreSQL.
- Variáveis de ambiente típicas:
  - QUARKUS_HTTP_PORT=8080
  - DB_JDBC_URL=jdbc:postgresql://localhost:5432/postgres
  - DB_USERNAME=postgres
  - DB_PASSWORD=senha
  - DB_SCHEMA=public
  - FLYWAY_MIGRATE_AT_START=true
- Rodar em dev:
  - Windows: mvnw.cmd quarkus:dev
  - Linux/macOS: ./mvnw quarkus:dev

Configurações úteis (OpenAPI/Swagger)
- OpenAPI JSON: http://localhost:8080/q/openapi
- Swagger UI: http://localhost:8080/q/swagger-ui
- Estas rotas estão habilitadas em src/main/resources/application.properties via quarkus.smallrye-openapi e quarkus.swagger-ui.

Observações
- Os DTOs exatos (WatchlistCreateRequest, WatchlistUpdateRequest, WatchlistItemEnrichedResponse, CityInfo) podem ser consultados no pacote app/dto.
- Os mapeamentos entre entidade JPA e DTOs estão em cross/mapper/MapperWatch.
- A camada de domínio concentra os casos de uso e regras; as camadas app/ e infra/ somente expõem e persistem dados.
