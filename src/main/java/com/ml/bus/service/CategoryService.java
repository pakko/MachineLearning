package com.ml.bus.service;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ml.bus.dao.ICategoryDAO;
import com.ml.bus.model.Category;

@Service
public class CategoryService {

	@Autowired
	ICategoryDAO categoryDAO;

	public List<Category> findAll() {
		return categoryDAO.findAll();
	}

	public void save(Category category) {
		categoryDAO.save(category);
	}
	
}
