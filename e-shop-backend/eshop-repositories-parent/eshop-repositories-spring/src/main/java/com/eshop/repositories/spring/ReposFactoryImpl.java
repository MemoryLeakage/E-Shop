package com.eshop.repositories.spring;

import com.eshop.repositories.EshopRepository;
import com.eshop.repositories.ReposFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class ReposFactoryImpl implements ReposFactory {
    private final BeanFactory factory;

    @Autowired
    public ReposFactoryImpl(BeanFactory factory) {
        this.factory = factory;
    }

    @Override
    public <T extends EshopRepository> T getRepository(Class<T> clazz) {
        return factory.getBean(clazz);
    }
}
