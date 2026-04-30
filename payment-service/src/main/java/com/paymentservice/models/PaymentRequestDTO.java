package com.paymentservice.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PaymentRequestDTO {

    @JsonProperty("amount_details")
    private AmountDetails amountDetails;

    @JsonProperty("user_id")
    private String userId;

    @JsonProperty("reason")
    private String reason;
}
