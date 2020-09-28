package com.eshop.models.entities;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class UserReviewId implements Serializable {
    @Column(name = "reviewer_id")
    private String reviewerId;
    @Column(name = "reviewee_id")
    private String revieweeId;

    public UserReviewId(String reviewerId, String revieweeId) {
        this.reviewerId = reviewerId;
        this.revieweeId = revieweeId;
    }

    public UserReviewId() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserReviewId that = (UserReviewId) o;
        return reviewerId.equals(that.reviewerId) &&
                revieweeId.equals(that.revieweeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(reviewerId, revieweeId);
    }
}
