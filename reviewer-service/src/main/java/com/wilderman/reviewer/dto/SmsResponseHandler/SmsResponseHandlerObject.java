package com.wilderman.reviewer.dto.SmsResponseHandler;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SmsResponseHandlerObject {

    @JsonProperty("Sns")
    private SnsObject sns;

    public SmsResponseHandlerObject(SnsObject sns) {
        this.sns = sns;
    }

    public SnsObject getSns() {
        return sns;
    }

    public void setSns(SnsObject sns) {
        this.sns = sns;
    }
}
