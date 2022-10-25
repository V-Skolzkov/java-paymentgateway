package com.demo.payment.gateway.api.dto.response;

import com.demo.payment.gateway.domain.dto.Payment;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponse {

    @JsonProperty("invoice")
    Long invoice;

    @JsonProperty("amount")
    Integer amount;

    @JsonProperty("currency")
    String currency;

    @JsonProperty("cardholder")
    CardholderResponse cardholderResponse;

    @JsonProperty("card")
    CardResponse cardResponse;

    public PaymentResponse fromDomain(Payment payment) {
        invoice = payment.getInvoice();
        amount = payment.getAmount();
        currency = payment.getCurrency();
        cardholderResponse = CardholderResponse.builder()
                .name(payment.getCardholder().getName())
                .email(payment.getCardholder().getEmail())
                .build();
        cardResponse = CardResponse.builder()
                .pan(payment.getCard().getPan())
                .expiry(payment.getCard().getExpiry())
                .build();
        return this;
    }
}
