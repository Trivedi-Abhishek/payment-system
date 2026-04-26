package com.entity;

import com.enums.CurrencyCodeEnum;
import com.enums.TransactionStatusEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Entity
@Table(name = "payments")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="merchant_id", nullable=false)
    private Long merchantId;

    @Column(name="user_id", nullable=false)
    private Long userId;

    @Column(name = "amount", nullable = false)
    private Long amount;

    @Column(name = "currency_code", nullable = false)
    @Enumerated(EnumType.STRING)
    private CurrencyCodeEnum currencyCode=CurrencyCodeEnum.INR;

    @Column(name = "transaction_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private TransactionStatusEnum transactionStatus=TransactionStatusEnum.INITIATED;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Date createdAt;

    @Column(name = "updated_at")
    private Date updatedAt;

    @Column(name = "idempotent_key", nullable = false, unique = true)
    private String idempotentKey;

    @Version// for optimistic locking
    @Column(name = "version")
    private Integer version;

    @Column(name = "reason")
    private String reason;
}
