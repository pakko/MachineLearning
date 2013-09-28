package com.ml.db;

import java.util.HashSet;
import java.util.Set;

import com.ml.bus.model.News;
import com.ml.util.Queue;

/**
 * 用来保存待解析的新闻的类
 */
public class ParserDB {
	// 待解析的新闻集合
	private static Set<String> parsedNews = new HashSet<String>();
	private static Queue<News> unParsedNews = new Queue<News>();

	public static void addParserNews(String url) {
		parsedNews.add(url);
	}
	
	public static Queue<News> getParsedNews() {
		return unParsedNews;
	}

	public static News unParsedNewsDeQueue() {
		return unParsedNews.deQueue();
	}

	public static void addUnParsedNews(News news) {
		String url = news.getUrl();
		if (url != null && !url.trim().equals("")
				&& !parsedNews.contains(url)
				&& !unParsedNews.contians(news))
			unParsedNews.enQueue(news);
	}

	public static boolean unParsedNewsIsEmpty() {
		return unParsedNews.empty();
	}
	
	public static int getUnParsedNewsNum() {
		return unParsedNews.size();
	}

}