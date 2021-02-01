package com.wilderman.reviewer.dto;

public class PublishSmsInput {

    private String pn;

    private String msg;

    public PublishSmsInput() {

    }

    public PublishSmsInput(String pn, String msg) {
        this.pn = pn;
        this.msg = msg;
    }

    public String getPn() {
        return pn;
    }

    public void setPn(String pn) {
        this.pn = pn;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
