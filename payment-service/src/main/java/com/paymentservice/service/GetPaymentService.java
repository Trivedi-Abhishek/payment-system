package com.paymentservice.service;

import com.paymentservice.entity.Payment;
import com.paymentservice.repository.PaymentsRepository;
import com.paymentservice.utils.ExceptionUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GetPaymentService {

    private final PaymentsRepository paymentsRepository;

    public Payment getPaymentResponse(Long id){

        Optional<Payment> paymentOptional = paymentsRepository.findById(id);

        if(paymentOptional.isEmpty()) {
            ExceptionUtil.throwResourceNotFoundException("PAYMENT_NOT_FOUND", "No payment exists for the given payment_id");
        }

        return paymentOptional.get();
    }
}
