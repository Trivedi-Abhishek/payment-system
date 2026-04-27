package com.kafka.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class FraudServiceDLQConsumer {

    //TODO: check if this needs(actually required) to be added
//    @KafkaListener(topics = "payments.dlq", groupId = "dlq-group")
//    public void consumerDLQMessage(            String message,
//                                               @Header(KafkaHeaders.RECEIVED_TOPIC) String originalTopic,
//                                               @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
//                                               @Header(KafkaHeaders.OFFSET) long offset,
//                                               @Header(value = "kafka_dlt-exception-message", required = false) String exceptionMessage,
//                                               @Header(value = "kafka_dlt-original-topic", required = false) String dlqOriginalTopic) {
//
//        log.error(
//                " originalTopic={}" +
//                        " partition={}" +
//                        " offset={}" +
//                        " exceptionMessage={}" +
//                        " rawMessage={}",
//                dlqOriginalTopic != null ? dlqOriginalTopic : originalTopic,
//                partition,
//                offset,
//                exceptionMessage,
//                message
//        );
//    }
}
