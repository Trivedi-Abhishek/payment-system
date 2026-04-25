package com.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.TopicPartition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.ExponentialBackOffWithMaxRetries;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class KafkaErrorConfig {

    private final KafkaTemplate<String, String> kafkaTemplate;

    @Bean
    public DefaultErrorHandler defaultErrorHandler() {

        ExponentialBackOffWithMaxRetries backOffWithMaxRetries=new ExponentialBackOffWithMaxRetries(3);
        backOffWithMaxRetries.setInitialInterval(1000L);
        backOffWithMaxRetries.setMaxAttempts(3);
        backOffWithMaxRetries.setMaxInterval(10000L);

        DeadLetterPublishingRecoverer dlqRecover=new DeadLetterPublishingRecoverer(kafkaTemplate, (record, exception) -> {
            log.error(
                    "All retries exhausted. Sending to DLQ. " +
                            "topic={} partition={} offset={} key={} error={}",
                    record.topic(),
                    record.partition(),
                    record.offset(),
                    record.key(),
                    exception.getMessage()
            );
            return new TopicPartition("payments.dlq", 0);
        });

        DefaultErrorHandler defaultErrorHandler=new DefaultErrorHandler(dlqRecover, backOffWithMaxRetries);
        defaultErrorHandler.addNotRetryableExceptions(JsonProcessingException.class, IllegalArgumentException.class);

        return defaultErrorHandler;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory(ConsumerFactory<String, String> consumerFactory, DefaultErrorHandler defaultErrorHandler) {

        ConcurrentKafkaListenerContainerFactory<String, String> concurrentKafkaListenerContainerFactor=new ConcurrentKafkaListenerContainerFactory<>();
        concurrentKafkaListenerContainerFactor.setCommonErrorHandler(defaultErrorHandler);
        concurrentKafkaListenerContainerFactor.setConsumerFactory(consumerFactory);

        return concurrentKafkaListenerContainerFactor;
    }
}
