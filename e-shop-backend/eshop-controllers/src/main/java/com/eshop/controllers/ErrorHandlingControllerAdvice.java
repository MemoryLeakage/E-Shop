package com.eshop.controllers;

import com.eshop.models.entities.User;
import com.eshop.security.SecurityContext;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;


@ControllerAdvice
public class ErrorHandlingControllerAdvice extends ResponseEntityExceptionHandler {
    private static final Logger logger = LogManager.getLogger(ErrorHandlingControllerAdvice.class);

    private final HttpServletRequest request;
    private final SecurityContext securityContext;

    @Autowired
    public ErrorHandlingControllerAdvice(HttpServletRequest request, @Qualifier("keyCloakSecurityContext") SecurityContext securityContext) {
        this.request = request;
        this.securityContext = securityContext;
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<?> onConstraintValidationException(ConstraintViolationException e) {
        ViolationsResponse error = new ViolationsResponse();
        for (ConstraintViolation<?> violation : e.getConstraintViolations()) {
            String field = violation.getPropertyPath().toString();
            User user = securityContext.getUser();
            String username = user != null ? user.getUsername() : "unauthenticated";
            logger.error("{} {} {} {}", request.getMethod(), request.getRequestURI(), request.getRemoteAddr(),
                    username);
            error.getViolations().add(
                    new ViolationsResponse.Violation(field, violation.getMessage()));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
}
