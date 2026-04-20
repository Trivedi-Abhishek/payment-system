package com.mapper;

import com.entity.Payment;
import com.models.AmountDetails;
import com.models.GetPaymentResponseDTO;

public class PaymentsMapper {

    public static GetPaymentResponseDTO mapPaymentToGetPaymentResponseDTO(Payment payment) {

        GetPaymentResponseDTO getPaymentResponseDTO=new GetPaymentResponseDTO();
        getPaymentResponseDTO.setPaymentId(payment.getId());
        getPaymentResponseDTO.setAmountDetails(new AmountDetails(payment.getAmount(), payment.getCurrencyCode()));
        getPaymentResponseDTO.setTransactionStatus(payment.getTransactionStatus());
        getPaymentResponseDTO.setReason(payment.getReason());
        getPaymentResponseDTO.setCreatedAt(payment.getCreatedAt());

        return getPaymentResponseDTO;
    }
}
