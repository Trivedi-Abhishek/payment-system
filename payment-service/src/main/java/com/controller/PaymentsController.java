package com.controller;

import com.entity.Payment;
import com.mapper.PaymentsMapper;
import com.models.CreatePaymentResponseDTO;
import com.models.GetPaymentResponseDTO;
import com.models.PaymentRequestDTO;
import com.service.CreatePaymentService;
import com.service.GetPaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
public class PaymentsController {

    private final CreatePaymentService createPaymentService;
    private final GetPaymentService getPaymentService;

    public PaymentsController(CreatePaymentService createPaymentService, GetPaymentService getPaymentService) {
        this.createPaymentService=createPaymentService;
        this.getPaymentService=getPaymentService;
    }

    @PostMapping("/create-payment")
    public ResponseEntity<CreatePaymentResponseDTO> createPayments(@RequestBody PaymentRequestDTO paymentRequestDTO,
                                               @RequestHeader("idempotency_key") String idempotencyKey) {

        Long paymentId = createPaymentService.createPayment(paymentRequestDTO, idempotencyKey);
        CreatePaymentResponseDTO createPaymentResponseDTO=new CreatePaymentResponseDTO(paymentId);
        return ResponseEntity.ok(createPaymentResponseDTO);
    }

    @GetMapping("/fetch-payment/{payment_id}")
    public ResponseEntity<GetPaymentResponseDTO> getPaymentResponse(@PathVariable("payment_id") String paymentId){
        Payment paymentResponse = getPaymentService.getPaymentResponse(Long.valueOf(paymentId));
        return ResponseEntity.ok(PaymentsMapper.mapPaymentToGetPaymentResponseDTO(paymentResponse));
    }
}
