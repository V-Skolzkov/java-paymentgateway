package com.demo.payment.gateway.api;

import com.demo.payment.gateway.api.dto.request.PaymentRequest;
import com.demo.payment.gateway.api.dto.response.PaymentResponse;
import com.demo.payment.gateway.api.dto.response.SubmitPaymentResponse;
import com.demo.payment.gateway.api.validate.PaymentValidator;
import com.demo.payment.gateway.domain.dto.Payment;
import com.demo.payment.gateway.exception.BadRequestException;
import com.demo.payment.gateway.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.stream.Collectors;

import static com.demo.payment.gateway.api.dto.PaymentGatewayControllerConstants.*;

@Tag(name = "Public API", description = "Payment Gateway API")
@RestController
@RequestMapping("/v1/gateway")
@Slf4j
@RequiredArgsConstructor
public class PaymentGatewayController {
    private final PaymentService paymentService;

    @InitBinder
    private void initBinder(WebDataBinder binder) {
        binder.addValidators(new PaymentValidator());
    }


    @Operation(summary = "Submit new payment")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(schema = @Schema(implementation = PaymentRequest.class),
            examples = @ExampleObject(value = PAYMENT_REQUEST)))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = @Content(
                            schema = @Schema(implementation = SubmitPaymentResponse.class),
                            examples = @ExampleObject(value = SUBMIT_PAYMENT_RESPONSE)
                    )),
            @ApiResponse(responseCode = "400", description = "Request validation error",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = VALIDATION_ERROR)
                    )),
            @ApiResponse(responseCode = "409", description = "Payment already exists",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = PAYMENT_ALREADY_EXISTS)
                    )),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = INTERNAL_SERVER_ERROR)
                    ))
    })
    @RequestMapping(
            method = {RequestMethod.POST},
            value = {"/payment"},
            produces = {"application/json"},
            consumes = {"application/json"}
    )
    ResponseEntity<SubmitPaymentResponse> submitPayment(@Validated @RequestBody PaymentRequest paymentRequest,
                                                        BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            Map<String, String> errors = bindingResult.getFieldErrors().stream().map(e ->
                    Map.entry(e.getArguments()[0].toString(), e.getDefaultMessage())
            ).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
            throw new BadRequestException(errors, "Input data not valid!");
        }

        Payment payment = paymentRequest.toDomain();

        paymentService.submitPayment(payment);
        return ResponseEntity.status(HttpStatus.OK)
                .body(SubmitPaymentResponse.builder().approved(true).build());
    }

    @Operation(summary = "Get payment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Return payment",
                    content = @Content(
                            schema = @Schema(implementation = PaymentResponse.class),
                            examples = @ExampleObject(value = PAYMENT_RESPONSE)
                    )),
            @ApiResponse(responseCode = "404", description = "Payment not found",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = PAYMENT_NOT_FOUND)
                    )),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = INTERNAL_SERVER_ERROR)
                    ))
    })
    @RequestMapping(
            method = {RequestMethod.GET},
            value = {"/payment/get/{invoice}"},
            produces = {"application/json"}
    )
    ResponseEntity<PaymentResponse> getPayment(@PathVariable Long invoice) {

        Payment payment = paymentService.getPayment(invoice, true);
        PaymentResponse paymentResponse = new PaymentResponse();
        return ResponseEntity.status(HttpStatus.OK)
                .body(paymentResponse.fromDomain(payment));
    }
}
