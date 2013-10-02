package com.ml.bus.schedule;

import java.util.List;
import java.util.Map;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.ml.bus.model.News;
import com.ml.bus.service.NewsService;
import com.ml.bus.service.SyncTaskService;
import com.ml.db.AnalyzerDB;
import com.ml.util.ApplicationContextUtil;
import com.ml.util.Constants;
import com.ml.util.ShortUrlUtil;

public class DoHadoopJob extends QuartzJobBean {

	private static final Logger logger = LoggerFactory.getLogger(DoHadoopJob.class);
	
	private NewsService newsService;
	private SyncTaskService syncTaskService;

	private String[] categoryIndex = new String[] {
			"C000007", "C000008", "C000010", "C000013",
			"C000014", "C000016", "C000020", "C000022",
			"C000023", "C000024"
	};
	
    @Override
	protected void executeInternal(JobExecutionContext context)
			throws JobExecutionException {
		logger.info("Starting to doing hadoop ......");
		
		// get app context
		ApplicationContext ctx = ApplicationContextUtil.getQuartzApplicationContext(context);
		newsService = (NewsService) ctx.getBean("newsService");
        syncTaskService = (SyncTaskService) ctx.getBean("syncTaskService");

        // analyze
		while(!AnalyzerDB.unAnalysizedNewsIsEmpty()) {
			System.out.println("Analyzer queue size: " + AnalyzerDB.getUnAnalysizedNewsNum());
			List<News> newsList = AnalyzerDB.unAnalysizedNewsDeQueue();
			if (newsList == null)
				continue;
			try {
				System.out.println("Begin to exec task");
				String result = syncTaskService.analyze(newsList);
				processResult(newsList, result);
				System.out.println("End to exec task");
			} catch (Exception e) {
				logger.error("Error of analyzing: ", e);
			}
		}
		logger.info("End of doing hadoop");
    }
    
    private void processResult(List<News> newsList, String result) {
    	Map<String, Integer> resultMap = syncTaskService.resolveResult(result);
		System.out.println("news size " + newsList.size());
		
		//save to db
		for(News news: newsList) {
			String key = ShortUrlUtil.shortText(news.getUrl())[0] + Constants.fileExt;
			int category = resultMap.get(key);
			news.setCategoryId(categoryIndex[category]);
			newsService.save(news);
		}
    }

}
