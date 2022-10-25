package com.demo.payment.gateway.exception;

import java.util.Map;

public class PaymentAlreadyExistsException extends PaymentGatewayGeneralException {

    public PaymentAlreadyExistsException(String message) {
        super(message);
    }

    public PaymentAlreadyExistsException(Map<String, String> errors, String message) {
        super(errors, message);
    }

}
