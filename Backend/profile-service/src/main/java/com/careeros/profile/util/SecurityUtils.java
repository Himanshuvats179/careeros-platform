package com.careeros.profile.util;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.UUID;

public class SecurityUtils {

    public static final String X_USER_ID_HEADER = "X-User-Id";
    public static final String X_CORRELATION_ID_HEADER = "X-Correlation-Id";

    public static UUID getCurrentUserId() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            String userIdStr = request.getHeader(X_USER_ID_HEADER);
            if (userIdStr != null && !userIdStr.isBlank()) {
                try {
                    return UUID.fromString(userIdStr);
                } catch (IllegalArgumentException e) {
                    // Fallback to random or throw
                }
            }
        }
        return UUID.fromString("00000000-0000-0000-0000-000000000001");
    }

    public static String getCorrelationId() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            String correlationId = request.getHeader(X_CORRELATION_ID_HEADER);
            if (correlationId != null && !correlationId.isBlank()) {
                return correlationId;
            }
        }
        return UUID.randomUUID().toString();
    }
}
