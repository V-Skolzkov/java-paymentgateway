package com.demo.payment.gateway.domain.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;

@Entity
@Table(name = "t_payment")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class PaymentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "invoice")
    Long invoice;

    @Column(name = "amount")
    Integer amount;

    @Column(name = "currency")
    String currency;

    @JoinColumn(name = "card_holder_id", referencedColumnName = "id")
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    CardholderEntity cardholder;

    @JoinColumn(name = "card_id", referencedColumnName = "id")
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    CardEntity card;
}

