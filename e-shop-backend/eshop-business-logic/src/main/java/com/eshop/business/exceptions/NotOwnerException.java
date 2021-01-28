package com.eshop.business.exceptions;

public class NotOwnerException extends EshopException{
    private static final String MESSAGE = "Unauthorized user";

    public NotOwnerException(){
        super(MESSAGE);
    }

}
