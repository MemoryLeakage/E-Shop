package com.eshop.business.product.handlers;

import com.eshop.business.product.requests.AddProductImagesRequest;
import com.eshop.business.product.responses.AddProductImagesResponse;
import com.eshop.models.entities.Image;
import com.eshop.models.entities.Product;
import com.eshop.models.entities.User;
import com.eshop.repositories.ImageRepository;
import com.eshop.repositories.ProductRepository;
import com.eshop.security.SecurityContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;

import static com.eshop.business.product.handlers.Validators.validateNotNull;

public class AddProductImagesHandler {

    private static final Logger logger = LoggerFactory.getLogger(AddProductImagesHandler.class);

    private ProductRepository productRepository;
    private ImageRepository imageRepository;
    private SecurityContext securityContext;
    private Path imagesPath;
    //    TODO this number should be configurable
    private final int maxNumberOfImagesPerProduct = 8;

    public AddProductImagesHandler(SecurityContext securityContext,
                                   ProductRepository productRepository,
                                   ImageRepository imageRepository,
                                   Path imagesPath) {
        logger.debug("Constructing AddProductImagesHandler");
        validateNotNull(securityContext, "security context");
        validateNotNull(productRepository, "product repository");
        validateNotNull(imageRepository, "image repository");
        validateNotNull(imagesPath, "images path");
        this.imagesPath = imagesPath;
        createFolderIfNotExist(imagesPath);
        this.productRepository = productRepository;
        this.imageRepository = imageRepository;
        this.securityContext = securityContext;
        logger.debug("AddProductImagesHandler constructed successfully.");
    }

    private void createFolderIfNotExist(Path imagesPath) {
        logger.debug("checking if {} exists for saving images",
                imagesPath.toAbsolutePath().toString());
        if (!Files.exists(imagesPath)) {
            logger.debug("Attempting to create the following directories {} for saving images",
                    imagesPath.toAbsolutePath().toString());
            try {
                Files.createDirectories(imagesPath);
            } catch (IOException e) {
                logger.error("Error creating directories in {}, cause was {}",
                        imagesPath.toAbsolutePath().toString(),
                        e.getMessage());
                throw new RuntimeException(e);
            }
            logger.debug("Images directory successfully created.");
            return;
        }
        logger.debug("Images directory already exists");
    }

    public AddProductImagesResponse handle(AddProductImagesRequest request) {
        validateNotNull(request, "request");
        logger.debug("serving request to add product images");
        validateNotNull(request.getImages(), "images");
        logger.debug("fetching product with product id of {}", request.getProductId());
        Product product = productRepository.getProductById(request.getProductId());
        User owner = product.getOwner();
        User currentUser = securityContext.getUser();
        if (!owner.equals(currentUser)) {
            logger.error("User {} attempted to edit product with id {} belonging to user {}",
                    currentUser.getUsername(),
                    product.getId(),
                    owner.getUsername());
            throw new IllegalStateException("not the owner");
        }

        int numberOfImages = imageRepository.getImagesCountByProductId(request.getProductId());
        if (request.getImages().size() + numberOfImages > maxNumberOfImagesPerProduct) {
            logger.error("user {} attempted to upload more than the maximum number of images allowed.",
                    currentUser.getUsername());
            throw new IllegalArgumentException("max number of images exceeded");
        }

        Path productImagesPath = imagesPath.resolve(request.getProductId());
        createFolderIfNotExist(productImagesPath);

        List<AddProductImagesRequest.Image> images = request.getImages();
        for (AddProductImagesRequest.Image image : images) {
            validateNotNull(image, "image");
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
