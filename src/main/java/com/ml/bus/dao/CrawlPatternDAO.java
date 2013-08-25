package com.ml.bus.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.ml.bus.model.CrawlPattern;
import com.ml.db.IBaseDB;
import com.ml.util.Constants;

public class CrawlPatternDAO implements ICrawlPatternDAO {

	@Autowired
	IBaseDB baseDB;

	public List<CrawlPattern> findByName(String name) {
		Query query = new Query();
		query.addCriteria(Criteria.where("crawlUrl").is(name));
		return baseDB.find(query, CrawlPattern.class, Constants.crawlPatternCollectionName);
	}

	public List<CrawlPattern> findByType(String type) {
		Query query = new Query();
		query.addCriteria(Criteria.where("type").is(type));
		return baseDB.find(query, CrawlPattern.class, Constants.crawlPatternCollectionName);
	}

	public List<CrawlPattern> findAll() {
		return baseDB.findAll(CrawlPattern.class, Constants.crawlPatternCollectionName);
	}

	public void save(CrawlPattern cp) {
		baseDB.save(cp, Constants.crawlPatternCollectionName);
	}
}
