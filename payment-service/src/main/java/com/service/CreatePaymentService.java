package com.service;

import com.entity.Payment;
import com.enums.TransactionStatusEnum;
import com.models.PaymentRequestDTO;
import com.repository.PaymentsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class CreatePaymentService {

    private final PaymentsRepository paymentsRepository;

    public Payment createPayment(PaymentRequestDTO paymentRequestDTO, String idempotentKey) {

        Date currentDate=new Date();

        Payment payment = Payment.builder().amount(paymentRequestDTO.getAmountDetails().getAmount())
                .currencyCode(paymentRequestDTO.getAmountDetails().getCurrencyCode())
                .createdAt(currentDate).reason(paymentRequestDTO.getReason()).idempotentKey(idempotentKey)
                .transactionStatus(TransactionStatusEnum.INITIATED).build();

        return paymentsRepository.save(payment);

    }
}
