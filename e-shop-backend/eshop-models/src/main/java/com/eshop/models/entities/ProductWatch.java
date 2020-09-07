package com.eshop.models.entities;

import javax.persistence.*;

@Entity
@Table(name = "product_watch")
public class ProductWatch {

    @EmbeddedId
    private ProductWatchId id;

    @ManyToOne
    @MapsId("productId")
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;
}
