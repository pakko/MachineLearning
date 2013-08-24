package com.ml.bus.controller;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ml.bus.model.Category;
import com.ml.bus.model.CrawlPattern;
import com.ml.bus.service.CategoryService;
import com.ml.bus.service.CrawlPatternService;
import com.ml.util.Constants;


@Controller
@RequestMapping(value = "/category")
public class CategoryController {

	    private static final Logger logger = LoggerFactory.getLogger(CategoryController.class);

	    @Autowired
	    CategoryService categoryService;
		@Autowired
		CrawlPatternService crawlPatternService;
	    @RequestMapping(value = "", method = RequestMethod.GET)
	    public @ResponseBody List<Category> get() {
	    	
	    	return categoryService.findAll();
	    	
	    }
	    
		private static String defaultPath = "C:\\soft\\TrainingSet2";

	    @RequestMapping(value = "/saveCategory", method = RequestMethod.GET)
	    public @ResponseBody String saveCategory() throws Exception {
	    	
	    	String filePath = defaultPath + "\\ClassList.txt";
			String text = getText(filePath, " ");
			String[] lines = text.split(" ");
			for(String line: lines) {
				String[] l = line.split("\t");
				Category category = new Category(l[0], l[1]);
				categoryService.save(category);
			}
	    	return "ok";
	    }
	    
	    @RequestMapping(value = "/savePattern", method = RequestMethod.GET)
	    public @ResponseBody String savePattern() throws Exception {
	    	
	    	Map<String, String> urlPatterns = new HashMap<String, String>();
			//urlPatterns.put("http://news.sohu.com", "http://news.sohu.com/[\\d]+/n[\\d]+.shtml");
			urlPatterns.put("http://it.sohu.com", "http://it.sohu.com/[\\d]+/n[\\d]+.shtml");
			urlPatterns.put("http://learning.sohu.com", "http://learning.sohu.com/[\\d]+/n[\\d]+.shtml");
			urlPatterns.put("http://travel.sohu.com", "http://travel.sohu.com/[\\d]+/n[\\d]+.shtml");
			urlPatterns.put("http://health.sohu.com", "http://health.sohu.com/[\\d]+/n[\\d]+.shtml");
			for(String url: urlPatterns.keySet()) {
				CrawlPattern cp = new CrawlPattern(null, url, urlPatterns.get(url), "sohu");
				crawlPatternService.save(cp);
			}
	    	return "ok";
	    }
	   
	    private String getText(String filePath, String token) throws Exception {

			InputStreamReader isReader = new InputStreamReader(new FileInputStream(
					filePath), "GBK");
			BufferedReader reader = new BufferedReader(isReader);
			String aline;
			StringBuilder sb = new StringBuilder();

			while ((aline = reader.readLine()) != null) {
				sb.append(aline.trim() + token);
			}
			isReader.close();
			reader.close();
			return sb.toString();
		}
}
