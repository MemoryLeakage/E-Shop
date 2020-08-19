package com.eshop.models.entities;

import javax.persistence.*;

@Entity
@Table(name = "order_details")
public class OrderDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private Long id;
    @Column(name = "total_price")
    private Double totalPrice;
    @Column(name = "quantity")
    private Integer quantity;
}
