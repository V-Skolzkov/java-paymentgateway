package com.demo.payment.gateway.service.impl;

import com.demo.payment.gateway.domain.dto.Payment;
import com.demo.payment.gateway.exception.PaymentAlreadyExistsException;
import com.demo.payment.gateway.exception.PaymentNotFoundException;
import com.demo.payment.gateway.service.AuditService;
import com.demo.payment.gateway.service.DataSecureService;
import com.demo.payment.gateway.service.PaymentService;
import com.demo.payment.gateway.service.SanitizeService;
import com.demo.payment.gateway.service.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;

import static com.demo.payment.gateway.util.TestUtil.buildPayment;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@MockitoSettings
public class PaymentServiceImplTest {

    private DataSecureService dataSecureService = new Base64SecureServiceImpl();
    private SanitizeService sanitizeService = new SanitizeServiceImpl();

    @Mock
    private AuditService auditService;

    private PaymentService paymentService;

    @Captor
    ArgumentCaptor<Payment> paymentArgumentCaptor;

    @BeforeEach
    void before() {
        paymentService = new PaymentServiceImpl(new HashMapPersistenceServiceImp(),
                dataSecureService, sanitizeService, auditService);
    }

    @Test
    void submitPaymentOkTest() {

        doNothing().when(auditService).writeAudit(any());
        Payment payment = buildPayment(1L);
        paymentService.submitPayment(payment);
        verify(auditService, times(1)).writeAudit(any());
        verify(auditService).writeAudit(paymentArgumentCaptor.capture());
        Payment paymentSanitized = sanitizeService.sanitize(payment);
        assertEquals(paymentSanitized, paymentArgumentCaptor.getValue());
    }

    @Test
    void submitPaymentPaymentAlreadyExistsExceptionTest() {

        Payment payment = buildPayment(2L);

        paymentService.submitPayment(payment);

        PaymentAlreadyExistsException exception =
                assertThrows(PaymentAlreadyExistsException.class, () -> paymentService.submitPayment(payment));

        assertEquals("Payment with invoice 2 already exists!", exception.getMessage());
    }

    @Test
    void getPaymentOkTest() {

        Payment payment = buildPayment(3L);
        paymentService.submitPayment(payment);

        Payment paymentFromMap = paymentService.getPayment(3L, false);
        assertEquals(payment, paymentFromMap);

        Payment paymentFromMapSanitized = paymentService.getPayment(3L, true);
        Assertions.assertEquals(sanitizeService.sanitize(payment), paymentFromMapSanitized);
    }

    @Test
    void getPaymentPaymentNotFoundExceptionTest() {

        PaymentNotFoundException exception =
                assertThrows(PaymentNotFoundException.class, () -> paymentService.getPayment(4L, true));

        assertEquals("Payment with invoice 4 not found!", exception.getMessage());
    }
}
