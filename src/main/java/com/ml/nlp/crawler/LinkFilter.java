package com.ml.nlp.crawler;

public interface LinkFilter {
	public boolean accept(String url);
}