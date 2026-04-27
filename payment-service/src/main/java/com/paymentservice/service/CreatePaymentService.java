package com.paymentservice.service;

import com.paymentservice.entity.Payment;
import com.paymentservice.enums.TransactionStatusEnum;
import com.paymentservice.models.PaymentRequestDTO;
import com.paymentservice.repository.PaymentsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class CreatePaymentService {

    private final PaymentsRepository paymentsRepository;

    public Payment createPayment(PaymentRequestDTO paymentRequestDTO, String idempotentKey, Long merchantId) {

        Date currentDate=new Date();

        Payment payment = Payment.builder().amount(paymentRequestDTO.getAmountDetails().getAmount())
                .currencyCode(paymentRequestDTO.getAmountDetails().getCurrencyCode())
                .createdAt(currentDate).reason(paymentRequestDTO.getReason()).idempotentKey(idempotentKey)
                .transactionStatus(TransactionStatusEnum.INITIATED).merchantId(merchantId).userId(Long.valueOf(paymentRequestDTO.getUserId())).build();

        return paymentsRepository.save(payment);

    }
}
