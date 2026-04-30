package com.paymentservice.controller;

import com.paymentservice.entity.Payment;
import com.paymentservice.kafka.publisher.PaymentInitiatedEventPublisher;
import com.paymentservice.mapper.PaymentsMapper;
import com.paymentservice.models.CreatePaymentResponseDTO;
import com.paymentservice.models.GetPaymentResponseDTO;
import com.paymentservice.models.PaymentRequestDTO;
import com.paymentservice.service.CreatePaymentService;
import com.paymentservice.service.GetPaymentService;
import com.paymentservice.service.RedisService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("/api/payments")
public class PaymentsController {

    //TODO: create user-register api

    private final CreatePaymentService createPaymentService;
    private final GetPaymentService getPaymentService;

    // idempotency is only important for post/put operations
    private final RedisService redisService;

    private final PaymentInitiatedEventPublisher publisher;

    public PaymentsController(CreatePaymentService createPaymentService, GetPaymentService getPaymentService, RedisService redisService, PaymentInitiatedEventPublisher publisher) {
        this.createPaymentService=createPaymentService;
        this.getPaymentService=getPaymentService;
        this.redisService=redisService;
        this.publisher=publisher;
    }

    @PostMapping("/create-payment")
    public ResponseEntity<CreatePaymentResponseDTO> createPayments(@RequestBody PaymentRequestDTO paymentRequestDTO,
                                               @RequestHeader("X-idempotency-key") String idempotencyKey, @RequestHeader("X-merchant-id") String merchantId) {

        String key=merchantId + '#' + idempotencyKey;

        CreatePaymentResponseDTO cachedData = redisService.getData(key, CreatePaymentResponseDTO.class);
        if(Objects.nonNull(cachedData)) {
            return ResponseEntity.ok(cachedData);
        }

        Payment payment = createPaymentService.createPayment(paymentRequestDTO, idempotencyKey, Long.valueOf(merchantId));
        CreatePaymentResponseDTO createPaymentResponseDTO=new CreatePaymentResponseDTO(payment, Long.valueOf(merchantId));

        // If a merchant retries a payment creation within 1 day minutes because their system was slow, you want to still return the cached response, not create a duplicate.
        redisService.setData(key, createPaymentResponseDTO, 24*60L);

        publisher.publishPaymentInitiatedEvents(createPaymentResponseDTO);

        return ResponseEntity.ok(createPaymentResponseDTO);
    }

    @GetMapping("/fetch-payment/{payment_id}")
    public ResponseEntity<GetPaymentResponseDTO> getPaymentResponse(@PathVariable("payment_id") String paymentId, @RequestHeader("X-merchant-id") String merchantId){

        String key=merchantId + '#' + paymentId;

        GetPaymentResponseDTO cachedData = redisService.getData(key, GetPaymentResponseDTO.class);
        if(Objects.nonNull(cachedData)) {
            return ResponseEntity.ok(cachedData);
        }

        Payment paymentResponse = getPaymentService.getPaymentResponse(Long.valueOf(paymentId));

        GetPaymentResponseDTO getPaymentResponseDTO = PaymentsMapper.mapPaymentToGetPaymentResponseDTO(paymentResponse);
        redisService.setData(key, getPaymentResponseDTO, 5L);

        return ResponseEntity.ok(getPaymentResponseDTO);
    }
}
