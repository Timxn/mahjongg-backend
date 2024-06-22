package com.fairsager.mahjongg.backend.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
public class ServiceException extends RuntimeException {

    private HttpStatus httpStatus;
    private String message;
    private String troubleshooting;
    private String executorClass;
    private Date occurrenceDate;

    public ServiceException(HttpStatus httpStatus, String message, String troubleshooting, Class<?> executorClass) {
        this.httpStatus = httpStatus;
        this.message = message;
        this.troubleshooting = troubleshooting;
        this.executorClass = executorClass.getName();
        this.occurrenceDate = new Date();
    }

    public ServiceException(HttpStatus httpStatus, String message, Class<?> executorClass) {
        this.httpStatus = httpStatus;
        this.message = message;
        this.executorClass = executorClass.getName();
        this.occurrenceDate = new Date();
    }
}
