package com.eshop.business.exceptions;

public class ProductNotFoundException extends EshopException{
    private static final String MESSAGE = "Unauthorized user";

    public ProductNotFoundException(){
        super(MESSAGE);
    }
}
