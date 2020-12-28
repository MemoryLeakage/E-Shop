package com.eshop.services.spring;

import com.eshop.business.product.handlers.AddProductHandler;
import com.eshop.business.product.handlers.AddProductImagesHandler;
import com.eshop.business.product.handlers.GetProductDetailsHandler;
import com.eshop.business.product.handlers.GetProductImageHandler;
import com.eshop.business.user.handlers.GetUserInfoHandler;
import com.eshop.repositories.*;
import com.eshop.security.SecurityContext;
import com.eshop.validators.ConstraintValidator;
import com.eshop.validators.EshopValidator;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class BeansSupplier {

    // TODO Change this value
    private final Path imagesPath = Paths.get("/tmp");

    @Bean
    @Autowired
    public AddProductHandler addProductHandler(@Qualifier("userContextProvider") SecurityContext securityContext,
                                               ProductRepository productRepository,
                                               CategoryRepository categoryRepository,
                                               ProductCategoryRepository productCategoryRepository,
                                               EshopValidator validator) {
        return new AddProductHandler(securityContext, productRepository, categoryRepository, productCategoryRepository, validator);
    }

    @Bean
    public EshopValidator getValidator(){
        return new ConstraintValidator(Validation.buildDefaultValidatorFactory().getValidator());
    }

    @Bean
    @Autowired
    public AddProductImagesHandler addProductImagesHandler(@Qualifier("keyCloakSecurityContext") SecurityContext securityContext,
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
    public GetUserInfoHandler getUserInfoHandler(@Qualifier("keyCloakSecurityContext") SecurityContext securityContext,
                                                 UserRepository userRepository) {
        return new GetUserInfoHandler(securityContext, userRepository);
    }

    @Bean
    @Autowired
    public GetProductDetailsHandler getProductDetailsHandler(ProductRepository productRepository) {
        return new GetProductDetailsHandler(productRepository);
    }
}
