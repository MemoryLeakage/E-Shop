package com.eshop.models.entities;

import javax.persistence.*;

@Entity
@Table(name = "product_review")
public class ProductReview {

    @EmbeddedId
    private ProductReviewId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("productId")
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "rate")
    private float rate;
    @Column(name = "comment")
    private String comment;


}
