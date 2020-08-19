package com.eshop.models.entities;

import javax.persistence.*;

@Entity
@Table(name = "cart_details")
public class CartDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private Long id;
    @Column(name = "quantity")
    private Integer quantity;
    @Column(name = "total_price")
    private Double totalPrice;
}
