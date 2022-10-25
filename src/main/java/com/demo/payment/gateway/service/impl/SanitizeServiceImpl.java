package com.demo.payment.gateway.service.impl;

import com.demo.payment.gateway.service.SanitizeService;
import com.demo.payment.gateway.domain.dto.Card;
import com.demo.payment.gateway.domain.dto.Cardholder;
import com.demo.payment.gateway.domain.dto.Payment;
import org.springframework.stereotype.Service;

@Service
public class SanitizeServiceImpl implements SanitizeService {

    private static final String maskString = "*";

    @Override
    public Payment sanitize(Payment payment) {

        return Payment.builder()
                .invoice(payment.getInvoice())
                .amount(payment.getAmount())
                .currency(payment.getCurrency())
                .cardholder(Cardholder.builder()
                        .email(payment.getCardholder().getEmail())
                        .name(maskAll(payment.getCardholder().getName()))
                        .build())
                .card(Card.builder()
                        .pan(maskPan(payment.getCard().getPan()))
                        .expiry(maskAll(payment.getCard().getExpiry()))
                        .build())
                .build();
    }

    private static String maskAll(String value) {
        return maskString.repeat(value.length());
    }

    private static String maskPan(String value) {
        int idx = value.length() - 4;
        return maskString.repeat(idx) + value.substring(idx);
    }
}
