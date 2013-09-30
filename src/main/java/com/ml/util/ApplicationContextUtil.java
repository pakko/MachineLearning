package com.ml.util;

import org.quartz.JobExecutionContext;
import org.quartz.SchedulerContext;
import org.quartz.SchedulerException;
import org.springframework.context.ApplicationContext;

public class ApplicationContextUtil {
	private static final String APPLICATION_CONTEXT_KEY = "applicationContext";

	public static ApplicationContext getQuartzApplicationContext(
			JobExecutionContext context) {
		
		SchedulerContext schedulerContext = null;
		try {
			schedulerContext = context.getScheduler().getContext();
		} catch (SchedulerException e) {
			System.err.println("Error of get application context, " + e.getMessage());
		}

		ApplicationContext appContext = (ApplicationContext) schedulerContext
				.get(APPLICATION_CONTEXT_KEY);
		return appContext;
	}
}
