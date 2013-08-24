package com.ml.nlp.preprocessing;

import java.util.List;



public interface TextTokenizer {

	List<String> tokenize(String text, String splitToken);
	List<String> dropStopWords(List<String> oldTokens);
}
