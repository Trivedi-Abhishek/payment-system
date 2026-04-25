package com.models;

import com.entity.Payment;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreatePaymentResponseDTO {

    @JsonProperty("payment_id")
    private Long paymentId;

    @JsonProperty("merchant_id")
    private Long merchantId;

    @JsonProperty("amount_details")
    private AmountDetails amountDetails;

    public CreatePaymentResponseDTO(Payment payment, Long merchantId) {
        this.paymentId=payment.getId();
        this.merchantId=merchantId;
        this.amountDetails= new AmountDetails(payment.getAmount(), payment.getCurrencyCode());
    }
}
