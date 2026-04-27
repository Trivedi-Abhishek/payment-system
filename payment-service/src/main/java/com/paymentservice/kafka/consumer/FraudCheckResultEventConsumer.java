package com.paymentservice.kafka.consumer;

import com.paymentservice.entity.Payment;
import com.paymentservice.enums.TransactionStatusEnum;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.paymentservice.models.FraudCheckResultEvent;
import com.paymentservice.repository.PaymentsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class FraudCheckResultEventConsumer {

    private final PaymentsRepository paymentsRepository;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "payments.fraud-checked-payment", groupId = "payment-service-group")
    public void consumeSuccessFraudCheckEvent(String fraudCheckResultEventMessage) {

        try {
            FraudCheckResultEvent fraudCheckResultEvent = objectMapper.readValue(objectMapper.writeValueAsString(fraudCheckResultEventMessage), FraudCheckResultEvent.class);
            Optional<Payment> paymentOptional = paymentsRepository.findById(fraudCheckResultEvent.getPaymentId());
            if(paymentOptional.isPresent()) {
                Payment payment = paymentOptional.get();
                payment.setTransactionStatus("VALID".equals(fraudCheckResultEvent.getDecision()) ? TransactionStatusEnum.FRAUD_CHECKED:TransactionStatusEnum.FAILED);
                payment.setReason(fraudCheckResultEvent.getReason());
                payment.setUpdatedAt(new Date());
                paymentsRepository.save(payment);
            }
            else{
                log.error("No payment found");
            }
        }
        catch (JsonProcessingException e) {
            log.error(e.getMessage());
        }
        catch (Exception e) {
            log.error(e.getMessage());
            throw e;
        }
    }
}
