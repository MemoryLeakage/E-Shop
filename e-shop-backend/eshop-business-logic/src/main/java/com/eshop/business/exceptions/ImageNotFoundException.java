package com.eshop.business.exceptions;

public class ImageNotFoundException extends EshopException{
    private static final String MESSAGE = "image was not found";

    public ImageNotFoundException() {
        super(MESSAGE);
    }
}
