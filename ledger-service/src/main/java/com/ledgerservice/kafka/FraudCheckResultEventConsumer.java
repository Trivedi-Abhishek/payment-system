package com.ledgerservice.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ledgerservice.service.LedgerService;
import com.paymentservice.models.FraudCheckResultEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class FraudCheckResultEventConsumer {

    private final ObjectMapper objectMapper;
    private final LedgerService ledgerService;

    @KafkaListener(topics = "payments.fraud-checked-payment", groupId = "fraud-service-group")
    public void consumeFraudCheckResultEventMessage(String fraudCheckResultEventMessage) {

        try {
            FraudCheckResultEvent fraudCheckResultEvent = objectMapper.readValue(fraudCheckResultEventMessage, FraudCheckResultEvent.class);
            if("VALID".equals(fraudCheckResultEvent.getDecision())) {
                ledgerService.createLedgerEntries(fraudCheckResultEvent);
            }

        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
        }
        catch (Exception e) {
            log.error(e.getMessage());
            throw e;
        }
    }
}
