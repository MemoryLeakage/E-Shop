package com.eshop.validators.annotation;


import com.eshop.validators.ListPatternValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({FIELD})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = {ListPatternValidator.class})
public @interface ListPattern {
    String message() default "invalid value";

    String regex();

    boolean nullableContent() default false;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};


}
