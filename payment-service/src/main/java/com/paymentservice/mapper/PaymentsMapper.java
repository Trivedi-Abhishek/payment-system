package com.paymentservice.mapper;

import com.paymentservice.entity.Payment;
import com.paymentservice.models.AmountDetails;
import com.paymentservice.models.GetPaymentResponseDTO;

public class PaymentsMapper {

    public static GetPaymentResponseDTO mapPaymentToGetPaymentResponseDTO(Payment payment) {

        GetPaymentResponseDTO getPaymentResponseDTO=new GetPaymentResponseDTO();
        getPaymentResponseDTO.setPaymentId(payment.getId());
        getPaymentResponseDTO.setMerchantId(payment.getMerchantId());
        getPaymentResponseDTO.setUserId(payment.getUserId());
        getPaymentResponseDTO.setAmountDetails(new AmountDetails(payment.getAmount(), payment.getCurrencyCode()));
        getPaymentResponseDTO.setTransactionStatus(payment.getTransactionStatus());
        getPaymentResponseDTO.setReason(payment.getReason());
        getPaymentResponseDTO.setCreatedAt(payment.getCreatedAt());

        return getPaymentResponseDTO;
    }
}
