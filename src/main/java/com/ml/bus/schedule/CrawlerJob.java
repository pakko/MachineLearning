package com.ml.bus.schedule;

import java.util.List;
import java.util.Map;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.ml.bus.model.CrawlPattern;
import com.ml.bus.service.MemoryService;
import com.ml.db.LinkDB;
import com.ml.nlp.crawler.Crawler;
import com.ml.util.Queue;

public class CrawlerJob extends QuartzJobBean {

	private static final Logger LOGGER = LoggerFactory.getLogger(CrawlerJob.class);
    
	private MemoryService memoryService;
	
    @Override
	protected void executeInternal(JobExecutionContext context)
			throws JobExecutionException {
		LOGGER.info("Starting to crawling ......");
		
		Map<?, ?> dataMap = context.getJobDetail().getJobDataMap();
		ApplicationContext ctx = (ApplicationContext)dataMap.get("applicationContext");
		
		memoryService = (MemoryService)ctx.getBean("memoryService");
		List<CrawlPattern> crawlPatterns = memoryService.getCrawlPatterns();
		
		Crawler crawler = new Crawler();
		
		for(CrawlPattern cp: crawlPatterns) {
			String crawlUrl = cp.getCrawlUrl();
			String crawPattern = cp.getPatternUrl();
			crawler.crawlingNews(crawlUrl, crawPattern);
			
			Queue<String> unVisitedUrlQueue = LinkDB.getUnVisitedUrl(crawlUrl);
			int unVisitedSize = 0;
			if(unVisitedUrlQueue != null) {
				unVisitedSize = unVisitedUrlQueue.size();
			}
			LOGGER.info("Queue: " + crawlUrl + " --- Unvisited url size: " + unVisitedSize);
		}
		LOGGER.info("Visited url size: " + LinkDB.getVisitedUrlNum());
		

		
		LOGGER.info("End of crawling");
	}
}
