package com.fairsager.mahjongg.backend.annotations;

import com.fairsager.mahjongg.backend.database.entity.Session;
import com.fairsager.mahjongg.backend.database.repository.SessionRepository;
import com.fairsager.mahjongg.backend.exception.ServiceException;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.*;

@Aspect
@Component
public class DefaultAuthenticationAspect {

    private static final Logger LOG = LoggerFactory.getLogger(DefaultAuthenticationAspect.class);

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private HttpServletRequest request;

    @Before("@annotation(defaultAuthentication)")
    public void validateSession(JoinPoint joinPoint, DefaultAuthentication defaultAuthentication) {
        if (request == null) {
            LOG.error("Failed to validate Controller Session call. No Http servlet request provided.");
            return;
        }

        UUID sessionId = null;
        if (request.getCookies() != null) {
            sessionId = Arrays.stream(request.getCookies())
                    .filter(cookie -> "sessionId".equals(cookie.getName()))
                    .findFirst()
                    .map(cookie -> {
                        try {
                            return UUID.fromString(cookie.getValue());
                        } catch (IllegalArgumentException e) {
                            LOG.warn("Invalid session ID format in cookie", e);
                            return null;
                        }
                    })
                    .orElse(null);
        }

        if (sessionId == null)
            throw new ServiceException(HttpStatus.UNAUTHORIZED, "Session ID not found in cookies", "Make sure your browser accepts cookies", getClass());

        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new ServiceException(HttpStatus.NOT_FOUND, "Session not found", "Try logging out and back in", getClass()));

        if (!session.isActive())
            throw new ServiceException(HttpStatus.FORBIDDEN, "Session is no longer valid", "Try logging out and back in", getClass());

        session.setLastSeen(new Date());
        sessionRepository.save(session);
        request.setAttribute("session", session);
    }
}
