package com.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {

    //TODO: check if this needs(actually required) to be added
//    @Bean
//    public ObjectMapper objectMapper() {
//            return JsonMapper.builder().configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
//                    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false).build();
//    }
}
