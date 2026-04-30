package com.ledgerservice.models;

import com.ledgerservice.enums.TxnTypeEnum;

public interface LedgerEntryAmountProjections {

    TxnTypeEnum getTxnType();
    Long getAmount();
}
