package com.demo.payment.gateway.service.impl;

import com.demo.payment.gateway.domain.dto.Card;
import com.demo.payment.gateway.domain.dto.Cardholder;
import com.demo.payment.gateway.domain.dto.Payment;
import com.demo.payment.gateway.service.DataSecureService;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Service
public class Base64SecureServiceImpl implements DataSecureService {

    private final static Base64.Encoder encoder = Base64.getEncoder();
    private final static Base64.Decoder decoder = Base64.getDecoder();

    @Override
    public Payment encrypt(Payment payment) {

        return Payment.builder()
                .invoice(payment.getInvoice())
                .amount(payment.getAmount())
                .currency(payment.getCurrency())
                .cardholder(Cardholder.builder()
                        .email(payment.getCardholder().getEmail())
                        .name(encoder.encodeToString(payment.getCardholder().getName().getBytes(StandardCharsets.UTF_8)))
                        .build())
                .card(Card.builder()
                        .pan(encoder.encodeToString(payment.getCard().getPan().getBytes(StandardCharsets.UTF_8)))
                        .expiry(encoder.encodeToString(payment.getCard().getExpiry().getBytes(StandardCharsets.UTF_8)))
                        .build())
                .build();
    }

    @Override
    public Payment decrypt(Payment payment) {
        return Payment.builder()
                .invoice(payment.getInvoice())
                .amount(payment.getAmount())
                .currency(payment.getCurrency())
                .cardholder(Cardholder.builder()
                        .email(payment.getCardholder().getEmail())
                        .name(new String(decoder.decode(payment.getCardholder().getName()), StandardCharsets.UTF_8))
                        .build())
                .card(Card.builder()
                        .pan(new String(decoder.decode(payment.getCard().getPan()), StandardCharsets.UTF_8))
                        .expiry(new String(decoder.decode(payment.getCard().getExpiry()), StandardCharsets.UTF_8))
                        .build())
                .build();
    }
}
