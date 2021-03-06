package com.ml.bus.service;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ml.bus.dao.INewsDAO;
import com.ml.bus.model.News;
import com.ml.util.Pagination;

@Service
public class NewsService {

	@Autowired
	INewsDAO newsDAO;


	public List<News> findAll() {
		return newsDAO.findAll();
	}
	
	public List<News> findByCategory(String categoryId) {
		return newsDAO.findByCategory(categoryId);
	}
	
	public Pagination findByPage(Pagination pager) {
		return newsDAO.findByPage(pager);
	}
	
	public Pagination findByCategoryAndPage(String categoryId, Pagination pager) {
		return newsDAO.findByCategoryAndPage(categoryId, pager);
	}
	
	public Pagination findByCategorysAndPage(String categoryId, String clusterId, Pagination pager) {
		return newsDAO.findByCategorysAndPage(categoryId, clusterId, pager);
	}
	
	public void save(News news) {
		newsDAO.save(news);
	}
	
	public void saveOrUpdate(News news) {
		News n = newsDAO.findById(news.getId());
		if(n != null) {
			newsDAO.delete(n);
		}
		newsDAO.save(news);
	}
	
}
