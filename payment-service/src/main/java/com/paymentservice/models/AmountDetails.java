package com.paymentservice.models;

import com.paymentservice.enums.CurrencyCodeEnum;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AmountDetails {

    @JsonProperty("amount")
    private Long amount;

    @JsonProperty("currency_code")
    private CurrencyCodeEnum currencyCode;
}
