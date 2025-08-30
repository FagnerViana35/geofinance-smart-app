
create table if not exists watchlist (
    id bigserial primary key,
    symbol varchar(50) not null,
    city_id integer not null,
    target_price numeric(19,2) not null,
    notes varchar(500),
    created_at timestamptz not null,
    updated_at timestamptz not null
);
