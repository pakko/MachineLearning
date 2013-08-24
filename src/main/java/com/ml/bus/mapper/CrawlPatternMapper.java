package com.ml.bus.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import com.ml.bus.model.CrawlPattern;

@Repository
public interface CrawlPatternMapper {
	
	final String SELECT_ALL = "SELECT * FROM crawl_pattern";
	final String INSERT = "INSERT INTO crawl_pattern(crawlUrl, patternUrl, type) VALUES(#{crawlUrl}, #{patternUrl}, #{type})";
	final String SELECT_BY_NAME = "SELECT * FROM crawl_pattern WHERE name=#{name}";
	final String SELECT_BY_TYPE = "SELECT * FROM crawl_pattern WHERE type=#{type}";

	
	
	@Select(SELECT_BY_NAME)
	List<CrawlPattern> findByName(String name);

	@Select(SELECT_BY_TYPE)
	List<CrawlPattern> findByType(String type);

	@Select(SELECT_ALL)
	List<CrawlPattern> findAll();

	@Insert(INSERT)
	void save(CrawlPattern cp);
	
}