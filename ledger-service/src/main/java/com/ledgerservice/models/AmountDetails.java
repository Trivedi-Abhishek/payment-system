package com.ledgerservice.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ledgerservice.enums.CurrencyCodeEnum;
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
