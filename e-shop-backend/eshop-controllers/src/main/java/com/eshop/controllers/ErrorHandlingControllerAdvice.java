package com.eshop.controllers;

import com.eshop.business.exceptions.EshopException;
import com.eshop.models.entities.User;
import com.eshop.security.SecurityContext;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


@ControllerAdvice
public class ErrorHandlingControllerAdvice extends ResponseEntityExceptionHandler {
    private static final Logger logger = LogManager.getLogger(ErrorHandlingControllerAdvice.class);
    private static final String GENERIC_ERROR_RESPONSE = "An unknown error occurred, please try again later";

    private final SecurityContext securityContext;

    @Autowired
    public ErrorHandlingControllerAdvice(@Qualifier("keyCloakSecurityContext") SecurityContext securityContext) {
        this.securityContext = securityContext;
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<?> onConstraintValidationException(ConstraintViolationException e) {
        ViolationsResponse error = new ViolationsResponse();
        for (ConstraintViolation<?> violation : e.getConstraintViolations()) {
            String field = violation.getPropertyPath().toString();
            User user = securityContext.getUser();
            String username = user != null ? user.getUsername() : "unauthenticated";
            logger.error("(user {})  (cause {} {})", username, field, violation.getMessage());
            error.getViolations().add(
                    new ViolationsResponse.Violation(field, violation.getMessage()));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }


    @ExceptionHandler(EshopException.class)
    public ResponseEntity<?> maxImagesExceptionHandler(EshopException e) {
        User user = securityContext.getUser();
        String username = user != null ? user.getUsername() : "unauthenticated";
        logger.error("(user {})  (cause {})", username, e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(e.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleExceptions(Exception e){
        logger.error("Unhandled exception", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse(GENERIC_ERROR_RESPONSE));
    }


}
