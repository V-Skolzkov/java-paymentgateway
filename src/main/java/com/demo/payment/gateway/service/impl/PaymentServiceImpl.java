package com.demo.payment.gateway.service.impl;

import com.demo.payment.gateway.service.*;
import com.demo.payment.gateway.domain.dto.Payment;
import com.demo.payment.gateway.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    @Qualifier("H2PersistenceService")
    private final PersistenceService persistenceService;
    private final DataSecureService dataSecureService;
    private final SanitizeService sanitizeService;
    private final AuditService auditService;

    @Override
    public void submitPayment(Payment payment) {
        persistenceService.persistPayment(dataSecureService.encrypt(payment));
        auditService.writeAudit(sanitizeService.sanitize(payment));
    }

    @Override
    public Payment getPayment(Long invoice, boolean sanitized) {

        Payment securedPayment = persistenceService.getPayment(invoice);
        Payment payment = dataSecureService.decrypt(securedPayment);
        if (sanitized) {
            return sanitizeService.sanitize(payment);
        } else {
            return payment;
        }
    }
}
