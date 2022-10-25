package com.demo.payment.gateway.api.advice;

import com.demo.payment.gateway.exception.BadRequestException;
import com.demo.payment.gateway.exception.PaymentAlreadyExistsException;
import com.demo.payment.gateway.exception.PaymentGatewayGeneralException;
import com.demo.payment.gateway.exception.PaymentNotFoundException;
import com.demo.payment.gateway.api.PaymentGatewayController;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Map;
import java.util.Objects;

import static com.demo.payment.gateway.common.Constants.GENERAL_DESCRIPTION;
import static com.demo.payment.gateway.util.GeneralUtil.buildResponse;

@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice(assignableTypes = {PaymentGatewayController.class})
public class PaymentGatewayControllerAdvice {

    @ExceptionHandler
    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @Hidden
    String notFound(PaymentNotFoundException exception) {
        return processException(exception);
    }

    @ExceptionHandler
    @ResponseBody
    @ResponseStatus(HttpStatus.CONFLICT)
    @Hidden
    String paymentExists(PaymentAlreadyExistsException exception) {
        return processException(exception);
    }

    @ExceptionHandler
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @Hidden
    String badRequest(BadRequestException exception) {
        return processException(exception);
    }

    @ExceptionHandler
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    String generalException(Exception exception) {
        return processGeneralException(exception);
    }

    private String processException(PaymentGatewayGeneralException exception) {
        LOG.error(exception.getMessage());
        Map<String, String> errors = exception.getErrors();
        return Objects.nonNull(errors) ? buildResponse(errors) : buildResponse(Map.of(GENERAL_DESCRIPTION, exception.getMessage()));
    }

    private String processGeneralException(Exception exception) {
        LOG.error(exception.getMessage());
        return buildResponse(Map.of(GENERAL_DESCRIPTION, exception.getMessage()));
    }
}
