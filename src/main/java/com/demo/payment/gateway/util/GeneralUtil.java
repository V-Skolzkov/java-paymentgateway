package com.demo.payment.gateway.util;

import java.util.Map;

public final class GeneralUtil {

    private static final String errorResponseTemplate = "{\"approved\": false,\"errors\": {%s}}";

    private GeneralUtil() {
    }

    public static String buildResponse(Map<String, String> errors) {
        StringBuilder sb = new StringBuilder();
        errors.forEach((k, v) -> {
            sb.append("\"").append(k).append("\"").append(":").append("\"").append(v).append("\",");
        });
        return String.format(errorResponseTemplate, sb.deleteCharAt(sb.length() - 1));
    }
}
