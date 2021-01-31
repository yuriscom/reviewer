package com.wilderman.reviewer.dto.response;

import java.util.List;

public interface IPageableResponse<T> extends IResponse<T> {
	PageableResponse.PageableMetaResponse getPageInfo();
	List<T> getData();
}
