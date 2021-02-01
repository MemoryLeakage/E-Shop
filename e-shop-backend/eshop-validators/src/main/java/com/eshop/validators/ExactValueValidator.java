package com.eshop.validators;

import com.eshop.validators.annotation.ExactValue;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ExactValueValidator implements ConstraintValidator<ExactValue, String> {

    private String[] values;

    @Override
    public void initialize(ExactValue constraintAnnotation) {
        this.values = constraintAnnotation.values();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if(value == null)
            return true;
        for (String validValue : values) {
            if(value.equals(validValue))
                return true;
        }
        return false;
    }
}
