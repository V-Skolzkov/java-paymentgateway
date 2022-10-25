package com.demo.payment.gateway.domain.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode
public class Cardholder {

    String name;
    String email;
}
