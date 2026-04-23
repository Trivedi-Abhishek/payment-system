package com.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FraudCheckResultEvent {

    private Long paymentId;
    private String decision;
    private String reason;

}
