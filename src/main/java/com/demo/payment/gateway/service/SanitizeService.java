package com.demo.payment.gateway.service;

import com.demo.payment.gateway.domain.dto.Payment;

public interface SanitizeService {

    Payment sanitize(Payment payment);
}
