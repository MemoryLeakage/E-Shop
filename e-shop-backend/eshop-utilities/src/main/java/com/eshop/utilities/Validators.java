package com.eshop.utilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;

public class Validators {

    private static final Logger logger = LoggerFactory.getLogger(Validators.class);

    public static void validateMoreThanZero(double number, String name) {
        String message = name + " has to be greater than 0";
        validate(n -> n <= 0,
                IllegalArgumentException::new,
                message,
                number);
    }


    public static void validateNotNullArgument(Object object, String argumentName) {
        String message = argumentName + " can not be null";
        validate(Objects::isNull,
                IllegalArgumentException::new,
                message,
                object);
    }

    public static  <T> void validate(Predicate<T> predicate,
                                     Function<String, RuntimeException> exceptionFunction,
                                     String message,
                                     T testSubject) {
        if (predicate.test(testSubject)) {
            logger.error(message);
            throw exceptionFunction.apply(message);
        }

    }
}
