package com.demo.payment.gateway.service.impl;

import com.demo.payment.gateway.domain.dto.Card;
import com.demo.payment.gateway.domain.dto.Cardholder;
import com.demo.payment.gateway.domain.dto.Payment;
import com.demo.payment.gateway.domain.entity.CardEntity;
import com.demo.payment.gateway.domain.entity.CardholderEntity;
import com.demo.payment.gateway.domain.entity.PaymentEntity;
import com.demo.payment.gateway.exception.PaymentAlreadyExistsException;
import com.demo.payment.gateway.exception.PaymentNotFoundException;
import com.demo.payment.gateway.repository.PaymentGatewayRepository;
import com.demo.payment.gateway.service.PersistenceService;
import lombok.RequiredArgsConstructor;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@Qualifier("H2PersistenceService")
@RequiredArgsConstructor
public class H2PersistenceServiceImp implements PersistenceService {

    private final PaymentGatewayRepository paymentGatewayRepository;

    @Override
    public void persistPayment(Payment payment) {

        try {
            paymentGatewayRepository.save(toPaymentEntity(payment));
        } catch (Exception ex) {
            Throwable cause = ex.getCause();
            if (cause instanceof ConstraintViolationException) {
                throw new PaymentAlreadyExistsException(String.format("Payment with invoice %s already exists!", payment.getInvoice()));
            } else {
                throw ex;
            }
        }
    }

    @Override
    public Payment getPayment(Long invoice) {
        PaymentEntity paymentEntity = paymentGatewayRepository.findByInvoice(invoice);
        if (Objects.nonNull(paymentEntity)) {
            return toPayment(paymentEntity);
        } else {
            throw new PaymentNotFoundException(String.format("Payment with invoice %s not found!", invoice));
        }
    }

    private Payment toPayment(PaymentEntity paymentEntity) {

        return Payment.builder()
                .invoice(paymentEntity.getInvoice())
                .amount(paymentEntity.getAmount())
                .currency(paymentEntity.getCurrency())
                .cardholder(Cardholder.builder()
                        .name(paymentEntity.getCardholder().getName())
                        .email(paymentEntity.getCardholder().getEmail())
                        .build())
                .card(Card.builder()
                        .pan(paymentEntity.getCard().getPan())
                        .expiry(paymentEntity.getCard().getExpiry())
                        .build())
                .build();
    }

    private PaymentEntity toPaymentEntity(Payment payment) {

        return PaymentEntity.builder()
                .invoice(payment.getInvoice())
                .amount(payment.getAmount())
                .currency(payment.getCurrency())
                .cardholder(CardholderEntity.builder()
                        .name(payment.getCardholder().getName())
                        .email(payment.getCardholder().getEmail())
                        .build())
                .card(CardEntity.builder()
                        .pan(payment.getCard().getPan())
                        .expiry(payment.getCard().getExpiry())
                        .build())
                .build();
    }
}
