package com.ml.bus.schedule;


import java.util.Map;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.ml.bus.model.News;
import com.ml.bus.service.MemoryService;
import com.ml.bus.service.NewsService;
import com.ml.db.ParserDB;
import com.ml.nlp.classify.MultiNomialNB;
import com.ml.nlp.classify.NaiveBayesClassifier;
import com.ml.nlp.classify.TrainnedModel;

public class AnalyzerJob extends QuartzJobBean {

	private static final Logger LOGGER = LoggerFactory.getLogger(AnalyzerJob.class);
    
	private MemoryService memoryService;
	private NewsService newsService;

	private NaiveBayesClassifier classifier;
	
    @Override
	protected void executeInternal(JobExecutionContext context)
			throws JobExecutionException {
		LOGGER.info("Starting to analyzing ......");
		
		Map<?, ?> dataMap = context.getJobDetail().getJobDataMap();
		ApplicationContext ctx = (ApplicationContext)dataMap.get("applicationContext");
		
		memoryService = (MemoryService)ctx.getBean("memoryService");
		newsService = (NewsService)ctx.getBean("newsService");
		
		TrainnedModel model = memoryService.getModel();
		
    	if(classifier == null) {
    		System.out.println("new classifier");
    		classifier = new MultiNomialNB(model);
    	}
		analyze(classifier, 0);
		
		LOGGER.info("End of analyzing");
	}
	
    private void analyze(NaiveBayesClassifier classifier, int limitNumber) {
    	int i = 0;
		// 循环迭代出连接，然后提取该连接中的新闻。limitNumber为0时不限制解析数量
		while(!ParserDB.unAnalysizedNewsEmpty() 
				&& (i < limitNumber || limitNumber == 0)) {
			
			News news = ParserDB.unAnalysizedNewsDeQueue();
			if (news == null)
				continue;

			long start = System.currentTimeMillis();
			String result = classifier.classify(news.getContent());// 进行分类
			long end = System.currentTimeMillis();
			long time = (end - start)/1000;
			
			System.out.println("此项属于[" + result + "], 耗时：" + time);
			if(result != null) {
				// 更新该条新闻的类别
				news.setCategoryId(result);
				newsService.save(news);
				
			}
			
			// 该 url 放入到已分析的新闻 中
			ParserDB.addAnalysizedNews(news.getUrl());
			
			i++;
		}
    }

}
