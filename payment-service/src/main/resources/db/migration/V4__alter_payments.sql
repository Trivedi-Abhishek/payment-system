alter table payments add column merchant_id bigint default 1 not null;

alter table payments drop constraint payments_idempotent_key_key;