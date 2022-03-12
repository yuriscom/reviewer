package com.wilderman.reviewer.data.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RateInput {
    private Integer rating;
    private String userAgent;
}
