package com.demo.payment.gateway.service;

import com.demo.payment.gateway.domain.dto.Payment;

public interface PersistenceService {

    void persistPayment(Payment payment);

    Payment getPayment(Long invoice);
}
