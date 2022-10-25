package com.demo.payment.gateway.service.impl;

import com.demo.payment.gateway.domain.dto.Payment;
import com.demo.payment.gateway.exception.PaymentAlreadyExistsException;
import com.demo.payment.gateway.exception.PaymentNotFoundException;
import com.demo.payment.gateway.repository.PaymentGatewayRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static com.demo.payment.gateway.util.TestUtil.buildPayment;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
public class H2PersistenceServiceImpTest {

    @Autowired
    PaymentGatewayRepository paymentGatewayRepository;

    H2PersistenceServiceImp h2PersistenceServiceImp;

    @BeforeEach
    void before() {
        h2PersistenceServiceImp = new H2PersistenceServiceImp(paymentGatewayRepository);
    }

    @Test
    void persistPaymentOkTest() {
        Payment payment = buildPayment(1L);
        h2PersistenceServiceImp.persistPayment(payment);
        Payment paymentFromRepository = h2PersistenceServiceImp.getPayment(1L);
        assertEquals(payment, paymentFromRepository);
    }

    @Test
    void persistPaymentExceptionTest() {
        Payment payment = buildPayment(1L);
        h2PersistenceServiceImp.persistPayment(payment);

        PaymentAlreadyExistsException exception =
                assertThrows(PaymentAlreadyExistsException.class, () -> h2PersistenceServiceImp.persistPayment(payment));

        assertEquals("Payment with invoice 1 already exists!", exception.getMessage());
    }

    @Test
    void getPaymentExceptionTest() {

        PaymentNotFoundException exception =
                assertThrows(PaymentNotFoundException.class, () -> h2PersistenceServiceImp.getPayment(1l));

        assertEquals("Payment with invoice 1 not found!", exception.getMessage());
    }
}
