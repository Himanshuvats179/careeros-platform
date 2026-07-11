package com.careeros.common.dto;

import java.time.LocalDateTime;
import java.util.Map;

public record ErrorResponse(
        boolean success,
        String message,
        int status,
        Map<String, String> errors,
        LocalDateTime timestamp
) {
    public static ErrorResponse of(
            String message,
            int status,
            Map<String, String> errors
    ) {
        return new ErrorResponse(
                false,
                message,
                status,
                errors,
                LocalDateTime.now()
        );
    }
}