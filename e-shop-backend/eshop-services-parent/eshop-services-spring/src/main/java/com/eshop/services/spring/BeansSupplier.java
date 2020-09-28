package com.eshop.services.spring;

import com.eshop.business.product.handlers.AddProductHandler;
import com.eshop.business.product.handlers.AddProductImagesHandler;
import com.eshop.business.product.handlers.GetProductImageHandler;
import com.eshop.business.user.handlers.GetUserInfoHandler;
import com.eshop.repositories.ImageRepository;
import com.eshop.repositories.ProductRepository;
import com.eshop.repositories.UserRepository;
import com.eshop.security.SecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class BeansSupplier {

    private final Path imagesPath = Paths.get("/images");

    @Bean
    @Autowired
    public AddProductHandler addProductHandler(SecurityContext securityContext,
                                               ProductRepository productRepository) {
        return new AddProductHandler(securityContext, productRepository);
    }

    @Bean
    @Autowired
    public AddProductImagesHandler addProductImagesHandler(SecurityContext securityContext,
                                                           ProductRepository productRepository,
                                                           ImageRepository imageRepository) {
        return new AddProductImagesHandler(securityContext, productRepository, imageRepository, imagesPath);
    }

    @Bean
    @Autowired
    public GetProductImageHandler getProductImageHandler(ImageRepository imageRepository) {
        return new GetProductImageHandler(imageRepository);
    }

    @Bean
    @Autowired
    public GetUserInfoHandler getUserInfoHandler(SecurityContext securityContext,
                                                 UserRepository userRepository) {
        return new GetUserInfoHandler(securityContext, userRepository);
    }

}
