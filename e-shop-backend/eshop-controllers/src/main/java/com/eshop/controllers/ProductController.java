package com.eshop.controllers;

import com.eshop.business.product.requests.*;
import com.eshop.business.product.responses.*;
import com.eshop.services.ProductService;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import static com.eshop.business.product.requests.AddProductImagesRequest.Image;

@RestController
@RequestMapping("v1/products")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("")
    public ResponseEntity<AddProductResponse> addProduct(@RequestBody AddProductRequest request) {
        AddProductResponse addProductResponse = productService.addProduct(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(addProductResponse);
    }

    @PostMapping("/{productId}/images")
    public ResponseEntity<AddProductImagesResponse> addProductImage(@RequestPart("file") List<MultipartFile> images,
                                                                    @PathVariable String productId) throws IOException {
        List<Image> imagesToAdd = new ArrayList<>();
        for (MultipartFile image : images) {
            imagesToAdd.add(new Image(image.getBytes(), FilenameUtils.getExtension(image.getOriginalFilename())));
        }
        AddProductImagesRequest request = new AddProductImagesRequest(imagesToAdd, productId);
        AddProductImagesResponse addProductImagesResponse = productService.addProductImage(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(addProductImagesResponse);
    }

    @DeleteMapping("/images/{imageId}")
    public ResponseEntity<?> deleteImage(@PathVariable("imageId") String imageId){

        DeleteImageRequest request = new DeleteImageRequest(imageId);
        productService.removeImage(request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/images/{imageId}")
    public ResponseEntity<GetProductImageResponse> getProductImage(@PathVariable String imageId) {
        GetProductImageRequest request = new GetProductImageRequest(imageId);
        GetProductImageResponse getProductImageResponse = productService.getProductImage(request);
        return ResponseEntity.status(HttpStatus.OK).body(getProductImageResponse);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<GetProductDetailsResponse> getProductDetails(@PathVariable String productId) {
        GetProductDetailsResponse productDetails = productService.getProductDetails(new GetProductDetailsRequest(productId));
        return ResponseEntity.status(HttpStatus.OK).body(productDetails);
    }
}
