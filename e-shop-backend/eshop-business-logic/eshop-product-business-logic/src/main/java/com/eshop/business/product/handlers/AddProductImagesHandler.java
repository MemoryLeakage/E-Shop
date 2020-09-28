package com.eshop.business.product.handlers;

import com.eshop.business.product.requests.AddProductImagesRequest;
import com.eshop.business.product.responses.AddProductImagesResponse;
import com.eshop.models.entities.Image;
import com.eshop.models.entities.Product;
import com.eshop.models.entities.User;
import com.eshop.repositories.ImageRepository;
import com.eshop.repositories.ProductRepository;
import com.eshop.security.SecurityContext;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;

import static com.eshop.utilities.Validators.validateNotNullArgument;

public class AddProductImagesHandler {

    private final ProductRepository productRepository;
    private final ImageRepository imageRepository;
    private final SecurityContext securityContext;
    private final Path imagesPath;
    //    TODO this number should be configurable
    private final int maxNumberOfImagesPerProduct = 8;

    public AddProductImagesHandler(SecurityContext securityContext,
                                   ProductRepository productRepository,
                                   ImageRepository imageRepository,
                                   Path imagesPath) {
        validateNotNullArgument(securityContext, "security context");
        validateNotNullArgument(productRepository, "product repository");
        validateNotNullArgument(imageRepository, "image repository");
        validateNotNullArgument(imagesPath, "images path");
        this.imagesPath = imagesPath;
        this.productRepository = productRepository;
        this.imageRepository = imageRepository;
        this.securityContext = securityContext;
        createFolderIfNotExist(imagesPath);
    }

    private void createFolderIfNotExist(Path imagesPath) {
        if (!Files.exists(imagesPath)) {
            try {
                Files.createDirectories(imagesPath);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public AddProductImagesResponse handle(AddProductImagesRequest request) {
        validateNotNullArgument(request, "request");
        validateNotNullArgument(request.getImages(), "images");
        Product product = productRepository.getProductById(request.getProductId());

        if(product == null){
            throw new IllegalStateException("productid="+ request.getProductId() + " does not exist in the database");
        }

        User owner = product.getOwner();
        if (!owner.equals(securityContext.getUser())) {
            throw new IllegalStateException("not the owner");
        }

        int numberOfImages = imageRepository.getImagesCountByProductId(request.getProductId());
        if (request.getImages().size() + numberOfImages > maxNumberOfImagesPerProduct) {
            throw new IllegalArgumentException("max number of images exceeded");
        }

        Path productImagesPath = imagesPath.resolve(request.getProductId());
        createFolderIfNotExist(productImagesPath);

        List<AddProductImagesRequest.Image> images = request.getImages();
        for (AddProductImagesRequest.Image image : images) {
            validateNotNullArgument(image, "image");
            addImage(productImagesPath, image, product);
        }

        return new AddProductImagesResponse(images.size(),
                product.getProductName(),
                owner.getFirstName() + " " + owner.getLastName());
    }


    private void addImage(Path productImagesPath, AddProductImagesRequest.Image imageDetails, Product product) {
        //TODO validate imageDetails types.
        String imageName = UUID.randomUUID() + "." + imageDetails.getImageType();
        byte[] imageBytes = imageDetails.getImageBytes();
        Path imagePath = productImagesPath.resolve(imageName).toAbsolutePath();
        Image image = new Image.Builder()
                .name(imageName)
                .path(imagePath.toString())
                .product(product)
                .size(imageBytes.length)
                .build();

        imageRepository.addImage(image);
        writeImageToDisk(imagePath, imageBytes);
    }

    private void writeImageToDisk(Path imagePath, byte[] imageBytes) {
        try (OutputStream writer = Files.newOutputStream(imagePath)) {
            writer.write(imageBytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
