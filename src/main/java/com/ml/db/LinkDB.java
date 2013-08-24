package com.ml.db;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.ml.util.Queue;

/**
 * 用来保存已经访问过 Url 和待访问的 Url 的类
 */
public class LinkDB {

	//已访问的 url 集合
	private static Set<String> visitedUrl = new HashSet<String>();
	//待访问的 url 集合
	private static Map<String, Queue<String>> unVisitedQueueMap = new HashMap<String, Queue<String>>();
	
	
	public static Map<String, Queue<String>> getQueueMap() {
		return unVisitedQueueMap;
	}
	
	public static void addUnVisitedUrl(String queueName, String url) {
		Queue<String> q = unVisitedQueueMap.get(queueName);
		if(q == null) {
			q = new Queue<String>();
			unVisitedQueueMap.put(queueName, q);
		}
		if (url != null && !url.trim().equals("")
				 && !visitedUrl.contains(url)
								&& !q.contians(url)) {
			q.enQueue(url);
		}
	}
	
	public static Queue<String> getUnVisitedUrl(String queueName) {
		return unVisitedQueueMap.get(queueName);
	}

	public static String unVisitedUrlDeQueue(String queueName) {
		Queue<String> q = unVisitedQueueMap.get(queueName);
		if(q != null) {
			return q.deQueue();
		}
		return null;
	}

	public static boolean unVisitedUrlsEmpty(String queueName) {
		Queue<String> q = unVisitedQueueMap.get(queueName);
		if(q == null) {
			return true;
		}
		return q.empty();
	}
	
	public static int getVisitedUrlNum() {
		return visitedUrl.size();
	}

	public static void addVisitedUrl(String url) {
		visitedUrl.add(url);
	}

	public static void removeVisitedUrl(String url) {
		visitedUrl.remove(url);
	}
}