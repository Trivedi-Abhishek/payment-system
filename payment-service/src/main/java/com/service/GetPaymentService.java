package com.service;

import com.entity.Payment;
import com.models.GetPaymentResponseDTO;
import com.repository.PaymentsRepository;
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
