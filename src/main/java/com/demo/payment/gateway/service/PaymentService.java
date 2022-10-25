package com.demo.payment.gateway.service;

import com.demo.payment.gateway.domain.dto.Payment;

public interface PaymentService {

    void submitPayment(Payment payment);

    Payment getPayment(Long invoice, boolean sanitized);
}
