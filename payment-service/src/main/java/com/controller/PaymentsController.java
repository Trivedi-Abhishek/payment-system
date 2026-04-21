package com.controller;

import com.entity.Payment;
import com.mapper.PaymentsMapper;
import com.models.CreatePaymentResponseDTO;
import com.models.GetPaymentResponseDTO;
import com.models.PaymentRequestDTO;
import com.service.CreatePaymentService;
import com.service.GetPaymentService;
import com.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("/api/payments")
public class PaymentsController {

    private final CreatePaymentService createPaymentService;
    private final GetPaymentService getPaymentService;

    // idempotency is only important for post/put operations
    private final RedisService redisService;

    public PaymentsController(CreatePaymentService createPaymentService, GetPaymentService getPaymentService, RedisService redisService) {
        this.createPaymentService=createPaymentService;
        this.getPaymentService=getPaymentService;
        this.redisService=redisService;
    }

    @PostMapping("/create-payment")
    public ResponseEntity<CreatePaymentResponseDTO> createPayments(@RequestBody PaymentRequestDTO paymentRequestDTO,
                                               @RequestHeader("X-idempotency-key") String idempotencyKey, @RequestHeader("X-merchant-id") String merchantId) {

        String key=merchantId + '#' + idempotencyKey;

        CreatePaymentResponseDTO cachedData = redisService.getData(key, CreatePaymentResponseDTO.class);
        if(Objects.nonNull(cachedData)) {
            return ResponseEntity.ok(cachedData);
        }

        Long paymentId = createPaymentService.createPayment(paymentRequestDTO, idempotencyKey);
        CreatePaymentResponseDTO createPaymentResponseDTO=new CreatePaymentResponseDTO(paymentId);

        // If a merchant retries a payment creation after 6 minutes because their system was slow, you want to still return the cached response, not create a duplicate.
        redisService.setData(key, createPaymentResponseDTO, 24*60L);

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
