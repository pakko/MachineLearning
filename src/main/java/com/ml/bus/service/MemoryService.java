package com.ml.bus.service;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ml.bus.dao.ICategoryDAO;
import com.ml.bus.dao.ICrawlPatternDAO;
import com.ml.bus.dao.INewsDAO;
import com.ml.bus.model.CrawlPattern;
import com.ml.bus.model.News;
import com.ml.db.LinkDB;
import com.ml.nlp.classify.TrainnedModel;
import com.ml.util.Constants;


@Service
public class MemoryService {
	
	@Autowired
	ICrawlPatternDAO crawlPatternDAO;
	
	@Autowired
	ICategoryDAO categoryDAO;
	
	@Autowired
	INewsDAO newsDAO;
	
	private List<CrawlPattern> crawlPatterns;
	private TrainnedModel model;

	Map<String, String> categoryUrl;
	
	@PostConstruct 
    public void init(){ 
		long start = System.currentTimeMillis();
		
		crawlPatterns = crawlPatternDAO.findAll();
		List<News> news = newsDAO.findAll();
		
		inititVisitedUrl(news);
		
		//later added to memcached
		loadModel(Constants.defaultMultinomialModelFile);
		
		initCategoryUrl();
		calculateNews(news);
		
		long end = System.currentTimeMillis();
		
		System.out.println("Complete init memory datasets. Classified News size: " + news.size());
		System.out.println("Cost time:" + (end - start) );
		
		news.clear();
		news = null;
    }

	private void calculateNews(List<News> news) {
		double i = 0;
		for(News n: news) {
			String categoryId = n.getCategoryId();
			String originCategory = categoryUrl.get(n.getOriginalCategory());
			if(originCategory.equals(categoryId)){
				i++;
			}
		}
		System.out.println("Accuracy:" + (i / news.size()) );
		
	}
	
	private void initCategoryUrl() {
		categoryUrl = new HashMap<String, String>();
		categoryUrl.put("auto", "C000007");
		categoryUrl.put("business", "C000008");
		categoryUrl.put("it", "C000010");
		categoryUrl.put("health", "C000013");
		categoryUrl.put("sports", "C000014");
		categoryUrl.put("travel", "C000016");
		categoryUrl.put("learning", "C000020");
		categoryUrl.put("job", "C000022");
		categoryUrl.put("cul", "C000023");
		categoryUrl.put("mil", "C000024");
		
	}


	private void inititVisitedUrl(List<News> news){
		//initial visited url
		for(News n: news) {
			LinkDB.addVisitedUrl(n.getUrl());
		}
	}
	
	@SuppressWarnings("resource")
	private void loadModel(final String modelFile) {
        try {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(
                    modelFile));
            model = (TrainnedModel) in.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	
	public List<CrawlPattern> getCrawlPatterns() {
		return crawlPatterns;
	}

	public void setCrawlPatterns(List<CrawlPattern> crawlPatterns) {
		this.crawlPatterns = crawlPatterns;
	}
	
	public TrainnedModel getModel() {
		return model;
	}

	public void setModel(TrainnedModel model) {
		this.model = model;
	}

	
}
