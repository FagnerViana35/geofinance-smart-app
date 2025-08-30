# geofinance-smart-app
Sistema Quarkus que integra dados financeiros e urbanos do Brasil para gerar insights acionáveis de investimento. Combina séries históricas de ativos com indicadores municipais para apoiar decisões contextualizadas.
Sumário
- Visão Geral
- Problema de Negócio
- O que o sistema faz
- APIs externas escolhidas
- Endpoints
    - CRUD (POST, GET, PUT, DELETE)
    - 2 Endpoints de Insights
    - Exemplos de uso (curl)

- Arquitetura e Padrões
    - Clean Architecture (justificativa)
    - Organização proposta de pastas
    - Decisões técnicas e trade-offs

- Banco de Dados e Migrações
- Como executar localmente
- Estratégia de Testes e Qualidade
- Roadmap

Visão Geral O projeto propõe uma “Central de Investimentos Inteligente com Contexto Local”, cruzando:
- dados financeiros (cotações e séries históricas) e
- dados urbanos do IBGE (informações de municípios). Objetivo: ir além de “buscar e armazenar”, processando os dados para entregar insights simples, claros e úteis para tomada de decisão.

Problema de Negócio Investidores iniciantes têm dificuldade de interpretar séries históricas e volatilidade, e raramente consideram contexto local. O sistema agrega e processa dados para:
- detectar tendências (médias móveis) e
- estimar risco (volatilidade histórica).

O que o sistema faz
- Integra uma API financeira para obter séries históricas de um ativo (símbolo).
- Integra a API do IBGE para coletar dados básicos do município (população, UF) usados no cadastro e contexto do usuário/ativo.
- Oferece um CRUD de “watchlist” (ativos monitorados).
- Expõe 2 endpoints de insights com cálculo de tendências e risco.

APIs externas escolhidas
- Financeira: Alpha Vantage
    - Uso: Time Series (Daily/Intraday) para calcular médias móveis, retornos e volatilidade.
    - Autenticação: via API key (env).

- Urbana (Brasil): IBGE API
    - Uso: buscar municípios por código/UF e coletar dados básicos de população/localidade.
    - Sem autenticação.

Endpoints Base path sugerido: /api
CRUD de Watchlist Recurso: /watchlist
- POST /watchlist
    - Cria um item monitorado.
    - Body (JSON): { "symbol": "PETR4.SA", "cityId": 3550308, "targetPrice": 42.5, "notes": "Acompanhar resultado trimestral" }

- GET /watchlist
    - Lista todos os itens.

- GET /watchlist/{id}
    - Recupera item por id.

- PUT /watchlist/{id}
    - Atualiza campos do item.
    - Body (JSON): { "targetPrice": 44.0, "notes": "Ajuste após guidance" }

- DELETE /watchlist/{id}
    - Remove item por id.

2 Endpoints de Insights
1. Tendência do Ativo (médias móveis)

- GET /insights/asset/{symbol}/trend?shortWindow=7&longWindow=21
- O que faz:
    - Busca série histórica do Alpha Vantage.
    - Calcula médias móveis simples (curta e longa).
    - Retorna sinal: bullish (cruzamento para cima), bearish (cruzamento para baixo) ou sideways.

- Resposta (exemplo): { "symbol": "PETR4.SA", "shortWindow": 7, "longWindow": 21, "signal": "bullish", "shortMA": 41.23, "longMA": 39.87, "recentChangePct7d": 3.42 }

1. Risco do Ativo (volatilidade histórica e stop sugerido)

- GET /insights/asset/{symbol}/volatility?window=30
- O que faz:
    - Calcula retornos logarítmicos.
    - Desvio-padrão (volatilidade) e classificação de risco: baixa, média, alta.
    - Sugere um stop-loss percentual baseado na volatilidade.

- Resposta (exemplo): { "symbol": "PETR4.SA", "window": 30, "volatility": 0.215, "riskLevel": "alta", "suggestedStopLossPct": 6.5 }

