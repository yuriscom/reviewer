package com.wilderman.reviewer.data.dto;

public class RateResponse {
    private boolean isRated;
    private String hash;

    public boolean isRated() {
        return isRated;
    }

    public void setRated(boolean rated) {
        isRated = rated;
    }

    public void setRatedInt(Integer statusCode) {
        isRated = statusCode == 200 ? true : false;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }
}
