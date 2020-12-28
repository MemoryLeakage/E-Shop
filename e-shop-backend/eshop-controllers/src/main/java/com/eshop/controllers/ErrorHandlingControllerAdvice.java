package com.eshop.controllers;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


@ControllerAdvice
public class ErrorHandlingControllerAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ConstraintViolationException.class)
    public  ResponseEntity<?> onConstraintValidationException(ConstraintViolationException e) {
        ViolationsResponse error = new ViolationsResponse();
        for (ConstraintViolation<?> violation : e.getConstraintViolations()) {
            String field = violation.getPropertyPath().toString();
            error.getViolations().add(
                    new ViolationsResponse.Violation(field, violation.getMessage()));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
}
