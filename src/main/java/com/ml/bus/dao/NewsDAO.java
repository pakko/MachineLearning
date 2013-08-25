package com.ml.bus.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Order;
import org.springframework.data.mongodb.core.query.Query;

import com.ml.bus.model.News;
import com.ml.db.IBaseDB;
import com.ml.util.Constants;
import com.ml.util.Pagination;

public class NewsDAO implements INewsDAO {
	
	@Autowired
	IBaseDB baseDB;
	
	public List<News> findAll() {
		return baseDB.findAll(News.class, Constants.newsCollectionName);
	}
	
	public List<News> findByCategory(String categoryId) {
		Query query = new Query();
		query.addCriteria(Criteria.where("category._id").is(categoryId));
		return baseDB.find(query, News.class, Constants.newsCollectionName);
	}
	
	public Pagination findByPage(Pagination pager) {
		int limitSize = pager.getLimitSize();
		int startIndex = pager.getStartIndex();
		
		Query query = new Query();
		if(pager.getSortOrder().equals("desc")){
			query.sort().on(pager.getSortField(), Order.DESCENDING);
		}
		else if(pager.getSortOrder().equals("asc")){
			query.sort().on(pager.getSortField(), Order.ASCENDING);
		}
		query = query.skip(startIndex).limit(limitSize);
		
		List<News> items = baseDB.find(query, News.class, Constants.newsCollectionName);
		int totalCount = (int) baseDB.count(query, Constants.newsCollectionName);

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
		int startIndex = pager.getStartIndex();
		
		Query query = new Query();
		query.addCriteria(Criteria.where("category._id").is(categoryId));
		
		if(pager.getSortOrder().equals("desc")){
			query.sort().on(pager.getSortField(), Order.DESCENDING);
		}
		else if(pager.getSortOrder().equals("asc")){
			query.sort().on(pager.getSortField(), Order.ASCENDING);
		}
		query = query.skip(startIndex).limit(limitSize);
		
		List<News> items = baseDB.find(query, News.class, Constants.newsCollectionName);
		int totalCount = (int) baseDB.count(query, Constants.newsCollectionName);

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
		baseDB.save(news, Constants.newsCollectionName);
	}
	

}
