package com.paymentservice.models;

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

    public PaymentInitiatedEvent(CreatePaymentResponseDTO createPaymentResponseDTO) {
        this.paymentId= createPaymentResponseDTO.getPaymentId();
        this.merchantId=createPaymentResponseDTO.getMerchantId();
        this.amountDetails=createPaymentResponseDTO.getAmountDetails();
        this.userId= createPaymentResponseDTO.getUserId();
    }
}
