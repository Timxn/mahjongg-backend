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

    public static void validateUsername(String username, Class<?> clazz) {
        validateString(username, "Username is required", clazz);
        if (username.length() < 3 || username.length() > 20) {
            throw new ServiceException(HttpStatus.BAD_REQUEST, "Username must be between 3 and 20 characters", clazz);
        }
    }
}
