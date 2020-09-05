package com.eshop.business.product.handlers;

import com.eshop.business.product.requests.GetProductImageRequest;
import com.eshop.business.product.responses.GetProductImageResponse;
import com.eshop.models.entities.Image;
import com.eshop.repositories.ImageRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class GetProductImageHandlerTest {


    @Mock
    private ImageRepository imageRepository;

    @Test
    void givenNullImageRepository_whenConstructing_thenThrowException() {
        NullPointerException thrown = assertThrows(NullPointerException.class,
                () -> new GetProductImageHandler(null));
        assertEquals("image repository can not be null", thrown.getMessage());
    }

    @Test
    void givenNullRequest_whenHandling_thenThrowException() {
        GetProductImageHandler handler = new GetProductImageHandler(imageRepository);
        NullPointerException thrown = assertThrows(NullPointerException.class,
                () -> handler.handle(null));
        assertEquals("request can not be null", thrown.getMessage());
    }

    @Test
    void givenImageIdThatDoesntExist_whenHandling_thenThrowException() {
        GetProductImageHandler handler = new GetProductImageHandler(imageRepository);
        Mockito.when(imageRepository.getByImageId(anyLong())).thenReturn(null);
        NullPointerException thrown = assertThrows(NullPointerException.class,
                () -> handler.handle(new GetProductImageRequest(1)));
        assertEquals("image does not exist", thrown.getMessage());
    }

    @Test
    void givenValidRequest_whenHandling_thenReturnExpected() throws IOException {
        long imageId = 123;
        long productId = 4;
        String imageName = "test-image.JPG";
        String imageContent = "MOCK-CONTENT";
        Path imagesPath = Paths.get("./src/test/resources");
        Path imagePath = imagesPath.resolve(Long.toString(productId)).resolve(imageName).toAbsolutePath();
        createTestImageFile(imagePath, imageContent);
        prepareMock(imageId, imageName, imagePath);
        GetProductImageHandler handler = new GetProductImageHandler(imageRepository);
        GetProductImageRequest request = new GetProductImageRequest(imageId);
        GetProductImageResponse response = handler.handle(request);
        String expectedImageContent = new String(Base64.getDecoder().decode(response.getBase64Image()));
        assertEquals(expectedImageContent, imageContent);
        Mockito.verify(imageRepository, times(1)).getByImageId(anyLong());
    }

    private void prepareMock(long imageId, String imageName, Path imagePath) {
        Image image = getImage(imageName, imagePath);
        Mockito.when(imageRepository.getByImageId(imageId)).thenReturn(image);
    }

    private void createTestImageFile(Path imagePath, String imageContent) throws IOException {
        Files.createDirectories(imagePath.getParent());
        try (BufferedWriter writer = Files.newBufferedWriter(imagePath)) {
            writer.write(imageContent);
        }
    }

    private Image getImage(String imageName, Path imagePath) {
        return new Image.Builder()
                .name(imageName)
                .path(imagePath.toString())
                .product(null)
                .size(10)
                .build();
    }
}
