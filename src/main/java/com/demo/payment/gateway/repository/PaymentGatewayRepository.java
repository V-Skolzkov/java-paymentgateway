package com.demo.payment.gateway.repository;

import com.demo.payment.gateway.domain.entity.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentGatewayRepository extends JpaRepository<PaymentEntity, Long> {

    PaymentEntity findByInvoice(Long invoice);
}
