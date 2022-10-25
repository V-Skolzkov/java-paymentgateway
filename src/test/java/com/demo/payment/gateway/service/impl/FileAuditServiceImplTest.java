package com.demo.payment.gateway.service.impl;

import com.demo.payment.gateway.domain.dto.Payment;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static com.demo.payment.gateway.util.TestUtil.buildPayment;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class FileAuditServiceImplTest {

    @TempDir
    private static Path sharedTempDir;
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private FileAuditServiceImpl auditService = new FileAuditServiceImpl();

    @Test
    void writeAuditTest() throws IOException {

        Payment payment = buildPayment(1L);

        String lineBefore = objectMapper.writeValueAsString(payment) + System.lineSeparator();

        Path audit = sharedTempDir.resolve("audit.log");
        ReflectionTestUtils.setField(auditService, "filePath", audit.toString());
        auditService.writeAudit(payment);

        String lineAfter = Files.readString(audit);
        assertEquals(lineBefore, lineAfter);
    }
}
