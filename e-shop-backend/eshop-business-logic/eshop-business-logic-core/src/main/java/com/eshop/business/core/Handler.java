package com.eshop.business.core;

public interface Handler<REQUEST, RESPONSE> {

    public RESPONSE handle(REQUEST request);
}
