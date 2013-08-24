package com.ml.nlp.parser;

public interface IParser<T> {
	
	T parse(String url);
}
