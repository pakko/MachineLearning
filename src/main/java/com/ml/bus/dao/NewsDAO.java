package com.ml.bus.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ml.bus.mapper.NewsMapper;
import com.ml.bus.model.News;
import com.ml.util.Pagination;

@Repository
public class NewsDAO implements INewsDAO {
	
	@Autowired
	NewsMapper newsMapper;
	
	public List<News> findAll() {
		return newsMapper.findAll();
	}
	
	public List<News> findByCategory(String categoryId) {
		return newsMapper.findByCategory(categoryId);
	}
	
	public Pagination findByPage(Pagination pager) {
		int limitSize = pager.getLimitSize();
		List<News> items = newsMapper.findByPage(pager);
		int totalCount = newsMapper.count();

		int totalPage = (int)(totalCount / limitSize) + 1;
		if((totalCount % limitSize) == 0) {
			totalPage = totalPage - 1;
		}
		pager.setItems(items);
		pager.setTotalCount(totalCount);
		pager.setTotalPage(totalPage);
		
		return pager;
	}
	
	public Pagination findByCategoryAndPage(String categoryId, Pagination pager) {
		int limitSize = pager.getLimitSize();
		List<News> items = newsMapper.findByCategoryAndPage(categoryId, pager);
		int totalCount = newsMapper.countCategory(categoryId);

		int totalPage = (int)(totalCount / limitSize) + 1;
		if((totalCount % limitSize) == 0) {
			totalPage = totalPage - 1;
		}
		pager.setItems(items);
		pager.setTotalCount(totalCount);
		pager.setTotalPage(totalPage);
		
		return pager;
	}
	
	public void save(News news) {
		newsMapper.save(news);
	}
	

}
