package com.wilderman.reviewer.data.dto;

public class RateResponse {
    private boolean isRated;

    public boolean isRated() {
        return isRated;
    }

    public void setRated(boolean rated) {
        isRated = rated;
    }

    public void setRatedInt(Integer statusCode) {
        isRated = statusCode == 200 ? true : false;
    }

}
