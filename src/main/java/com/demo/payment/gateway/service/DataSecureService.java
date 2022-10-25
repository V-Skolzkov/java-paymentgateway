package com.demo.payment.gateway.service;

import com.demo.payment.gateway.domain.dto.Payment;

public interface DataSecureService {

    Payment encrypt(Payment payment);

    Payment decrypt(Payment payment);
}
