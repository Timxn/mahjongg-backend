package com.fairsager.mahjongg.backend.utils;

import com.fairsager.mahjongg.backend.exception.ServiceException;
import org.springframework.http.HttpStatus;

import java.util.UUID;

public class Validator {
    public static void validateString(String value, String errorMessage, Class<?> clazz) {
        if (value == null || value.trim().isBlank()) {
            throw new ServiceException(HttpStatus.BAD_REQUEST, errorMessage, clazz);
        }
    }

    public static void validateUUID(UUID value, String errorMessage, Class<?> clazz) {
        if (value == null) {
            throw new ServiceException(HttpStatus.BAD_REQUEST, errorMessage, clazz);
        }
    }
}
