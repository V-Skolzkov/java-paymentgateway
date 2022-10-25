package com.demo.payment.gateway.api.validate;

import com.demo.payment.gateway.api.dto.request.CardRequest;
import com.demo.payment.gateway.api.dto.request.CardholderRequest;
import com.demo.payment.gateway.api.dto.request.PaymentRequest;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class PaymentValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return PaymentRequest.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {

        PaymentRequest paymentRequest = (PaymentRequest) target;
        ValidationUtil.isNull(paymentRequest.getInvoice(), "invoice", "invoice", errors);
        ValidationUtil.isPositiveInt(paymentRequest.getAmount(), "amount", "amount", errors);
        ValidationUtil.isNull(paymentRequest.getCurrency(), "currency", "currency", errors);
        CardholderRequest cardholderRequest = paymentRequest.getCardholderRequest();
        errors.pushNestedPath("cardholderRequest");
        if (!ValidationUtil.isNull(cardholderRequest, "cardholderRequest", "cardholder", errors)) {
            ValidationUtil.isNull(cardholderRequest.getName(), "name", "name", errors);
            ValidationUtil.checkEmail(cardholderRequest.getEmail(), "email", "email", errors);
        }
        errors.popNestedPath();
        errors.pushNestedPath("cardRequest");
        CardRequest cardRequest = paymentRequest.getCardRequest();
        if (!ValidationUtil.isNull(cardRequest, "cardRequest", "card", errors)) {
            ValidationUtil.checkPan(cardRequest.getPan(), "pan", "pan", errors);
            ValidationUtil.checkExpiryDate(cardRequest.getExpiry(), "expiry", "expiry", errors);
        }
        errors.popNestedPath();
    }
}
