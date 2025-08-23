create table if not exists urls (
    short_code varchar(8) not null primary key,
    long_url text not null,
    created_at timestamptz not null default now(),
    updated_at timestamptz not null default now()
);

create or replace function set_updated_at()
returns trigger as $$
begin
    new.updated_at = now();
    return new;
end;
$$ language plpgsql;

create trigger trg_set_updated_at
    before update on urls
    for each row
    execute function set_updated_at();