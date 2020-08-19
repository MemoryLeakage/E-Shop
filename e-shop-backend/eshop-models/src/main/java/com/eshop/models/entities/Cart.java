package com.eshop.models.entities;

import javax.persistence.*;

@Entity
@Table(name = "cart")
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "cart_id")
    private Long id;
    @Column(name = "cart_total_price")
    private Double totalPrice;
}
