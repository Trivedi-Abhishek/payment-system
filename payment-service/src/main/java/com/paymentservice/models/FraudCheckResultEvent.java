package com.paymentservice.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FraudCheckResultEvent {

    private Long paymentId;
    private String decision;
    private String reason;
    private Long merchantId;
    private Long userId;
    private AmountDetails amountDetails;
}
