package com.wilderman.reviewer.dto.response;

public interface ISingleResponse<T> extends IResponse<T> {
	T getData();
}
