package com.paymentservice.models;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ErrorDetail {
    private String issue;
    private String description;
    private String value;

}
