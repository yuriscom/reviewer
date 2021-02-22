package com.wilderman.reviewer.data.dto;

import com.wilderman.reviewer.db.primary.entities.Review;

public class BadReviewOutput {
    private String referenceNo;

    public BadReviewOutput(Review review) {
        this.referenceNo = review.getReferenceNo();
    }

    public BadReviewOutput() {
    }

    public String getReferenceNo() {
        return referenceNo;
    }

    public void setReferenceNo(String referenceNo) {
        this.referenceNo = referenceNo;
    }
}
