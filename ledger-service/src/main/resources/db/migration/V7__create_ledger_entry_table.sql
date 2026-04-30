create table ledger_entry(

    id bigint PRIMARY KEY,
    account_id bigint not null,
    payment_id bigint not null,
    amount bigint not null,
    currency_code varchar(10) not null,
    txn_type VARCHAR(255) not null,
    createdAt timestamp not null
);