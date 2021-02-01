package com.eshop.business.cart.requests;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

public class GetCartItemsRequest {

    @Positive
    private final int pageNumber;
    @Positive
    @Max(100)
    private final int pageSize;

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
