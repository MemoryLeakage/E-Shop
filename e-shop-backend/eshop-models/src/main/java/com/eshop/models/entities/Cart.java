package com.eshop.models.entities;


import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "cart")
public class Cart {

    @Id
    @GeneratedValue(generator = "id-generator")
    @GenericGenerator(name = "id-generator", strategy = "com.eshop.models.IdsGenerator")
    @Column(name = "id")
    private String id;
    @Column(name = "total_price")
    private Double totalPrice;

    @OneToOne(mappedBy = "cart")
    private User user;

    @OneToMany(mappedBy = "cart" ,fetch = FetchType.LAZY)
    private List<CartDetails> cartDetailsList;
}
