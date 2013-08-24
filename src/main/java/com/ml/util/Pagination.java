package com.ml.util;


import java.util.List;

import javax.servlet.http.HttpServletRequest;

public class Pagination{

	private List<?> items;
	private int totalCount;

	private int totalPage;
	private int currentPage;
	private int limitSize;	//limit page size
	private int startIndex;
	private String sortField; // sort by field
	private String sortOrder; // desc or asc
	
	private int[] indexes = new int[0];
	
	public Pagination(HttpServletRequest servletRequest) {
		String p_currentPage = servletRequest.getParameter("currentPage");
		String p_limitSize = servletRequest.getParameter("limitSize");	//limit size
		String p_sortField = servletRequest.getParameter("sortField");
		String p_sortOrder = servletRequest.getParameter("sortOrder");

		int limitSize = Integer.parseInt(p_limitSize);
		int currentPage = Integer.parseInt(p_currentPage);
		int startIndex = (currentPage - 1) * limitSize;

		setCurrentPage(currentPage);
		setLimitSize(limitSize);
		setStartIndex(startIndex);
		setSortField(p_sortField);
		setSortOrder(p_sortOrder);

	}


	public List<?> getItems() {
		return items;
	}

	public void setItems(List<?> items) {
		this.items = items;
	}

	public int getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public int getLimitSize() {
		return limitSize;
	}

	public void setLimitSize(int limitSize) {
		this.limitSize = limitSize;
	}

	public int[] getIndexes() {
		return indexes;
	}

	public void setIndexes(int[] indexes) {
		this.indexes = indexes;
	}
	
	public int getStartIndex() {
		return startIndex;
	}

	public void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
	}

	public String getSortField() {
		return sortField;
	}

	public void setSortField(String sortField) {
		this.sortField = sortField;
	}

	public String getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(String sortOrder) {
		this.sortOrder = sortOrder;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		if (totalCount > 0) {
			this.totalCount = totalCount;
		} else {
			this.totalCount = 0;
		}
	}

}
