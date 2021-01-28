package com.eshop.business.product.handlers;

import com.eshop.business.Handler;
import com.eshop.business.exceptions.NotOwnerException;
import com.eshop.business.product.requests.DeleteImageRequest;
import com.eshop.models.entities.Image;
import com.eshop.models.entities.User;
import com.eshop.repositories.ImageRepository;
import com.eshop.repositories.ReposFactory;
import com.eshop.security.SecurityContext;
import com.eshop.validators.EshopValidator;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.lang.model.type.NullType;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class DeleteImageHandler implements Handler<DeleteImageRequest, NullType> {


    private static final Logger logger = LoggerFactory.getLogger(DeleteImageHandler.class);

    @NotNull
    private final SecurityContext securityContext;
    @NotNull
    private final Path path;
    private final EshopValidator validator;
    @NotNull
    private final ImageRepository imageRepo;

    public DeleteImageHandler(SecurityContext securityContext,
                              ReposFactory reposFactory,
                              Path path,
                              EshopValidator validator) {
        if (validator == null)
            throw new IllegalArgumentException("validator: must not be null");
        if (reposFactory == null)
            throw new IllegalArgumentException("reposFactory: must not be null");

        this.validator = validator;
        this.securityContext = securityContext;
        this.path = path;
        this.imageRepo = reposFactory.getRepository(ImageRepository.class);
        validator.validate(this);
    }

    @Override
    public NullType handle(DeleteImageRequest request) {
        if (request == null)
            throw new IllegalArgumentException("request: must not be null");
        validator.validate(request);

        User user = securityContext.getUser();
        Image image = imageRepo.getByImageId(request.getImageId());
        if (image == null)
            throw new IllegalArgumentException("image does not exist");
        User owner = image.getProduct().getOwner();
        if (!owner.getUsername().equals(user.getUsername())) {
            logger.error("User {} attempted to delete product image with product-id={} belonging to user {}",
                    user.getUsername(),
                    image.getProduct().getId(),
                    owner.getUsername());
            throw new NotOwnerException();
        }

        Path imagePath = Path.of(image.getPath());
        if (Files.exists(imagePath)) {
            try {
                Files.delete(imagePath);
            } catch (IOException e) {
                logger.error("error deleting image", e);
                return null;
            }
        }
        imageRepo.removeImageById(request.getImageId());
        return null;
    }
}
