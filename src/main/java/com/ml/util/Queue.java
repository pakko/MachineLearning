package com.ml.util;

import java.util.concurrent.LinkedBlockingQueue;


/**
 * 数据结构队列
 */
public class Queue<T> {

	private LinkedBlockingQueue<T> queue = new LinkedBlockingQueue<T>();

	public void enQueue(T t) {
		try {
			queue.put(t);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public T deQueue() {
		try {
			return queue.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return null;
	}

	public boolean isQueueEmpty() {
		return queue.isEmpty();
	}

	public boolean contians(T t) {
		return queue.contains(t);
	}

	public boolean empty() {
		return queue.isEmpty();
	}

	public int size() {
		return queue.size();
	}
}