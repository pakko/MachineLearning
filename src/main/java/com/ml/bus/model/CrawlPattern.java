package com.ml.bus.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

public class CrawlPattern {
	@Id
	private String id;
	@Indexed(unique = true)
	private String crawlUrl;
	private String patternUrl;
	private String type;
	
	public CrawlPattern() {}
	
	public CrawlPattern(String id, String crawlUrl, String patternUrl, String type) {
		super();
		this.id = id;
		this.crawlUrl = crawlUrl;
		this.patternUrl = patternUrl;
		this.type = type;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCrawlUrl() {
		return crawlUrl;
	}
	public void setCrawlUrl(String crawlUrl) {
		this.crawlUrl = crawlUrl;
	}
	public String getPatternUrl() {
		return patternUrl;
	}
	public void setPatternUrl(String patternUrl) {
		this.patternUrl = patternUrl;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	@Override
	public String toString() {
		return "CrawlPattern [id=" + id + ", crawlUrl=" + crawlUrl
				+ ", patternUrl=" + patternUrl + ", type=" + type + "]";
	}
	
	

}
