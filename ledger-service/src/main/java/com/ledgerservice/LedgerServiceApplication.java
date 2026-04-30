package com.ledgerservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LedgerServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(LedgerServiceApplication.class, args);
    }

}

// ledger-consumer service which listens to VALID payments
// check for imbalances if no imbalance insert in ledger table
// if imbalance update payments entry with valid reason
