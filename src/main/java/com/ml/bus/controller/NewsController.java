package com.ml.bus.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ml.bus.model.Category;
import com.ml.bus.model.News;
import com.ml.bus.service.NewsService;
import com.ml.util.Pagination;


@Controller
@RequestMapping(value = "/news")
public class NewsController {

	    private static final Logger logger = LoggerFactory.getLogger(NewsController.class);

	    @Autowired
	    private NewsService newsService;
	    
	    @RequestMapping(value = "", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	    public @ResponseBody Map<String, Object> news(
	    		@RequestParam(value = "categoryId", required = false) String categoryId,
	    		HttpServletRequest servletRequest) {
	    	Pagination pager = new Pagination(servletRequest);
	    	
	    	if(categoryId == null || categoryId.equals("")) {
	    		pager = newsService.findByPage(pager);
	    	}
	    	else {
	    		pager = newsService.findByCategoryAndPage(categoryId, pager);
	    	}
	    	
	    	List<News> news = (List<News>) pager.getItems();
	    	
	    	Map<String, Object> result = new HashMap<String, Object>();
			result.put("totalPage", pager.getTotalPage());
			result.put("currentPage", pager.getCurrentPage());
			result.put("totalCount", pager.getTotalCount());
			result.put("rows", news);
			return result;
	    }
	    
	    
	    @RequestMapping(value = "/nsave", method = RequestMethod.GET)
	    public @ResponseBody String newsave() {
	    	long start = System.currentTimeMillis();
	    	News news = new News(null, "好声音", "来吧~", "浙江卫视",
					new Date(), "", "http://happy", "", "", "");
			newsService.save(news);
			long end = System.currentTimeMillis();
			
			String result = "耗时：" + (end -start);
			System.out.println(result);
			
			return result;
	    	
	    }
	   
}
