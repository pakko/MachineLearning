package com.ml.bus.model;

import org.springframework.data.annotation.Id;

public class Cluster {
	@Id
	private String clusterId;
	private String categoryId;
	
	public Cluster() {}
	
	public Cluster(String clusterId, String categoryId) {
		super();
		this.clusterId = clusterId;
		this.categoryId = categoryId;
	}
	
	@Override
	public String toString() {
		return "Cluster [clusterId=" + clusterId + ", categoryId=" + categoryId
				+ "]";
	}

	public String getClusterId() {
		return clusterId;
	}
	public void setClusterId(String clusterId) {
		this.clusterId = clusterId;
	}
	public String getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}
	
	
}
