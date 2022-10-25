package com.demo.payment.gateway.api.validate;

import org.springframework.validation.Errors;

import java.time.LocalDate;
import java.util.Objects;
import java.util.regex.Pattern;

public final class ValidationUtil {

    private static final String INVALID_VALUE = "Invalid value for parameter %s, value = {%s}, %s";
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$");
    private static final Pattern PAN_PATTERN = Pattern.compile("^\\d{16}$");
    private static final Pattern EXPIRY_DATE_PATTERN = Pattern.compile("^\\d{4}$");

    private ValidationUtil() {
    }

    public static boolean isNull(Object value, String fieldName, String jsonPropertyName, Errors errors) {
        boolean result = false;
        if (Objects.isNull(value)) {
            errors.rejectValue(fieldName, "empty", new String[]{jsonPropertyName}, String.format(INVALID_VALUE, jsonPropertyName, value, "value can't be empty."));
            result = true;
        }
        return result;
    }

    public static void isPositiveInt(Integer value, String fieldName, String jsonPropertyName, Errors errors) {
        if (!isNull(value, fieldName, jsonPropertyName, errors)) {
            if (value <= 0) {
                errors.rejectValue(fieldName, "negative or zero", new String[]{jsonPropertyName}, String.format(INVALID_VALUE, jsonPropertyName, value, "value can't be negative or zero."));
            }
        }
    }

    public static void checkEmail(String value, String fieldName, String jsonPropertyName, Errors errors) {
        if (!isNull(value, fieldName, jsonPropertyName, errors)) {
            if (!EMAIL_PATTERN.matcher(value).matches()) {
                errors.rejectValue(fieldName, "Invalid email format", new String[]{jsonPropertyName}, String.format(INVALID_VALUE, jsonPropertyName, value, "invalid email format."));
            }
        }
    }

    public static void checkPan(String value, String fieldName, String jsonPropertyName, Errors errors) {
        if (!isNull(value, fieldName, jsonPropertyName, errors)) {
            if (!PAN_PATTERN.matcher(value).matches() || !isValidLuhn(value)) {
                errors.rejectValue(fieldName, "Invalid pan", new String[]{jsonPropertyName}, String.format(INVALID_VALUE, jsonPropertyName, value, "invalid pan."));
            }
        }
    }

    public static void checkExpiryDate(String value, String fieldName, String jsonPropertyName, Errors errors) {
        if (!isNull(value, fieldName, jsonPropertyName, errors)) {
            if (!EXPIRY_DATE_PATTERN.matcher(value).matches()) {
                errors.rejectValue(fieldName, "Invalid expiry date", new String[]{jsonPropertyName}, String.format(INVALID_VALUE, jsonPropertyName, value, "invalid expiry date."));
            } else {
                Pair<Boolean, String> pair = isValidExpiryDate(value);
                if (!pair.getFirst()) {
                    errors.rejectValue(fieldName, "Invalid expiry date", new String[]{jsonPropertyName}, String.format(INVALID_VALUE, jsonPropertyName, value, pair.getSecond()));

                }
            }
        }
    }

    private static Pair<Boolean, String> isValidExpiryDate(String value) {

        int month = Integer.parseInt(value.substring(0, 2));
        int year = Integer.parseInt(value.substring(2)) + 2000;
        if (month > 0 && month < 13) {
            LocalDate now = LocalDate.now();
            return now.minusDays(1).isBefore(LocalDate.of(year, month, now.getDayOfMonth()))
                    ? Pair.of(true, "") : Pair.of(false, "expiry date in past.");
        } else {
            return Pair.of(false, "invalid expiry date.");
        }
    }

    // from wikipedia
    private static boolean isValidLuhn(String value) {
        int sum = Character.getNumericValue(value.charAt(value.length() - 1));
        int parity = value.length() % 2;
        for (int i = value.length() - 2; i >= 0; i--) {
            int summand = Character.getNumericValue(value.charAt(i));
            if (i % 2 == parity) {
                int product = summand * 2;
                summand = (product > 9) ? (product - 9) : product;
            }
            sum += summand;
        }
        return (sum % 10) == 0;
    }

}
