package com.fairsager.mahjongg.backend.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<Map<String, Object>> handleServiceException(ServiceException e) {
        return ResponseEntity.status(e.getHttpStatus()).body(Map.of(
                "message", e.getMessage(),
                "troubleshooting", e.getTroubleshooting(),
                "executorClass", e.getExecutorClass(),
                "occurrenceDate", e.getOccurrenceDate()
        ));
    }
}
