package com.demo.payment.gateway.util;

import com.demo.payment.gateway.domain.dto.Card;
import com.demo.payment.gateway.domain.dto.Cardholder;
import com.demo.payment.gateway.domain.dto.Payment;

public final class TestUtil {

    private TestUtil() {
    }

    public static Payment buildPayment(Long invoice) {

        return Payment.builder()
                .invoice(invoice)
                .amount(100)
                .currency("EUR")
                .cardholder(Cardholder.builder()
                        .name("First Last")
                        .email("email@domain.com")
                        .build())
                .card(Card.builder()
                        .pan("5375414109569904")
                        .expiry("1022")
                        .build())
                .build();
    }
}
