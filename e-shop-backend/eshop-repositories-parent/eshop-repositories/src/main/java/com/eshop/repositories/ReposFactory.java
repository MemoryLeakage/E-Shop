package com.eshop.repositories;

public interface ReposFactory {

    <T extends EshopRepository> T getRepository(Class<T> clazz);

}
