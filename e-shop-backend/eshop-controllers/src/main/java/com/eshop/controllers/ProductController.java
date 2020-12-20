package com.eshop.controllers;

import com.eshop.business.product.requests.AddProductImagesRequest;
import com.eshop.business.product.requests.AddProductRequest;
import com.eshop.business.product.requests.GetProductDetailsRequest;
import com.eshop.business.product.requests.GetProductImageRequest;
import com.eshop.business.product.responses.AddProductImagesResponse;
import com.eshop.business.product.responses.AddProductResponse;
import com.eshop.business.product.responses.GetProductDetailsResponse;
import com.eshop.business.product.responses.GetProductImageResponse;
import com.eshop.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
            imagesToAdd.add(new Image(image.getBytes(), image.getContentType()));
        }
        AddProductImagesRequest request = new AddProductImagesRequest(imagesToAdd, productId);
        AddProductImagesResponse addProductImagesResponse = productService.addProductImage(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(addProductImagesResponse);
    }

    @GetMapping("/images/{imageId}")
    public ResponseEntity<GetProductImageResponse> getProductImage(@PathVariable String imageId) {
        GetProductImageRequest request = new GetProductImageRequest(imageId);
        GetProductImageResponse getProductImageResponse = productService.getProductImage(request);
        return ResponseEntity.status(HttpStatus.OK).body(getProductImageResponse);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<GetProductDetailsResponse> getPRoductDetails(@PathVariable String productId){
        GetProductDetailsResponse productDetails = productService.getProductDetails(new GetProductDetailsRequest(productId));
        return ResponseEntity.status(HttpStatus.OK).body(productDetails);
    }
}
