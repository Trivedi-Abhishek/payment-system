package com.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentInitiatedEvent {

    private Long paymentId;
    private Long amount;
    private String currency;
    private Long merchantId;
}