Exemplos de uso (curl)
- Criar item na watchlist: curl -X POST [http://localhost:8080/api/watchlist](http://localhost:8080/api/watchlist)
  -H "Content-Type: application/json"
  -d '{"symbol":"PETR4.SA","cityId":3550308,"targetPrice":42.5,"notes":"Acompanhar resultado trimestral"}'
- Consultar tendência: curl "[http://localhost:8080/api/insights/asset/PETR4.SA/trend?shortWindow=7&longWindow=21](http://localhost:8080/api/insights/asset/PETR4.SA/trend?shortWindow=7&longWindow=21)"
- Consultar risco (volatilidade): curl "[http://localhost:8080/api/insights/asset/PETR4.SA/volatility?window=30](http://localhost:8080/api/insights/asset/PETR4.SA/volatility?window=30)"

Arquitetura e Padrões Clean Architecture (justificativa)
- Independência de framework: núcleo de negócio (domínio e casos de uso) não conhece Quarkus. Facilita testes e manutenção.
- Regras de negócio primeiro: cálculos de médias móveis e volatilidade ficam em serviços de domínio e interatores de aplicação.
- Portas e Adaptadores: integrações com Alpha Vantage e IBGE são interfaces no domínio e implementações em adapters (inversão de dependência).
- Benefícios: troca fácil de fornecedores, testes offline e isolamento da infraestrutura.

Organização proposta de pastas
- br/com/org/geofinance/
    - domain/
        - model/ (WatchItem, AssetMetrics, CityInfo, etc.)
        - service/ (TrendCalculator, VolatilityService)
        - repository/ (WatchlistRepository)
        - exception/ (opcional)

    - application/
        - usecase/ (AddWatchItem, UpdateWatchItem, DeleteWatchItem, GetWatchItem, ListWatchItems, GetTrend, GetVolatility)
        - dto/, mapper/, port/

    - adapters/
        - inbound/rest/ (recursos REST)
        - outbound/persistence/ (JPA/Panache, entidades JPA e mappers)
        - outbound/restclient/ (clients Alpha Vantage e IBGE)

    - config/ (ConfigMapping, Producers)
    - common/ (tipos utilitários, Result, errors)

- resources/
    - db/migration/ (scripts Flyway)
    - application.properties

- test/
    - domain/, application/, adapters/ (unit e integração)

Decisões técnicas e trade-offs
- Quarkus imperativo pela simplicidade na prova técnica.
- Alpha Vantage pela qualidade das séries históricas; IBGE por dados municipais abertos.
- Panache/Hibernate para rapidez de desenvolvimento; mapeamentos explícitos evitam vazar JPA ao domínio.
- Flyway para versionar o schema. Em base pré-existente, usar baseline antes da primeira migração.
- Trade-off: cálculos em tempo de requisição simplificam a arquitetura, mas podem aumentar latência; cache é plano futuro.

Banco de Dados e Migrações
- Tabela: watchlist (id, symbol, city_id, target_price, notes, created_at, updated_at).
- Migrações Flyway em resources/db/migration, ex.: V1__init_watchlist.sql.

Como executar localmente Pré-requisitos
- Java 21, Maven, PostgreSQL local
- Chave da Alpha Vantage (ALPHA_VANTAGE_API_KEY)

Variáveis de ambiente (exemplo)
- QUARKUS_HTTP_PORT=8080
- DB_JDBC_URL=jdbc:postgresql://localhost:5432/postgres
- DB_USERNAME=postgres
- DB_PASSWORD=senha
- DB_SCHEMA=public
- FLYWAY_MIGRATE_AT_START=true
- ALPHA_VANTAGE_API_KEY=seu_token

Rodando em dev
- ./mvnw quarkus:dev
- OpenAPI: [http://localhost:8080/q/openapi](http://localhost:8080/q/openapi)
- Swagger UI: [http://localhost:8080/q/swagger-ui](http://localhost:8080/q/swagger-ui)

Build
- Empacotar: ./mvnw package
- Uber-jar: ./mvnw package -Dquarkus.package.jar.type=uber-jar
- Nativo: ./mvnw package -Dnative

Estratégia de Testes e Qualidade
- Unitários (alvo > 80% cobertura)
    - Domínio: médias móveis, retornos log, volatilidade.
    - Casos de uso: orquestração, validações.

- Integração
    - REST com mocks dos casos de uso.
    - Persistence com Testcontainers/Dev Services.
    - Rest Clients com WireMock/MockWebServer.

- Práticas
    - TDD para serviços de cálculo; BDD opcional.
    - OpenAPI com exemplos de payloads.
    - Clean Code, SOLID, padrões (Strategy para cálculo, Mapper, Repository).

Roadmap
- Cache de curto prazo para séries (ex.: 5 min).
- Indicadores adicionais: RSI, MACD.
- Enriquecimento urbano (população histórica, renda, escolaridade).
- Jobs para pré-processamento de métricas.
- Autenticação (JWT) e limites de taxa por usuário.

Observações finais
- Este README documenta o projeto geofinance-smart-app para a prova técnica, justificando a Clean Architecture e descrevendo os endpoints (CRUD + 2 insights). Ajustes finos de payloads e respostas podem ser feitos durante a implementação. Meu nome é AI Assistant.
