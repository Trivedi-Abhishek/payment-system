package com.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class ChatClientConfig {

    @Bean
    public ChatClient chatClient(ChatClient.Builder chatClientBuilder) {

        ChatOptions chatOptions=ChatOptions.builder()
                .temperature(0.1).build();

        return chatClientBuilder
                .defaultOptions(chatOptions)
                .defaultAdvisors(List.of(new SimpleLoggerAdvisor()))
                .defaultSystem("You are payment fraud detector system check whether the payment details is fraud or valid")
                .build();
    }
}
