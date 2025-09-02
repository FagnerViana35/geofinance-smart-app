# geofinance-smart-app

Sistema Quarkus para gerenciar uma watchlist de ativos e integrar dados urbanos do IBGE. Este README técnico documenta o problema de negócio, a arquitetura (com UML simplificado), algoritmos centrais, decisões e trade-offs, como executar localmente e exemplos reais de uso.

Sumário
- Problema de negócio resolvido
- Arquitetura e UML
- Algoritmos e lógica de negócio
- Decisões técnicas e trade-offs
- Como executar localmente
- Endpoints (com exemplos reais)
- Configurações úteis (OpenAPI/Swagger)

Problema de negócio resolvido
- Unificar informações financeiras de ativos com contexto geográfico brasileiro (IBGE) para apoiar decisões.
- Permitir que usuários montem uma “watchlist” de ativos com preço-alvo e anotações.
- Validar e enriquecer itens com dados de município (UF, nome) para relatórios e filtros regionais.

Arquitetura e UML
- Estilo em camadas inspirado em Clean Architecture: app (adaptação/REST), domain (regras), infra (persistência/integrações), cross (mapeadores/utilitários).
- Principais componentes e relações (UML simplificado):
  - WatchlistResource -> WatchlistService -> WatchUseCase -> WatchRepository -> WatchlistEntity
  - CitiesResource -> CitiesService -> IbgeGateway -> IbgeClient (REST)
  - AssertPerformersUseCase -> BrapiGateway (REST)
  - MapperWatch/MapperAssetPerformance convertem entre DTOs e entidades/modelos externos

Algoritmos e lógica de negócio
- Ranking de ativos (AssertPerformersUseCaseImpl):
  - Entrada: symbols (opcional), period (opcional), size, flags (riskAdjusted/includeDividends - reservadas para evolução).
  - Sem symbols: busca universo padrão via BrapiGateway.list(1, 200, "close", "desc").
  - Com symbols: normaliza para maiúsculas, filtra um universo ampliado list(1, 500, ...).
  - Mapeia itens BrapiQuoteItem -> AssetPerformance via MapperAssetPerformance.
  - Ordena por changePct (nulo tratado como 0) desc e limita a topN = max(1, size).
- Atualização parcial da watchlist (WatchRepository.update):
  - Constrói dinamicamente um JPQL de update somente com campos presentes (targetPrice, notes), sempre atualizando updatedAt.
  - Se nenhum campo for informado, retorna o estado atual (mapeado) sem alterar o banco.
  - Retorna Optional.empty quando id inexistente.
- Regras de validação (WatchUseCaseImpl):
  - create: payload obrigatório; se cityId presente, valida no IbgeGateway; persiste e retorna resposta enriquecida (CityInfo quando disponível).
  - list/get/update/delete: paginação, not-found, enriquecimento opcional com cidade.
- Integração IBGE (IbgeGatewayImpl):
  - findCityById: consulta REST, mapeia para CityInfo com UF (quando presente), trata erros retornando Optional.empty.
  - findCitiesByUf: normaliza UF, consulta lista e mapeia para CityInfo.

Decisões técnicas e trade-offs
- Quarkus 3.x: inicialização rápida e perfil dev produtivo; exige alinhamento de versões do plugin e BOM (já configurado no pom.xml).
- Panache (Hibernate ORM): produtividade em CRUDs e JPQL dinâmico; trade-off: acoplamento ao ecossistema Quarkus.
- Flyway para migração: previsibilidade no schema; em testes usamos H2 em memória com DDL auto (mais rápido) para isolar.
- MicroProfile Rest Client: tipifica integrações (IBGE/Brapi) com timeouts configuráveis; requer tratamento de falhas.
- Lombok: reduz boilerplate; depende de annotation processing.

Como executar localmente
- Pré‑requisitos: Java 21, Maven, PostgreSQL local (ou alterar JDBC para seu ambiente).
- Variáveis de ambiente (padrões no application.properties):
  - QUARKUS_HTTP_PORT=8080
  - DB_JDBC_URL=jdbc:postgresql://localhost:5432/postgres
  - DB_USERNAME=postgres
  - DB_PASSWORD=senha
  - DB_SCHEMA=postgres
  - FLYWAY_MIGRATE_AT_START=true
  - CONNECT_TIMEOUT=5000, READ_TIMEOUT=10000..15000
- Rodar em dev:
  - Windows: mvnw.cmd quarkus:dev
  - Linux/macOS: ./mvnw quarkus:dev
- Rodar testes:
  - mvnw.cmd -q test (Windows) | ./mvnw -q test (Unix)
  - Para classes específicas: -Dtest=br.com.org.geofinance.domain.usecase.AssertPerformersUseCaseImplTest

Endpoints (com exemplos reais)
Base path: /api

1) Watchlist
Recurso: /api/watchlist
- POST /api/watchlist
  - Body:
    {"symbol":"PETR4","cityId":3550308,"targetPrice":10.50,"notes":"n1"}
  - Resposta 201 (trecho): {"id":1,"symbol":"PETR4","city":{"id":3550308,"uf":"SP"}}
- GET /api/watchlist?page=0&size=20
  - Resposta 200: lista com itens, ex: [ {"symbol":"PETR4"}, {"symbol":"VALE3"} ]
- GET /api/watchlist/{id}
  - Resposta 200: {"id":10,"symbol":"BBAS3","city":{"uf":"DF"}}
- PUT /api/watchlist/{id}
  - Body: {"targetPrice":20.00,"notes":"upd"}
  - Resposta 200: {"id":5,"symbol":"WEGE3"}
- DELETE /api/watchlist/{id}
  - Resposta 204

2) Cidades (IBGE)
Recurso: /api/cities
- GET /api/cities/{id}
  - Ex: /api/cities/3550308 -> CityInfo { id, nome, uf }
- GET /api/cities/resolve?uf=SP&name=Sao%20Paulo
  - Ex: retorna CityInfo do município

Configurações úteis (OpenAPI/Swagger)
- OpenAPI JSON: http://localhost:8080/q/openapi
- Swagger UI: http://localhost:8080/q/swagger-ui
- Habilitadas em src/main/resources/application.properties via quarkus.smallrye-openapi e quarkus.swagger-ui.

Executar com Docker (app + Postgres)
- Pré‑requisito: Docker Desktop (Compose v2).
- Passos:
  1) Empacote o app: mvnw.cmd -q package -DskipTests (Windows) | ./mvnw -q package -DskipTests (Unix)
  2) Suba os serviços: docker compose up --build
  3) Acesse: http://localhost:8080/q/swagger-ui
- Serviços:
  - db: postgres:16-alpine
    - Credenciais: geouser/geopass, DB: geofinance
    - Volume de dados: volume nomeado db_data
  - app: imagem construída a partir de src/main/docker/Dockerfile.jvm
    - Porta 8080 exposta
    - Variáveis principais:
      - DB_JDBC_URL=jdbc:postgresql://db:5432/geofinance
      - DB_USERNAME=geouser, DB_PASSWORD=geopass, DB_SCHEMA=public
      - FLYWAY_MIGRATE_AT_START=true (migrações aplicadas automaticamente)
- Comandos úteis:
  - Parar: docker compose down
  - Limpar dados: docker compose down -v

Notas adicionais
- DTOs: pacote app/dto; Mappers: cross/mapper; Repositório: infra/db/repository; Entidade: infra/db/model.
- Gateways: domain/gateway; Casos de uso: domain/usecase. Este README cobre os principais fluxos e decisões para onboarding rápido.
