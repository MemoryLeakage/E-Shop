package com.eshop.business.product.handlers;

import com.eshop.business.Handler;
import com.eshop.business.product.requests.GetProductImageRequest;
import com.eshop.business.product.responses.GetProductImageResponse;
import com.eshop.models.entities.Image;
import com.eshop.repositories.ImageRepository;
import com.eshop.utilities.Validators;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Base64;
import java.util.Objects;

public class GetProductImageHandler implements Handler<GetProductImageRequest, GetProductImageResponse> {

    private static final Logger logger = LoggerFactory.getLogger(GetProductImageHandler.class);
    private final ImageRepository imageRepository;

    public GetProductImageHandler(ImageRepository imageRepository) {
        Validators.validateNotNullArgument(imageRepository, "image repository");

        this.imageRepository = imageRepository;
    }

    public GetProductImageResponse handle(GetProductImageRequest request) {
        Validators.validateNotNullArgument(request, "request");
        Image image = imageRepository.getByImageId(request.getImageId());
        Validators.validate(Objects::isNull,
                NullPointerException::new,
                "image does not exist",
                image);
        try (BufferedReader bufferedReader = Files.newBufferedReader(Paths.get(image.getPath()))) {
            StringBuilder builder = new StringBuilder();
            bufferedReader.lines().forEach(builder::append);
            return new GetProductImageResponse(new String(Base64.getEncoder().encode(builder.toString().getBytes())));
        } catch (IOException e) {
            logger.error(e.getMessage());
            logger.debug(Arrays.toString(e.getStackTrace()));
            throw new RuntimeException(e);
        }
    }

}
