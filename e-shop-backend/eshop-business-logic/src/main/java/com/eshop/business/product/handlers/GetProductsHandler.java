package com.eshop.business.product.handlers;

import com.eshop.business.Handler;
import com.eshop.business.product.requests.GetProductsRequest;
import com.eshop.business.product.responses.GetProductsResponse;
import com.eshop.business.product.responses.ProductDetailsPageEntry;
import com.eshop.models.entities.Category;
import com.eshop.models.entities.Image;
import com.eshop.models.entities.Product;
import com.eshop.models.entities.ProductCategory;
import com.eshop.repositories.ProductRepository;
import com.eshop.repositories.ReposFactory;
import com.eshop.repositories.data.PageDetailsWrapper;
import com.eshop.validators.EshopValidator;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class GetProductsHandler implements Handler<GetProductsRequest, GetProductsResponse> {


    @NotNull
    private final ProductRepository productRepo;
    private final EshopValidator validator;

    public GetProductsHandler(ReposFactory reposFactory, EshopValidator validator) {
        if (validator == null)
            throw new IllegalArgumentException("validator: must not be null");
        if (reposFactory == null)
            throw new IllegalArgumentException("reposFactory: must not be null");
        this.productRepo = reposFactory.getRepository(ProductRepository.class);
        this.validator = validator;
        validator.validate(this);
    }

    @Override
    public GetProductsResponse handle(GetProductsRequest request) {
        if (request == null)
            throw new IllegalArgumentException("request: must not be null");
        validator.validate(request);
        PageDetailsWrapper<Product> products = productRepo.getProducts(request.getPage(),
                request.getSize(),
                request.getCategory(),
                request.getDirection(),
                request.getSortBy(),
                request.getSearchTerm());

        int pages = products.getTotalPages();
        long elements = products.getTotalElements();
        Set<ProductDetailsPageEntry> productEntries = products.getItemsStream().map(this::getProductDetailsEntry).collect(Collectors.toSet());
        return new GetProductsResponse(pages, elements, productEntries);
    }

    private ProductDetailsPageEntry getProductDetailsEntry(Product product) {
        List<String> imageIds = product.getImages().stream().map(Image::getId).collect(Collectors.toList());
        List<Category> categories = product.getCategories().stream().map(ProductCategory::getCategory).collect(Collectors.toList());
        return new ProductDetailsPageEntry.Builder()
                .reviewCount(product.getProductReviews().size())
                .productName(product.getProductName())
                .rating(product.getRating())
                .price(product.getPrice())
                .id(product.getId())
                .imageIds(imageIds)
                .categories(categories)
                .availabilityState(product.getAvailabilityState())
                .build();
    }
}
