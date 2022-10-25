package com.demo.payment.gateway.service;

import com.demo.payment.gateway.domain.dto.Payment;

public interface AuditService {

    void writeAudit(Payment payment);
}
