package com.eshop.validators;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import java.util.Set;

public class ConstraintValidator implements EshopValidator {

    private final Validator jakartaValidator;


    public ConstraintValidator(Validator jakartaValidator){
        this.jakartaValidator = jakartaValidator;
    }

    @Override
    public <SUBJECT> void validate(SUBJECT request) {
        handleViolations(jakartaValidator.validate(request));
    }

    private <T> void handleViolations(Set<ConstraintViolation<T>> violations) {
        if(!violations.isEmpty())
            throw new ConstraintViolationException(violations);

    }

}
