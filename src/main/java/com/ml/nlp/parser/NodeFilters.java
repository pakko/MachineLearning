package com.ml.nlp.parser;

import org.htmlparser.NodeFilter;

public class NodeFilters {
	private NodeFilter titleFilter;
	private NodeFilter contentFilter;
	private NodeFilter dateFilter;
	private NodeFilter authorFilter;
	private NodeFilter imgFilter;
	private NodeFilter sourceFilter;
	
	
	public NodeFilters(NodeFilter titleFilter, NodeFilter contentFilter,
			NodeFilter dateFilter, NodeFilter authorFilter,
			NodeFilter imgFilter, NodeFilter sourceFilter) {
		super();
		this.titleFilter = titleFilter;
		this.contentFilter = contentFilter;
		this.dateFilter = dateFilter;
		this.authorFilter = authorFilter;
		this.imgFilter = imgFilter;
		this.sourceFilter = sourceFilter;
	}
	
	public NodeFilter getTitleFilter() {
		return titleFilter;
	}
	public void setTitleFilter(NodeFilter titleFilter) {
		this.titleFilter = titleFilter;
	}
	public NodeFilter getContentFilter() {
		return contentFilter;
	}
	public void setContentFilter(NodeFilter contentFilter) {
		this.contentFilter = contentFilter;
	}
	public NodeFilter getDateFilter() {
		return dateFilter;
	}
	public void setDateFilter(NodeFilter dateFilter) {
		this.dateFilter = dateFilter;
	}
	public NodeFilter getAuthorFilter() {
		return authorFilter;
	}
	public void setAuthorFilter(NodeFilter authorFilter) {
		this.authorFilter = authorFilter;
	}
	public NodeFilter getImgFilter() {
		return imgFilter;
	}
	public void setImgFilter(NodeFilter imgFilter) {
		this.imgFilter = imgFilter;
	}
	public NodeFilter getSourceFilter() {
		return sourceFilter;
	}
	public void setSourceFilter(NodeFilter sourceFilter) {
		this.sourceFilter = sourceFilter;
	}
	@Override
	public String toString() {
		return "NodeFilters [titleFilter=" + titleFilter + ", contentFilter="
				+ contentFilter + ", dateFilter=" + dateFilter
				+ ", authorFilter=" + authorFilter + ", imgFilter=" + imgFilter
				+ ", sourceFilter=" + sourceFilter + "]";
	}
	
	
	
	
}
