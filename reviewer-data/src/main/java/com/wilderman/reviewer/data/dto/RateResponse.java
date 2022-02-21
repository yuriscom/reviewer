package com.wilderman.reviewer.data.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RateResponse {
    private String hash;
    private String reviewLink;
    private boolean isRated;

//    public void setRatedInt(Integer status) {
//        isRated = status == 200 ? true : false;
//    }

}
