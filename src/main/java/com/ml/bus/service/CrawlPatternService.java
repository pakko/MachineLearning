package com.ml.bus.service;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ml.bus.dao.ICrawlPatternDAO;
import com.ml.bus.model.CrawlPattern;

@Service
public class CrawlPatternService {

	@Autowired
	ICrawlPatternDAO crawlPatternDAO;

	public List<CrawlPattern> findByName(String name) {
		return crawlPatternDAO.findByName(name);
	}

	public List<CrawlPattern> findByType(String type) {
		return crawlPatternDAO.findByType(type);
	}

	public List<CrawlPattern> findAll() {
		return crawlPatternDAO.findAll();
	}

	public void save(CrawlPattern cp) {
		crawlPatternDAO.save(cp);
	}
	
	
	
	
}
