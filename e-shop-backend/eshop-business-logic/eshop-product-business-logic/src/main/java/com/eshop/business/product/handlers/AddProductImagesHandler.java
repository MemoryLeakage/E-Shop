package com.eshop.business.product.handlers;

import com.eshop.business.product.requests.AddProductImagesRequest;
import com.eshop.business.product.responses.AddProductImagesResponse;
import com.eshop.models.entities.User;
import com.eshop.repositories.ProductRepository;
import com.eshop.security.SecurityContext;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static com.eshop.business.product.handlers.Validators.validateNotNull;

public class AddProductImagesHandler {

    private ProductRepository productRepository;
    private SecurityContext securityContext;
    private Path imagesPath;

    public AddProductImagesHandler( SecurityContext securityContext,
                                    ProductRepository productRepository,
                                    Path imagesPath) {
        validateNotNull(securityContext, "security context");
        validateNotNull(productRepository, "product repository");
        validateNotNull(imagesPath, "images path");
        this.imagesPath = imagesPath;
        createFolderIfNotExist(imagesPath);
        this.productRepository = productRepository;
        this.securityContext = securityContext;
    }

    private void createFolderIfNotExist(Path imagesPath) {
        if(!Files.exists(imagesPath)){
            try {
                Files.createDirectories(imagesPath);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public AddProductImagesResponse handle(AddProductImagesRequest request){
        validateNotNull(request, "request");
        validateNotNull(request.getImages(), "images");
        User owner = productRepository.getOwnerById(request.getProductId());

        if(!owner.equals(securityContext.getUser())){
            throw new IllegalStateException("not the owner");
        }

        List<AddProductImagesRequest.Image> images = request.getImages();
        Path productImagesPath = imagesPath.resolve(Long.toString(request.getProductId()));
        try {
            Files.createDirectories(productImagesPath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        for (int i=0; i< images.size(); i++) {
            AddProductImagesRequest.Image image = images.get(i);
            validateNotNull(image, "image");
            addImage(productImagesPath, i, image);
        }
        productRepository.updateImageUrlById(request.getProductId(), productImagesPath.toAbsolutePath().toString());
        return new AddProductImagesResponse(images.size(),
                productRepository.getProductNameById(request.getProductId()),
                String.format("%s %s",owner.getFirstName(), owner.getLastName()));
    }

    private void addImage(Path productImagesPath, int i, AddProductImagesRequest.Image image) {
        //TODO validate image types.
        String extension = String.format("%s.%s",
                i,
                image.getImageType());
        Path path = productImagesPath.resolve(extension);
        try(OutputStream writer = Files.newOutputStream(path)) {
            writer.write(image.getImageBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
