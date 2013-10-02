package com.ml.bus.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ml.bus.model.News;
import com.ml.bus.service.NewsService;
import com.ml.util.Pagination;


@Controller
@RequestMapping(value = "/news")
public class NewsController {

	    @Autowired
	    private NewsService newsService;
	    
	    @RequestMapping(value = "", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	    public @ResponseBody Map<String, Object> news(
	    		@RequestParam(value = "categoryId", required = false) String categoryId,
	    		@RequestParam(value = "clusterId", required = false) String clusterId,
	    		HttpServletRequest servletRequest) {
	    	Pagination pager = new Pagination(servletRequest);
	    	
	    	if(categoryId == null || categoryId.equals("")) {
	    		pager = newsService.findByPage(pager);
	    	}
	    	else if(categoryId != null && !categoryId.equals("")
	    			&& (clusterId == null || clusterId.equals(""))) {
	    		pager = newsService.findByCategoryAndPage(categoryId, pager);
	    	}
	    	else if(categoryId != null && !categoryId.equals("")
	    			&& clusterId != null && !clusterId.equals("")) {
	    		pager = newsService.findByCategorysAndPage(categoryId, clusterId, pager);
	    	}
	    	
	    	@SuppressWarnings("unchecked")
			List<News> news = (List<News>) pager.getItems();
	    	
	    	Map<String, Object> result = new HashMap<String, Object>();
			result.put("totalPage", pager.getTotalPage());
			result.put("currentPage", pager.getCurrentPage());
			result.put("totalCount", pager.getTotalCount());
			result.put("rows", news);
			return result;
	    }
	   
}
