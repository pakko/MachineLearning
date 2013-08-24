package com.ml.bus.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ml.bus.mapper.CrawlPatternMapper;
import com.ml.bus.model.CrawlPattern;

@Repository
public class CrawlPatternDAO implements ICrawlPatternDAO {

	@Autowired
	CrawlPatternMapper crawlPatternMapper;
	public List<CrawlPattern> findByName(String name) {
		return crawlPatternMapper.findByName(name);
	}

	public List<CrawlPattern> findByType(String type) {
		return crawlPatternMapper.findByType(type);
	}

	public List<CrawlPattern> findAll() {
		return crawlPatternMapper.findAll();
	}

	public void save(CrawlPattern cp) {
		crawlPatternMapper.save(cp);
	}
}
