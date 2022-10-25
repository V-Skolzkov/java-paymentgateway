package com.demo.payment.gateway.exception;

import java.util.Map;

public class BadRequestException extends PaymentGatewayGeneralException {

    public BadRequestException(String message) {
        super(message);
    }

    public BadRequestException(Map<String, String> errors, String message) {
        super(errors, message);
    }
    
}
