package com.eshop.services.spring;

import com.eshop.business.core.Handler;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class HandlerFactory {

    private final BeanFactory factory;

    @Autowired
    public HandlerFactory(BeanFactory factory){
        this.factory = factory;
    }


    public <T extends Handler<?, ?>> T getHandler(Class<T> clazz){
        return factory.getBean(clazz);
    }
}
