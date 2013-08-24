package com.ml.nlp.preprocessing;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

import com.chenlb.mmseg4j.analysis.MMSegAnalyzer;


public class ChineseTokenizer implements TextTokenizer {
	
	private Analyzer analyzer;
	
	public ChineseTokenizer() {
		analyzer = new MMSegAnalyzer();
	}
	
	@Override
	public List<String> tokenize(String text, String splitToken) {
		List<String> result = new ArrayList<String>();
		try {
			if(text == null || text.equals("")) {
				return result;
			}
			TokenStream tokenStream = analyzer.tokenStream("tags", text);
			tokenStream.reset();
		    CharTermAttribute termAtt = (CharTermAttribute)tokenStream.getAttribute(CharTermAttribute.class);
            while (tokenStream.incrementToken()) {
                String token = new String(termAtt.buffer(),0,termAtt.length());
                result.add(token);
            }
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NullPointerException e) {
			System.out.println(text + "_" + e.getMessage());
		}
		return result;
	}
	
	public List<String> dropStopWords(List<String> oldTokens) {
		List<String> newTokens = new ArrayList<String>();
		for (String token: oldTokens) {
			if (StopWordsHandler.isStopWord(token) == false) {
				newTokens.add(token);
			}
		}
		return newTokens;
	}
}
