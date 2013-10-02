package com.ml.bus.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ml.bus.model.Category;
import com.ml.bus.model.Cluster;
import com.ml.bus.model.News;
import com.ml.bus.service.CategoryService;
import com.ml.bus.service.ClusterService;
import com.ml.bus.service.NewsService;
import com.ml.util.Constants;
import com.ml.util.ShortUrlUtil;


@Controller
@RequestMapping(value = "/cluster")
public class ClusterController {

	    @Autowired
	    private NewsService newsService;
	    
	    @Autowired
	    private ClusterService clusterService;
	    
	    @Autowired
	    CategoryService categoryService;
	    
	    @RequestMapping(value = "/{categoryId}", method = RequestMethod.GET)
	    public @ResponseBody String doCluster(@PathVariable("categoryId") String categoryId) throws Exception {
	    	long start = System.currentTimeMillis();
			List<News> newsList = newsService.findByCategory(categoryId);
			if(newsList.size() == 0)
				return "";
			String resultFile = clusterService.doCluster(newsList);
			System.out.println("result file: " + resultFile);
			Map<Integer, List<String>> resultMap = clusterService.resolveResult(resultFile);
			System.out.println("result map: " + resultMap.toString());

			//first drop the collection
			List<Cluster> clusters = clusterService.findByCategory(categoryId);
			for(Cluster cluster: clusters) {
				clusterService.delete(cluster);
			}
			long stamp = System.currentTimeMillis();
			for(Integer key: resultMap.keySet()) {
				String clusterId = stamp + "_" + key;
				List<String> valueList = resultMap.get(key);
				
				for(String value: valueList) {
					News news = this.getNewsByShortUrl(newsList, value);
					news.setClusterId(clusterId);
					newsService.saveOrUpdate(news);
				}
				Cluster cluster = new Cluster(clusterId, categoryId);
				clusterService.save(cluster);
				System.out.println(key + ": " + valueList.size());
			}
			long end = System.currentTimeMillis();
			System.out.println("耗时：" + (end -start));
			
			return "";
	    	
	    }
	    
	    
	    @RequestMapping(value = "", method = RequestMethod.GET)
	    public @ResponseBody String doAllCluster() throws Exception {
	    	long start = System.currentTimeMillis();
	    	List<Category> categoryList = categoryService.findAll();
			for(Category category: categoryList) {
				doCluster(category.getId());
			}
			long end = System.currentTimeMillis();
			System.out.println("耗时：" + (end -start));
			
			return "";
	    }
	    
	    private News getNewsByShortUrl(List<News> newsList, String url) {
	    	for(News news: newsList) {
	    		String shortUrl = ShortUrlUtil.shortText(news.getUrl())[0] + Constants.fileExt;
	    		if(url.equals(shortUrl)) {
	    			return news;
	    		}
	    	}
	    	return null;
	    }
	   
}
