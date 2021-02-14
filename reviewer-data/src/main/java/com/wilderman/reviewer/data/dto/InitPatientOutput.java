package com.wilderman.reviewer.data.dto;

public class InitPatientOutput {
    private String hash;

    public InitPatientOutput(String hash) {
        this.hash = hash;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }
}
