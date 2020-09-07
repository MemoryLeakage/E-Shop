package com.eshop.models.entities;


import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "cart")
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private Long id;
    @Column(name = "total_price")
    private Double totalPrice;

    @OneToOne(mappedBy = "cart")
    private User user;

    @OneToMany(mappedBy = "cart" ,fetch = FetchType.LAZY)
    private List<CartDetails> cartDetailsList;
}
