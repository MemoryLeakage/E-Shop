package com.eshop.business.product.handlers;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;

public class Validators {
    public static void validateMoreThanZero(double number, String name) {
        String message = name + " has to be greater than 0";
        validate(n -> n <= 0,
                IllegalArgumentException::new,
                message,
                number);
    }

    public static void validateNotNull(Object object, String name) {
        String message = name + " can not be null";
        validate(Objects::isNull,
                NullPointerException::new,
                message,
                object);
    }

    public static  <T> void validate(Predicate<T> predicate,
                              Function<String, RuntimeException> exceptionSupplier,
                              String message, T testSubject) {
        if (predicate.test(testSubject))
            throw exceptionSupplier.apply(message);

    }

}
