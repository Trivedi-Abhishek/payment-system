package com.paymentservice.utils;

import com.paymentservice.models.Error;
import com.paymentservice.models.ErrorDetail;
import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import java.util.Collections;

public class ExceptionUtil {

    public static void throwUnprocessableEntityException(String value, String description) {

        ErrorDetail errorDetail = getErrorDetail(value, description);

        Error error = getError("422_UNPROCESSABLE_ENTITY_EXCEPTION", errorDetail);

        throw new ApplicationException(new HttpHeaders(), HttpStatus.UNPROCESSABLE_ENTITY, error);
    }

    public static void throwBadRequestException(String value, String description) {

        ErrorDetail errorDetail = getErrorDetail(value, description);

        Error error = getError("400_BAD_REQUEST", errorDetail);

        throw new ApplicationException(new HttpHeaders(), HttpStatus.BAD_REQUEST, error);
    }

    public static void throwForbiddenException(String value, String description) {

        ErrorDetail errorDetail = getErrorDetail(value, description);

        Error error = getError("403_FORBIDDEN", errorDetail);

        throw new ApplicationException(new HttpHeaders(), HttpStatus.FORBIDDEN, error);
    }

    public static void throwResourceNotFoundException(String value, String description) {

        ErrorDetail errorDetail = getErrorDetail(value, description);

        Error error = getError("404_RESOURCE_NOT_FOUND", errorDetail);

        throw new ApplicationException(new HttpHeaders(), HttpStatus.NOT_FOUND, error);
    }

    public static void throwResourceAlreadyExistsException(String value, String description) {

        ErrorDetail errorDetail = getErrorDetail(value, description);

        Error error = getError("409_CONFLICT", errorDetail);

        throw new ApplicationException(new HttpHeaders(), HttpStatus.CONFLICT, error);
    }

    public static void throwInternalServerException(String value, String description) {

        ErrorDetail errorDetail = getErrorDetail(value, description);

        Error error = getError("500_INTERNAL_SERVER", errorDetail);

        throw new ApplicationException(new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, error);
    }

    private static @NonNull Error getError(String message, ErrorDetail errorDetail) {
        Error error=new Error();
        error.setMessage(message);
        error.setErrorDetailList(Collections.singletonList(errorDetail));
        return error;
    }

    private static @NonNull ErrorDetail getErrorDetail(String value, String description) {
        ErrorDetail errorDetail=new ErrorDetail();
        errorDetail.setValue(value);
        errorDetail.setDescription(description);
        return errorDetail;
    }
}
