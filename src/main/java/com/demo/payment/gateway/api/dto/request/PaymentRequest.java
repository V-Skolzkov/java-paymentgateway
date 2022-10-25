package com.demo.payment.gateway.api.dto.request;

import com.demo.payment.gateway.domain.dto.Card;
import com.demo.payment.gateway.domain.dto.Cardholder;
import com.demo.payment.gateway.domain.dto.Payment;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class PaymentRequest {

    @JsonProperty("invoice")
    Long invoice;

    @JsonProperty("amount")
    Integer amount;

    @JsonProperty("currency")
    String currency;

    @JsonProperty("cardholder")
    CardholderRequest cardholderRequest;

    @JsonProperty("card")
    CardRequest cardRequest;

    public Payment toDomain() {
        return Payment.builder()
                .invoice(invoice)
                .amount(amount)
                .currency(currency)
                .cardholder(Cardholder.builder()
                        .name(cardholderRequest.getName())
                        .email(cardholderRequest.getEmail())
                        .build())
                .card(Card.builder()
                        .pan(cardRequest.getPan())
                        .expiry(cardRequest.getExpiry())
                        .cvv(cardRequest.getCvv())
                        .build())
                .build();
    }
}
