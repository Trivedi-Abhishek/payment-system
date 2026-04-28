package com.paymentservice.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class Error {
    private String message;
    private List<ErrorDetail> errorDetailList;
}
