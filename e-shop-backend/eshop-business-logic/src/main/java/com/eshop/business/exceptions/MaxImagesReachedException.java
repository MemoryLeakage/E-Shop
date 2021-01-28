package com.eshop.business.exceptions;

public class MaxImagesReachedException extends EshopException{

    private static final String MESSAGE = "image cap is reached";

    public MaxImagesReachedException(){
        super(MESSAGE);
    }

}
