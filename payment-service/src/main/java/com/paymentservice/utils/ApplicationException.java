package com.paymentservice.utils;

import com.paymentservice.models.Error;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
public class ApplicationException extends RuntimeException{
    private HttpHeaders httpHeaders;
    private HttpStatus httpStatus;
    private Error error;
}
