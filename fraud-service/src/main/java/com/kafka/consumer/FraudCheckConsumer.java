package com.kafka.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.paymentservice.models.FraudCheckResultEvent;
import com.paymentservice.models.PaymentInitiatedEvent;
import com.service.FraudDetectionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Slf4j
public class FraudCheckConsumer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;
    private final FraudDetectionService fraudDetectionService;

    @KafkaListener(topics = "payments.initiated-payment", groupId = "fraud-service-group")
    public void consume(String paymentInitiatedEventMessage) {

        try {
            PaymentInitiatedEvent paymentInitiatedEvent = objectMapper.readValue(paymentInitiatedEventMessage, PaymentInitiatedEvent.class);

            FraudCheckResultEvent fraudCheckResultEvent = fraudDetectionService.getFraudCheckResultEvent(paymentInitiatedEvent);
            String resultJson = objectMapper.writeValueAsString(fraudCheckResultEvent);
            CompletableFuture<SendResult<String, String>> fraudCheckedEventFuture = kafkaTemplate.send("payments.fraud-checked-payment", String.valueOf(paymentInitiatedEvent.getPaymentId()), resultJson);

            fraudCheckedEventFuture.whenComplete((result, exception)->{

                if(Objects.isNull(exception)) {
                    String topic = result.getRecordMetadata().topic();
                    int partition = result.getRecordMetadata().partition();
                    long offset = result.getRecordMetadata().offset();

                    log.info("Fraud checked payment event published to topic: {}, partition: {}, offset: {}",topic, partition, offset);
                }
                else {
                    log.error("Exception occurred: {}", exception.getMessage());
                }
            });
        }
        catch (JsonProcessingException e) {
            log.error(e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage());
            throw e;
        }

    }
}
