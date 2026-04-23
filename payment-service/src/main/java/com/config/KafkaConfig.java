package com.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaConfig {

    @Bean
    public NewTopic createInitiatedPaymentTopic() {
        return new NewTopic("payments.initiated-payment", 3, (short) 1);
    }

    @Bean
    public NewTopic createFraudCheckedPaymentTopic() {
        return new NewTopic("payments.fraud-checked-payment", 3, (short) 1);
    }
}
