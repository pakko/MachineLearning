package com.ml.bus.dao;

import java.util.List;

import com.ml.bus.model.News;
import com.ml.util.Pagination;

public interface INewsDAO {
	List<News> findAll();
	List<News> findByCategory(String categoryId);
	Pagination findByPage(Pagination pager);
	Pagination findByCategoryAndPage(String categoryId, Pagination pager);
	Pagination findByCategorysAndPage(String categoryId, String clusterId, Pagination pager);
	void save(News news);
	News findById(String id);
	void delete(News news);
}
