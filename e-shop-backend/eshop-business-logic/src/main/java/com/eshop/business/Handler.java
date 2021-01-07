package com.eshop.business;

public interface Handler<REQUEST, RESPONSE> {

    public RESPONSE handle(REQUEST request);
}
