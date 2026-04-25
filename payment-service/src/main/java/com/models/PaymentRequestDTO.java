package com.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Data;
import lombok.NonNull;

@Data
public class PaymentRequestDTO {

    @JsonProperty("amount_details")
    private AmountDetails amountDetails;

    @JsonProperty("reason")
    private String reason;
}
