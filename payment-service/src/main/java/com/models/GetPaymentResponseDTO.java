package com.models;

import com.enums.TransactionStatusEnum;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;

@Data
public class GetPaymentResponseDTO {

    @JsonProperty("payment_id")
    private Long paymentId;

    @JsonProperty("amount_details")
    private AmountDetails amountDetails;

    @JsonProperty("reason")
    private String reason;

    @JsonProperty("transaction_status")
    private TransactionStatusEnum transactionStatus;

    @JsonProperty("created_at")
    private Date createdAt;
}
