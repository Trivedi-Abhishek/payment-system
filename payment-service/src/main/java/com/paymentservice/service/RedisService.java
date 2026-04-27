package com.paymentservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class RedisService {

    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;

    public <T> T getData(String key, Class<T> responseClass) {

        String responseClassObject = redisTemplate.opsForValue().get(key);
        if(Objects.isNull(responseClassObject)) {
            return null;
        }
        try {
            return objectMapper.readValue(responseClassObject, responseClass);
        } catch (JsonProcessingException e) {
            log.error("Failed to deserialize Redis value for key={} error={}", key, e.getMessage());
        }
        return null;
    }

    public void setData(String key, Object valueObject, Long ttl) {

        try {
            String jsonValueObject = objectMapper.writeValueAsString(valueObject);
            redisTemplate.opsForValue().set(key, jsonValueObject, ttl, TimeUnit.MINUTES);
        } catch (JsonProcessingException e) {
            log.error("Failed to deserialize Redis value for key={} error={}", key, e.getMessage());
        }
    }
}
