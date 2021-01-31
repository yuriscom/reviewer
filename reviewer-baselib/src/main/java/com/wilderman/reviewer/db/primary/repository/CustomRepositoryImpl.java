package com.wilderman.reviewer.db.primary.repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

public class CustomRepositoryImpl<T> implements CustomRepository<T> {
	
	@PersistenceContext
	protected EntityManager entityManager;

	@Override
	@Transactional
	public void refresh(T entity) {
		entityManager.refresh(entity);
	}

	@Override
	public void detach(T entity) {
		entityManager.detach(entity);
	}

}
