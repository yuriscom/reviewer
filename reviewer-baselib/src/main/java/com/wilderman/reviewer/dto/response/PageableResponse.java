package com.wilderman.reviewer.dto.response;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

public class PageableResponse<S, T> implements IPageableResponse<T> {
	private int status;
	private String error;

	private PageableMetaResponse pageInfo;
	private List<T> data;

	public PageableResponse() {
		super();
		this.status = 200;
	}

	public PageableResponse(Page<S> page, List<T> data) {
		this();
		this.error = null;
		this.data = data;
		this.pageInfo = new PageableMetaResponse(page.getPageable(), page.getTotalElements(), data.size());
	}

	public PageableResponse(Page<S> page) {
		this();
		this.error = null;
		this.data = (List<T>) page.getContent();
		this.pageInfo = new PageableMetaResponse(page.getPageable(), page.getTotalElements(), data.size());
	}
	
	public PageableResponse(int status, String error, List<T> data) {
		this();
		this.status = status;
		this.error = error;
		this.data = data;
	}

	@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
	public class PageableMetaResponse {

		private long totalCount;
		private int currentPage;
		private int currentSize;
		private int pageSize;

		public PageableMetaResponse(Pageable pageable, long totalCount, int currentSize) {
			this.currentPage = pageable.getPageNumber() + 1;
			this.pageSize = pageable.getPageSize();
			this.totalCount = totalCount;
			this.currentSize = currentSize;

		}

		public long getTotalCount() {
			return totalCount;
		}

		public void setTotalCount(long totalCount) {
			this.totalCount = totalCount;
		}

		public int getCurrentPage() {
			return currentPage;
		}

		public void setCurrentPage(int currentPage) {
			this.currentPage = currentPage;
		}

		public int getCurrentSize() {
			return currentSize;
		}

		public void setCurrentSize(int currentSize) {
			this.currentSize = currentSize;
		}

		public int getPageSize() {
			return pageSize;
		}

		public void setPageSize(int pageSize) {
			this.pageSize = pageSize;
		}
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public PageableMetaResponse getPageInfo() {
		return pageInfo;
	}

	public void setPageInfo(PageableMetaResponse pageInfo) {
		this.pageInfo = pageInfo;
	}

	public List<T> getData() {
		return data;
	}

	public void setData(List<T> data) {
		this.data = data;
	}

}
