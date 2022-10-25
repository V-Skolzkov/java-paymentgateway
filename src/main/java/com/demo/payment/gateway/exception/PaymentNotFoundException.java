package com.demo.payment.gateway.exception;

import java.util.Map;

public class PaymentNotFoundException extends PaymentGatewayGeneralException {
    public PaymentNotFoundException(String message) {
        super(message);
    }

    public PaymentNotFoundException(Map<String, String> errors, String message) {
        super(errors, message);
    }

}
