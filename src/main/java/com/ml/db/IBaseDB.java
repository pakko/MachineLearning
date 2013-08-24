package com.ml.db;


import java.util.List;


public interface IBaseDB {
	void save(final Object entity);
	void save(final Object entity, String collectionName);
	
	void update(final Object entity);
	void update(final Object entity, String collectionName);
	
	void delete(final Object entity);
	void delete(final Object entity, String collectionName);
	
	<T> List<T> find(Object query, Object entity);
	<T> List<T> find(Object query, Object entity, String collectionName);
	
	<T> T findOne(Object query, Object entity);
	<T> T findOne(Object query, Object entity, String collectionName);
	
	<T> List<T> findAll(Object entity);
	<T> List<T> findAll(Object entity, String collectionName);
	
	long count(Object query, String collectionName);

}
