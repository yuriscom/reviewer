package com.wilderman.reviewer.dto.response;

public interface IResponse<T> {
	int getStatus();
	String getError();
}
