package com.ml.bus.dao;

import java.util.List;


import com.ml.bus.model.CrawlPattern;

public interface ICrawlPatternDAO {
	List<CrawlPattern> findByName(String name);
	List<CrawlPattern> findByType(String type);
	List<CrawlPattern> findAll();
	void save(CrawlPattern cp);
}
