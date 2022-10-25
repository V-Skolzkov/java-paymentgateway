package com.demo.payment.gateway.exception;

import java.util.Map;

public class PaymentGatewayGeneralException extends RuntimeException {

    private Map<String, String> errors;

    protected PaymentGatewayGeneralException(String message) {
        super(message);
    }

    protected PaymentGatewayGeneralException(Map<String, String> errors, String message) {
        this(message);
        this.errors = errors;
    }

    public Map<String, String> getErrors() {
        return errors;
    }
}

