package com.ml.bus.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.ml.bus.mapper.CategoryMapper;
import com.ml.bus.model.Category;

public class CategoryMybatisDAO implements ICategoryDAO {
	
	@Autowired
	CategoryMapper categoryMapper;
	
	public List<Category> findAll() {
		return categoryMapper.findAll();
	}
	
	public void save(Category category) {
		categoryMapper.save(category);
	}
}
