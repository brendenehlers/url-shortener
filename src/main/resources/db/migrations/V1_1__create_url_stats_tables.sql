create table if not exists url_stats (
    short_code varchar(8) not null primary key,
    hits int not null,
    last_hit timestamptz,
    constraint fk_url_stats_url foreign key (short_code) references urls(short_code)
);