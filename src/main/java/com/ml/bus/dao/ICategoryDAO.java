package com.ml.bus.dao;

import java.util.List;

import com.ml.bus.model.Category;

public interface ICategoryDAO {
	List<Category> findAll();
	void save(Category category);
}
