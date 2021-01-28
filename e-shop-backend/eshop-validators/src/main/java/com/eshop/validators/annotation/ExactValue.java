package com.eshop.validators.annotation;

import com.eshop.validators.ExactValueValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({FIELD})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = {ExactValueValidator.class})
public @interface ExactValue {
    String message() default "invalid string value";

    String[] values();

    boolean nullableContent() default false;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
