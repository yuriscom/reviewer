package com.wilderman.reviewer.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FetchLogData {
    private String uname;
    private Boolean isDirect;

    public FetchLogData(String uname, Boolean isDirect) {
        this.uname = uname;
        this.isDirect = isDirect;
    }
}
