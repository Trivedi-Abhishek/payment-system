package com.paymentservice.service;

import com.paymentservice.entity.Payment;
import com.paymentservice.enums.TransactionStatusEnum;
import com.paymentservice.models.PaymentRequestDTO;
import com.paymentservice.repository.PaymentsRepository;
import com.paymentservice.utils.ExceptionUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class CreatePaymentService {

    private final PaymentsRepository paymentsRepository;

    public Payment createPayment(PaymentRequestDTO paymentRequestDTO, String idempotentKey, Long merchantId) {

        Date currentDate=new Date();

        if(paymentsRepository.existsByMerchantIdAndIdempotentKey(merchantId, idempotentKey)) {
            ExceptionUtil.throwResourceAlreadyExistsException("RESOURCE_ALREADY_EXISTS", "Payment for the given merchant_id and idempotent_key already exists");
        }
        Payment payment = Payment.builder().amount(paymentRequestDTO.getAmountDetails().getAmount())
                .currencyCode(paymentRequestDTO.getAmountDetails().getCurrencyCode())
                .createdAt(currentDate).reason(paymentRequestDTO.getReason()).idempotentKey(idempotentKey)
                .transactionStatus(TransactionStatusEnum.INITIATED).merchantId(merchantId).userId(Long.valueOf(paymentRequestDTO.getUserId())).build();

        return paymentsRepository.save(payment);

    }
}
