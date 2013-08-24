package com.ml.nlp.classify;

public class ClassifyResult {
	private double probility;
	private String category;

	public ClassifyResult() {
		this.probility = 0;
		this.category = null;
	}

	public double getProbility() {
		return probility;
	}

	public void setProbility(double probility) {
		this.probility = probility;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}
	
	
}
