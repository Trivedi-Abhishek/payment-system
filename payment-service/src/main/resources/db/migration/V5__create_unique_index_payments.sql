create unique index payments_merchant_id_idempotent_key_key on payments(merchant_id, idempotent_key);

alter table payments alter column merchant_id drop default;