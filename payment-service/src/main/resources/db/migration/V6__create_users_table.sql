create table users (
    id bigint PRIMARY KEY,
    name varchar(255) not null,
    phone_number varchar(255) not null,
    address varchar(255) not null,
    country_code varchar(5) not null,
    created_at TIMESTAMP NOT NULL,
    is_active boolean default true not null
);

alter table payments
add column user_id bigint default 1 not null;