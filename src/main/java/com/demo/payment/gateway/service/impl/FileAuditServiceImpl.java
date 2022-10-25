package com.demo.payment.gateway.service.impl;

import com.demo.payment.gateway.service.AuditService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.demo.payment.gateway.domain.dto.Payment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
@Slf4j
public class FileAuditServiceImpl implements AuditService {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${file.path:}")
    private String filePath;

    private AtomicBoolean isCreateCalled;

    @Override
    public void writeAudit(Payment payment) {
        try {
            String line = objectMapper.writeValueAsString(payment) + System.lineSeparator();
            Files.writeString(
                    Paths.get(filePath),
                    line,
                    StandardOpenOption.APPEND);
        } catch (IOException ex) {
            LOG.warn("Non critical exception occurred. Cause : {}", ex.getMessage(), ex);
            if (ex instanceof NoSuchFileException) {
                createFile(payment);
            }
        }
    }

    private synchronized void createFile(Payment payment) {
        if (Objects.nonNull(isCreateCalled)) {
            return;
        }
        isCreateCalled = new AtomicBoolean(true);
        try {
            Files.createFile(Paths.get(filePath));
            writeAudit(payment);
        } catch (IOException ex) {
            LOG.warn("Non critical exception occurred. Cause : {}", ex.getMessage(), ex);
        }
    }
}
