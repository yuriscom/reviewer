package com.wilderman.reviewer.dto.SmsResponseHandler;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SnsObject {

    @JsonProperty("Message")
    private MessageObject message;

    public SnsObject(MessageObject message) {
        this.message = message;
    }

    public MessageObject getMessage() {
        return message;
    }

    public void setMessage(MessageObject message) {
        this.message = message;
    }
}
