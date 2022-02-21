package com.wilderman.reviewer.dto.response;

public class Response<T> implements ISingleResponse<T> {

    private int status;
    private String error;
    private T data;

    public Response() {}

    public Response(int status, T data) {
        this.status = status;
        this.data = data;
    }

    public Response(T data) {
        this(200, data, null);
    }

    public Response(int status, T data, String error) {
		this(status, data);
		this.error = error;
	}

    @Override
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    @Override
    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
