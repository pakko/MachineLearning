package com.ml.bus.model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

public class News {
	@Id
	private String id;
	private String title;
	private String content;
	private String author;
	private Date date;
	private String categoryId;
	@Indexed(unique = true)
	private String url;
	private String img;
	private String source;
	private String originalCategory;	//用来测试准确率
	
	public News() {
		
	}
	
	public News(String id, String title, String content, String author,
			Date date, String categoryId, String url, String img, String source, String originalCategory) {
		this.id = id;
		this.title = title;
		this.content = content;
		this.author = author;
		this.date = date;
		this.categoryId = categoryId;
		this.url = url;
		this.img = img;
		this.source = source;
		this.originalCategory = originalCategory;
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	
	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
	}
	
	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}
	
	public String getOriginalCategory() {
		return originalCategory;
	}

	public void setOriginalCategory(String originalCategory) {
		this.originalCategory = originalCategory;
	}

	@Override
	public String toString() {
		return "News [id=" + id + ", title=" + title + ", content=" + content
				+ ", author=" + author + ", date=" + date + ", category="
				+ categoryId + ", url=" + url + ", img=" + img + ", source=" + source + ", originalCategory=" + originalCategory + "]";
	}
	
	
	
}
