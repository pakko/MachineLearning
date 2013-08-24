package com.ml.bus.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ml.bus.model.News;
import com.ml.bus.service.ClassifyService;
import com.ml.nlp.parser.SohuNewsParser;


@Controller
@RequestMapping(value = "/classify")
public class ClassifyController {

	    private static final Logger LOGGER = LoggerFactory.getLogger(ClassifyController.class);

	    @Autowired
	    ClassifyService classifyService;
	    
		
	    @RequestMapping(value = "/gid", method = RequestMethod.GET)
	    public @ResponseBody String generateIntermediateData() {
	    	long start = System.currentTimeMillis();
	    	classifyService.generateIntermediateData();
			long end = System.currentTimeMillis();
			
			String result = "耗时：" + (end -start);
			System.out.println(result);
			
			return result;
	    	
	    }
	    
	    @RequestMapping(value = "/train/{type}", method = RequestMethod.GET)
	    public @ResponseBody String trainingModel(@PathVariable("type") String type) {
	    	long start = System.currentTimeMillis();
	    	classifyService.trainingModel(type);
			long end = System.currentTimeMillis();
			
			String result = "耗时：" + (end -start);
			System.out.println(result);
			
			return result;
	    	
	    }
	    
	    @RequestMapping(value = "/test/{type}", method = RequestMethod.GET)
	    public @ResponseBody String testAccuracyWithBernoulli(@PathVariable("type") String type) {
	    	long start = System.currentTimeMillis();
	    	classifyService.testAccuracyWithBernoulli(type);
			long end = System.currentTimeMillis();
			
			String result = "耗时：" + (end -start);
			System.out.println(result);
			
			return result;
	    	
	    }
	    
	    @RequestMapping(value = "/get/{type}", method = RequestMethod.GET)
	    public @ResponseBody String classifyDocument(@PathVariable("type") String type,
	    		@RequestParam(value = "url", required = false) String url) {
	    	System.out.println(url);
	    	SohuNewsParser sohu = new SohuNewsParser();
	        News news = sohu.parse(url);
	    	long start = System.currentTimeMillis();

	    	String result = classifyService.classifyDocument(news.getContent(), type);
			long end = System.currentTimeMillis();
			
			long time = (end - start)/1000;
			
			result = "此项属于[" + result + "], 耗时：" + time;
			System.out.println(result);
			
			return result;
	    	
	    }
	    
}
