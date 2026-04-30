package com.ledgerservice.entity;

import com.paymentservice.enums.CurrencyCodeEnum;
import com.ledgerservice.enums.TxnTypeEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name="ledger_entry")
public class LedgerEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="payment_id", nullable=false)
    private Long paymentId;

    @Column(name="account_id", nullable=false)
    private Long accountId;

    @Column(name = "amount", nullable = false)
    private Long amount;

    @Column(name = "currency_code", nullable = false)
    @Enumerated(EnumType.STRING)
    private CurrencyCodeEnum currencyCode= CurrencyCodeEnum.INR;

    @Column(name = "txn_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private TxnTypeEnum txnType;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Date createdAt;
}
