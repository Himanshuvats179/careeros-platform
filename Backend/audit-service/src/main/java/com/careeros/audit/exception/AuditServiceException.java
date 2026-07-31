package com.careeros.audit.exception;

public class AuditServiceException extends RuntimeException {
    public AuditServiceException(String message) {
        super(message);
    }
    public AuditServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
