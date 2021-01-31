package com.wilderman.reviewer.db.primary.repository;

public interface CustomRepository<T> {
	public void refresh(T entity);
	public void detach(T entity);
}
