package com.eshop.models.entities;

import com.eshop.models.constants.ProductAvailabilityState;

import javax.persistence.*;

@Entity
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private Long id;
    @Column(name = "product_name")
    private String productName;
    @Column(name = "img_url")
    private String imgUrl;
    @Column(name = "rating")
    private Float rating;
    @Column(name = "available_quantity")
    private Integer availableQuantity;
    @Column(name = "sold_quantity")
    private Integer soldQuantity;
    @Column(name = "availabilty_state")
    private ProductAvailabilityState availabilityState;
    @Column(name = "price")
    private Double price;
    @Column(name = "description")
    private String description;

}
