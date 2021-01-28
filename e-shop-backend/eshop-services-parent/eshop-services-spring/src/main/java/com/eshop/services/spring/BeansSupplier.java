package com.eshop.services.spring;

import com.eshop.business.cart.handlers.AddToCartHandler;
import com.eshop.business.product.handlers.*;
import com.eshop.business.user.handlers.GetUserInfoHandler;
import com.eshop.repositories.*;
import com.eshop.security.SecurityContext;
import com.eshop.validators.ConstraintValidator;
import com.eshop.validators.EshopValidator;
import jakarta.validation.Validation;
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
                                               ReposFactory reposFactory,
                                               EshopValidator validator) {
        return new AddProductHandler(securityContext, reposFactory, validator);
    }

    @Bean
    public EshopValidator getValidator() {
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
    public GetProductDetailsHandler getProductDetailsHandler(ReposFactory reposFactory, EshopValidator validator) {
        return new GetProductDetailsHandler(reposFactory, validator);
    }

    @Bean
    @Autowired
    public DeleteImageHandler getDeleteImageHandler(@Qualifier("keyCloakSecurityContext") SecurityContext securityContext,
                                                    ReposFactory reposFactory,
                                                    EshopValidator validator) {
        return new DeleteImageHandler(securityContext, reposFactory, imagesPath, validator);
    }

    @Bean
    @Autowired
    public AddToCartHandler getAddToCartHandler(@Qualifier("userContextProvider") SecurityContext securityContext,
                                                ReposFactory reposFactory,
                                                EshopValidator validator) {
        return new AddToCartHandler(securityContext, reposFactory, validator);
    }
}
