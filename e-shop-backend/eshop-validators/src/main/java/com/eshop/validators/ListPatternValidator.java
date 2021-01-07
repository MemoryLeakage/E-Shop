package com.eshop.validators;

import com.eshop.validators.annotation.ListPattern;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Collection;
import java.util.regex.Pattern;

public class ListPatternValidator implements ConstraintValidator<ListPattern, Collection<String>> {
    private Pattern pattern;
    private boolean isNullContentAllowed;

    @Override
    public void initialize(ListPattern constraintAnnotation) {
        this.pattern = Pattern.compile(constraintAnnotation.regex());
        this.isNullContentAllowed = constraintAnnotation.nullableContent();
    }

    @Override
    public boolean isValid(Collection<String> list, ConstraintValidatorContext context) {
        if (list == null) {
            return true;
        }
        for (String value : list) {
            if (value == null) {
                if (isNullContentAllowed) {
                    continue;
                } else {
                    return false;
                }
            }
            if (!pattern.matcher(value).matches()) {
                return false;
            }
        }
        return true;
    }
}
