package com.kafka.publisher;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.models.AmountDetails;
import com.models.CreatePaymentResponseDTO;
import com.models.PaymentInitiatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentInitiatedEventPublisher {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public void publishPaymentInitiatedEvents(CreatePaymentResponseDTO createPaymentResponseDTO) {

        PaymentInitiatedEvent paymentInitiatedEvent=new PaymentInitiatedEvent(createPaymentResponseDTO);
        try {
            CompletableFuture<SendResult<String, String>> paymentInitiatedEventFuture = kafkaTemplate.send("payments.initiated-payment", String.valueOf(createPaymentResponseDTO.getPaymentId()), objectMapper.writeValueAsString(paymentInitiatedEvent));

            paymentInitiatedEventFuture.whenComplete((result, exception)->{

                if(Objects.isNull(exception)) {
                    String topic = result.getRecordMetadata().topic();
                    int partition = result.getRecordMetadata().partition();
                    long offset = result.getRecordMetadata().offset();

                    log.info("Initiated payment event published to topic: {}, partition: {}, offset: {}",topic, partition, offset);
                }
                else {
                    log.error("Exception occurred: {}", exception.getMessage());
                }
            });
        }
        catch (JsonProcessingException e) {
            log.error(e.getMessage());
        }

    }
}
