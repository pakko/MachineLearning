package com.ml.nlp.crawler;

import java.util.Set;

import com.ml.db.LinkDB;

public class Crawler {
	
	public void crawlingNews(String url, String pattern) {
		LinkFilter linkFilter = new CustomLinkFilter(pattern);
		Set<String> links = HtmlParser.extracLinks(url, linkFilter);
		// 新的未访问的 URL 入队
		for (String link : links) {
			LinkDB.addUnVisitedUrl(url, link);
		}
		
	}

	class CustomLinkFilter implements LinkFilter {
		
		private String pattern;
		
		CustomLinkFilter(String pattern) {
			this.pattern = pattern;
		}
		public boolean accept(String url) {
			if (url.matches(pattern)) {
				return true;
			} else {
				return false;
			}
		}
		
	}
	
	// main 方法入口
	public static void main(String[] args) {
		Crawler crawler = new Crawler();
		String name = "http://roll.news.sina.com.cn/news/gnxw/gdxw1/index.shtml";
		String name2 = "http://news.sohu.com/s2005/shishi.shtml";
		String pattern = "http://news.sina.com.cn/[\\w]+/([\\d]{4}-[\\d]{2}-[\\d]{2})+/[\\d]+.shtml";
		String pattern2 = "http://news.sohu.com/[\\d]+/n[\\d]+.shtml";
		crawler.crawlingNews(name2, pattern2);
		
		int i = 0;
		System.out.println(LinkDB.getUnVisitedUrl(name2).size());
		while(!LinkDB.unVisitedUrlIsEmpty(name2) && i < 1000) {
			i++;
			System.out.println(LinkDB.unVisitedUrlDeQueue(name2));
		}
	}
}