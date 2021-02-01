package com.wilderman.reviewer.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.wilderman.reviewer.dto.SmsResponseHandler.ResponseBody;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SmsResponseHandlerOutput {
    private Integer statusCode;
    private ResponseBody body;

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public ResponseBody getBody() {
        return body;
    }

    public void setBody(ResponseBody body) {
        this.body = body;
    }
}
