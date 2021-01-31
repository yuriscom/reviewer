package com.wilderman.reviewer.dto.response;

public class Response<T> implements ISingleResponse<T> {

    private int statusCode;
    private String error;
    private T data;

    public Response() {}

    public Response(int statusCode, T data) {
        this.statusCode = statusCode;
        this.data = data;
    }

    public Response(T data) {
        this(200, data, null);
    }

    public Response(int statusCode, T data, String error) {
		this(statusCode, data);
		this.error = error;
	}

    @Override
    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
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
