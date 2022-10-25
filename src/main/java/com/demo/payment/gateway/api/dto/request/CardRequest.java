package com.demo.payment.gateway.api.dto.request;

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
public class CardRequest {

    @JsonProperty("pan")
    String pan;

    @JsonProperty("expiry")
    String expiry;

    @JsonProperty("cvv")
    String cvv;
}
