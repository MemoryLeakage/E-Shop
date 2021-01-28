package com.eshop.business.product.responses;

import java.util.Set;

public class GetProductsResponse {


    private int totalPages;
    private long totalElements;
    private Set<ProductDetailsPageEntry> products;

    public GetProductsResponse(int totalPages, long totalElements, Set<ProductDetailsPageEntry> products) {
        this.totalPages = totalPages;
        this.totalElements = totalElements;
        this.products = products;
    }

    public Set<ProductDetailsPageEntry> getProducts() {
        return this.products;
    }

    public long getTotalElements() {
        return this.totalElements;
    }

    public int getTotalPages() {
        return this.totalPages;
    }

}
