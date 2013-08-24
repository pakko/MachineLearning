package com.ml.db;

import java.util.HashSet;
import java.util.Set;

import com.ml.bus.model.News;
import com.ml.util.Queue;

/**
 * 用来保存已经分析过的新闻和待分析的新闻的类
 */
public class ParserDB {

	// 已分析过的新闻集合
	private static Set<String> analysizedNews = new HashSet<String>();
	// 待分析的新闻集合
	private static Queue<News> unAnalysizedNews = new Queue<News>();

	public static Queue<News> getAnalysizedNews() {
		return unAnalysizedNews;
	}

	public static void addAnalysizedNews(String url) {
		analysizedNews.add(url);
	}

	public static void removeAnalysizedNews(String url) {
		analysizedNews.remove(url);
	}

	public static News unAnalysizedNewsDeQueue() {
		return unAnalysizedNews.deQueue();
	}

	// 保证每个 url 只被分析一次
	public static void addUnAnalysizedNews(News news) {
		String url = news.getUrl();
		if (url != null && !url.trim().equals("")
				&& !analysizedNews.contains(url)
				&& !unAnalysizedNews.contians(news))
			unAnalysizedNews.enQueue(news);
	}

	public static int getAnalysizedNewsNum() {
		return analysizedNews.size();
	}

	public static boolean unAnalysizedNewsEmpty() {
		return unAnalysizedNews.empty();
	}
	
	public static int getUnAnalysizedNewsNum() {
		return unAnalysizedNews.size();
	}

}