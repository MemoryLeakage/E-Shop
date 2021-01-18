package com.eshop.business.cart.requests;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Positive;

public class GetCartItemsRequest {

    @Positive
    private int pageNumber;
    @Positive
    @Max(100)
    private int pageSize;

    public GetCartItemsRequest(int pageNumber, int pageSize) {
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public int getPageSize() {
        return pageSize;
    }
}
