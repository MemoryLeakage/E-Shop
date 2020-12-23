package com.eshop.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Path;
import java.util.Iterator;

@ControllerAdvice
public class ErrorHandlingControllerAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ConstraintViolationException.class)
    public  ResponseEntity<?> onConstraintValidationException(
            ConstraintViolationException e) {
        ViolationsResponse error = new ViolationsResponse();
        for (ConstraintViolation<?> violation : e.getConstraintViolations()) {
            Iterator<Path.Node> iterator = violation.getPropertyPath().iterator();
            String param = "";
            while (iterator.hasNext()){
                param = iterator.next().getName();
            }

            error.getViolations().add(
                    new ViolationsResponse.Violation(param, violation.getMessage()));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
}
