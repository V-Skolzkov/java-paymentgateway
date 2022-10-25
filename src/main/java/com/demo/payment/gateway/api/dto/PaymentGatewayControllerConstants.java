package com.demo.payment.gateway.api.dto;

public final class PaymentGatewayControllerConstants {

    private PaymentGatewayControllerConstants() {
    }

    public static final String PAYMENT_REQUEST = "{\"invoice\": 3,\"amount\": 1,\"currency\": \"EUR\",\"cardholder\": {\"name\": \"First Last\",\"email\": \"email@domain.com\"},\"card\": {\"pan\": \"5375414109569904\",\"expiry\": \"1022\",\"cvv\": \"789\"}}";
    public static final String SUBMIT_PAYMENT_RESPONSE = "{\"approved\": true}";
    public static final String VALIDATION_ERROR = "{\"approved\": false,\"errors\": {\"expiry\": \"Invalid value for parameter expiry, value = {1021}, expiry date in past.\"}}";
    public static final String PAYMENT_ALREADY_EXISTS = "{\"approved\": false,\"errors\": {\"generalDescription\": \"Payment with invoice 3 already exists!\"}}";
    public static final String INTERNAL_SERVER_ERROR = "{\"approved\": false,\"errors\": {\"generalDescription\": \"Internal server error!\"}}";
    public static final String PAYMENT_NOT_FOUND = "{\"approved\": false,\"errors\": {\"generalDescription\": \"Payment with invoice 1 not found!\"}}";
    public static final String PAYMENT_RESPONSE = "{\"invoice\": 3,\"amount\": 1,\"currency\": \"EUR\",\"cardholder\": {\"name\": \"**********\",\"email\": \"email@domain.com\"},\"card\": {\"pan\": \"************9904\",\"expiry\": \"****\"}}";
}
