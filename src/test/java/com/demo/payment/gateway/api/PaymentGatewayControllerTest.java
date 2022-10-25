package com.demo.payment.gateway.api;

import com.demo.payment.gateway.api.advice.PaymentGatewayControllerAdvice;
import com.demo.payment.gateway.api.dto.request.CardRequest;
import com.demo.payment.gateway.api.dto.request.CardholderRequest;
import com.demo.payment.gateway.api.dto.request.PaymentRequest;
import com.demo.payment.gateway.domain.dto.Payment;
import com.demo.payment.gateway.exception.PaymentAlreadyExistsException;
import com.demo.payment.gateway.exception.PaymentNotFoundException;
import com.demo.payment.gateway.service.PaymentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = PaymentGatewayController.class)
public class PaymentGatewayControllerTest {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PaymentService paymentService;

    @InjectMocks
    private PaymentGatewayController paymentGatewayController;

    @BeforeEach
    void beforeEach() {
        paymentGatewayController = new PaymentGatewayController(paymentService);
        mockMvc = MockMvcBuilders
                .standaloneSetup(paymentGatewayController)
                .setControllerAdvice(new PaymentGatewayControllerAdvice())
                .build();
    }

    @Test
    void submitPaymentReturn200Test() throws Exception {

        PaymentRequest paymentRequest = buildPaymentRequest();
        doNothing().when(paymentService).submitPayment(any());
        mockMvc.perform(
                        post("/v1/gateway/payment")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(paymentRequest)
                                ))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.approved").value(true));
    }

    @Test
    void submitPaymentReturn500Test() throws Exception {

        PaymentRequest paymentRequest = buildPaymentRequest();
        doThrow(new RuntimeException("Test exception")).when(paymentService).submitPayment(any());
        mockMvc.perform(
                        post("/v1/gateway/payment")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(paymentRequest)
                                ))
                .andDo(print())
                .andExpect(status().is5xxServerError())
                .andExpect(jsonPath("$.approved").value(false))
                .andExpect(jsonPath("$.errors.generalDescription").value("Test exception"));
    }

    @Test
    void submitPaymentReturn400ValidationErrorTest() throws Exception {

        PaymentRequest paymentRequest = buildPaymentRequest();
        paymentRequest.getCardholderRequest().setEmail("email");
        paymentRequest.getCardRequest().setExpiry("0121");
        paymentRequest.getCardRequest().setPan("4200000000000001");
        paymentRequest.setAmount(null);
        mockMvc.perform(
                        post("/v1/gateway/payment")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(paymentRequest)
                                ))
                .andDo(print())
                .andExpect(status().is(400))
                .andExpect(jsonPath("$.approved").value(false))
                .andExpect(jsonPath("$.errors.email").value("Invalid value for parameter email, value = {email}, invalid email format."))
                .andExpect(jsonPath("$.errors.pan").value("Invalid value for parameter pan, value = {4200000000000001}, invalid pan."))
                .andExpect(jsonPath("$.errors.expiry").value("Invalid value for parameter expiry, value = {0121}, expiry date in past."))
                .andExpect(jsonPath("$.errors.amount").value("Invalid value for parameter amount, value = {null}, value can't be empty."));
    }

    @Test
    void submitPaymentReturn409PaymentAlreadyExistsExceptionTest() throws Exception {

        PaymentRequest paymentRequest = buildPaymentRequest();
        Mockito.doThrow(new PaymentAlreadyExistsException("Payment with invoice 1 already exists!")).when(paymentService).submitPayment(any());
        mockMvc.perform(
                        post("/v1/gateway/payment")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(paymentRequest)
                                ))
                .andDo(print())
                .andExpect(status().is(409))
                .andExpect(jsonPath("$.approved").value(false))
                .andExpect(jsonPath("$.errors.generalDescription").value("Payment with invoice 1 already exists!"));
    }

    @Test
    void getPaymentReturn200Test() throws Exception {

        PaymentRequest paymentRequest = buildPaymentRequest();
        Payment payment = paymentRequest.toDomain();
        when(paymentService.getPayment(1L, true)).thenReturn(payment);
        mockMvc.perform(
                        get("/v1/gateway/payment/get/1")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.invoice").value(1));
    }

    @Test
    void getPaymentReturn404Test() throws Exception {

        when(paymentService.getPayment(1L, true)).thenThrow(new PaymentNotFoundException("Payment with invoice 1 not found!"));
        mockMvc.perform(
                        get("/v1/gateway/payment/get/1")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().is(404))
                .andExpect(jsonPath("$.approved").value(false))
                .andExpect(jsonPath("$.errors.generalDescription").value("Payment with invoice 1 not found!"));
    }

    private PaymentRequest buildPaymentRequest() {

        return PaymentRequest.builder()
                .invoice(1L)
                .amount(100)
                .currency("EUR")
                .cardholderRequest(CardholderRequest.builder()
                        .name("First Last")
                        .email("email@domain.com")
                        .build())
                .cardRequest(CardRequest.builder()
                        .pan("5375414109569904")
                        .expiry("1022")
                        .cvv("789")
                        .build())
                .build();
    }
}
