package com.eshop.models.entities;

import javax.persistence.*;

@Entity
@Table(name = "user_review")
public class UserReview {

    @EmbeddedId
    private UserReviewId userReviewId;

    @Column(name = "comment")
    private String comment;
    @Column(name = "rate")
    private Float rate;

    @ManyToOne
    @MapsId("reviewerId")
    @JoinColumn(name = "reviewer")
    private User reviewer;

    @ManyToOne
    @MapsId("revieweeId")
    @JoinColumn(name = "reviewee")
    private User reviewee;
}
