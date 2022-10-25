package com.demo.payment.gateway.service.impl;

import com.demo.payment.gateway.domain.dto.Payment;
import com.demo.payment.gateway.service.DataSecureService;
import com.demo.payment.gateway.util.TestUtil;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class Base64SecureServiceImplTest {

    private DataSecureService dataSecureService = new Base64SecureServiceImpl();

    @Test
    void encryptDecryptTest() {
        Payment payment = TestUtil.buildPayment(1L);
        Payment paymentEncrypted = dataSecureService.encrypt(payment);
        Payment paymentDecrypted = dataSecureService.decrypt(paymentEncrypted);

        assertNotEquals(paymentEncrypted.getCardholder().getName(), paymentDecrypted.getCardholder().getName());
        assertNotEquals(paymentEncrypted.getCard().getPan(), paymentDecrypted.getCard().getPan());
        assertNotEquals(paymentEncrypted.getCard().getExpiry(), paymentDecrypted.getCard().getExpiry());

        assertNotEquals(paymentEncrypted, paymentDecrypted);
        assertEquals(payment, paymentDecrypted);
    }
}
