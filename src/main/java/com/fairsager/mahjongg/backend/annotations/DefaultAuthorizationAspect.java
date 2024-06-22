package com.fairsager.mahjongg.backend.annotations;

import com.fairsager.mahjongg.backend.database.entity.Session;
import com.fairsager.mahjongg.backend.database.entity.User;
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

import java.lang.reflect.Field;

@Aspect
@Component
public class DefaultAuthorizationAspect {

    private static final Logger LOG = LoggerFactory.getLogger(DefaultAuthorizationAspect.class);

    @Autowired
    private HttpServletRequest request;

    @Before("@annotation(defaultAuthorization)")
    public void checkRequiredRight(JoinPoint joinPoint, DefaultAuthorization defaultAuthorization) {
        Right requiredRight = defaultAuthorization.right();
        Session session = (Session) request.getAttribute("session");

        switch (requiredRight) {
            case DEV:
                if (session.getRole() != User.Role.DEV)
                    throw new ServiceException(HttpStatus.FORBIDDEN, "Developer rights required", "Contact a developer for help", getClass());
                break;
            case OWN:
                if (!session.getUsername().equals(extractUsernameFromRequest(joinPoint)))
                    throw new ServiceException(HttpStatus.FORBIDDEN, "You can only edit your own data", getClass());
                break;
        }
    }

    private String extractUsernameFromRequest(JoinPoint joinPoint) {
        String paramUsername = request.getParameter("username");
        if (paramUsername != null) {
            return paramUsername;
        }

        for (Object arg : joinPoint.getArgs()) {
            if (arg != null) {
                try {
                    Field field = arg.getClass().getDeclaredField("username");
                    field.setAccessible(true);
                    return (String) field.get(arg);
                } catch (NoSuchFieldException | IllegalAccessException ignored) {
                }
            }
        }
        return null;
    }
}
