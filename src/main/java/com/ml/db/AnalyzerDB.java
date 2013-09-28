package com.ml.db;

import java.util.List;

import com.ml.bus.model.News;
import com.ml.util.Queue;

/**
 * 用来保存待分析的新闻的类
 */
public class AnalyzerDB {

	// 待分析的新闻集合
	private static Queue<List<News>> unAnalysizedNews = new Queue<List<News>>();


	public static List<News> unAnalysizedNewsDeQueue() {
		return unAnalysizedNews.deQueue();
	}

	public static void addUnAnalysizedNews(List<News> newsList) {
		if (newsList != null)
			unAnalysizedNews.enQueue(newsList);
	}


	public static boolean unAnalysizedNewsIsEmpty() {
		return unAnalysizedNews.empty();
	}
	
	public static int getUnAnalysizedNewsNum() {
		return unAnalysizedNews.size();
	}

}