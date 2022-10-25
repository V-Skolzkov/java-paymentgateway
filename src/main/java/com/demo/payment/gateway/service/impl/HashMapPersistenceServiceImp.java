package com.demo.payment.gateway.service.impl;

import com.demo.payment.gateway.domain.dto.Payment;
import com.demo.payment.gateway.exception.PaymentAlreadyExistsException;
import com.demo.payment.gateway.exception.PaymentNotFoundException;
import com.demo.payment.gateway.service.PersistenceService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
@Qualifier("hashMapPersistenceService")
public class HashMapPersistenceServiceImp implements PersistenceService {

    private static final Map<Long, Payment> paymentMap = new HashMap<>();

    @Override
    public void persistPayment(Payment payment) {

        Payment paymentFromMap = paymentMap.get(payment.getInvoice());
        if (Objects.isNull(paymentFromMap)) {
            synchronized (this) {
                paymentFromMap = paymentMap.get(payment.getInvoice());
                if (Objects.isNull(paymentFromMap)) {
                    paymentMap.put(payment.getInvoice(), payment);
                }
            }
        }
        if (Objects.nonNull(paymentFromMap)) {
            throw new PaymentAlreadyExistsException(String.format("Payment with invoice %s already exists!", payment.getInvoice()));
        }
    }

    @Override
    public Payment getPayment(Long invoice) {

        Payment payment = paymentMap.get(invoice);
        if (Objects.nonNull(payment)) {
            return payment;
        } else {
            throw new PaymentNotFoundException(String.format("Payment with invoice %s not found!", invoice));
        }
    }
}
