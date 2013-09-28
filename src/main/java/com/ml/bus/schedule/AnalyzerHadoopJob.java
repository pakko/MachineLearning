package com.ml.bus.schedule;


import java.util.ArrayList;
import java.util.List;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.ml.bus.model.News;
import com.ml.db.AnalyzerDB;
import com.ml.db.ParserDB;
import com.ml.util.Constants;

public class AnalyzerHadoopJob extends QuartzJobBean {

	private static final Logger logger = LoggerFactory.getLogger(AnalyzerHadoopJob.class);
    
    @Override
	protected void executeInternal(JobExecutionContext context)
			throws JobExecutionException {
		logger.info("Starting to analyzing ......");
		
    	List<News> newsList = new ArrayList<News>();
		// 循环迭代出连接，然后提取该连接中的新闻
		while(!ParserDB.unParsedNewsIsEmpty()) {
			News news = ParserDB.unParsedNewsDeQueue();
			if (news == null)
				continue;
			newsList.add(news);
			
			ParserDB.addParserNews(news.getUrl());
			// 达到阈值时提交
			if(newsList.size() == Constants.batchAnalyzeSize) {
				AnalyzerDB.addUnAnalysizedNews(new ArrayList<News>(newsList));
				newsList.clear();
			}
		}
		logger.info("End of analyzing");
    }

}
