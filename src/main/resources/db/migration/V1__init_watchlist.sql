
create table if not exists watchlist (
    id bigserial primary key,
    symbol varchar(50) not null,
    city_id integer,
    target_price numeric(19,2) not null,
    notes varchar(500),
    created_at timestamptz not null,
    updated_at timestamptz not null
);

INSERT INTO watchlist (
    symbol,
    city_id,
    target_price,
    notes,
    created_at,
    updated_at
) VALUES
      ('RZDL11', 3548906, 350.00, 'Riza Delphi Fundo De Investimento em Participacoes Em Infraestrutura', NOW(), NOW()),
      ('LMTB34', 3503208, 400.00, 'Lockheed Martin Corporation Shs Unsponsored Brazilian Depository Receipts Repr 1 Sh', NOW(), NOW()),
      ('PQDP11', 3543402, 150.00, 'Fundo de Investimento Imobiliario FII Parque Dom Pedro Shopping Center Responsabilidade Limitada', NOW(), NOW()),
      ('GEOO34', 3542909, 120.00, 'GE Aerospace Unsponsored Brazilian Depositary Receipt Repr 1 S', NOW(), NOW()),
      ('AATH11', 3550308, 230.00, 'Athon Energia ESG I Fundo de Investimento em Participacoes em Infraestrutura', NOW(), NOW());

