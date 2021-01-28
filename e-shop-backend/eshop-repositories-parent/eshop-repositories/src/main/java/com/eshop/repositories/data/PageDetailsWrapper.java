package com.eshop.repositories.data;

import java.util.stream.Stream;

public class PageDetailsWrapper<T> {


    private final int totalPages;
    private final long totalElements;
    private final Stream<T> items;

    public PageDetailsWrapper(int totalPages, long totalElements, Stream<T> items) {
        this.totalPages = totalPages;
        this.totalElements = totalElements;
        this.items = items;
    }

    public int getTotalPages(){
        return this.totalPages;
    }

    public long getTotalElements(){
        return this.totalElements;
    }

    public Stream<T> getItemsStream() {
        return this.items;
    }
}
