package com.ledgerservice.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentInitiatedEvent {

    private Long paymentId;
    private AmountDetails amountDetails;
    private Long merchantId;
    private Long userId;
}
