package com.ml.bus.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ml.bus.model.Category;
import com.ml.bus.model.Cluster;
import com.ml.bus.service.CategoryService;
import com.ml.bus.service.ClusterService;

@Controller
@RequestMapping(value = "/category")
public class CategoryController {

	    @Autowired
	    CategoryService categoryService;
	    @Autowired
	    private ClusterService clusterService;
	    
	    @RequestMapping(value = "", method = RequestMethod.GET)
	    public @ResponseBody List<Category> get() {
	    	
	    	return categoryService.findAll();
	    	
	    }
	    
	    @RequestMapping(value = "show", method = RequestMethod.GET)
	    public @ResponseBody List<Map<String, Object>> showCluster() throws Exception {
	    	long start = System.currentTimeMillis();
	    	List<Cluster> clusterList = clusterService.findAll();
	    	List<Category> categoryList = categoryService.findAll();
	    	
	    	List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
	    	for(Category category: categoryList) {
	    		List<Cluster> clusters = getClustersByCategory(clusterList, category.getId());
	    		Map<String, Object> map = new HashMap<String, Object>();
	    		map.put("categoryId", category.getId());
	    		map.put("categoryName", category.getName());
	    		map.put("clusters", clusters);
	    		result.add(map);
	    	}
			long end = System.currentTimeMillis();
			System.out.println("耗时：" + (end -start));
			
			return result;
	    }
	    
	    private List<Cluster> getClustersByCategory(List<Cluster> clusterList, String categoryId) {
	    	List<Cluster> clusters = new ArrayList<Cluster>();
	    	for(Cluster cluster: clusterList) {
	    		if(categoryId.equals(cluster.getCategoryId())){
	    			clusters.add(cluster);
	    		}
	    	}
	    	return clusters;
	    }
}
