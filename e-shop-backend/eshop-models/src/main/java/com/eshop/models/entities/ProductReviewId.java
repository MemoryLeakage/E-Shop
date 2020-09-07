package com.eshop.models.entities;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import java.io.Serializable;

@Embeddable
public class ProductReviewId implements Serializable {

    @Column(name = "product_id")
    private String productId;
    @Column(name = "user_id")
    private Long userId;
}
