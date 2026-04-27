package com.paymentservice.service;

import com.paymentservice.entity.Payment;
import com.paymentservice.repository.PaymentsRepository;
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
            throw new RuntimeException("Entity not found!!!");
        }

        return paymentOptional.get();
    }
}
