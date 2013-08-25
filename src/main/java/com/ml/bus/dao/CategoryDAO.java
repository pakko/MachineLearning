package com.ml.bus.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.ml.bus.model.Category;
import com.ml.db.IBaseDB;
import com.ml.util.Constants;

public class CategoryDAO implements ICategoryDAO {
	
	@Autowired
	IBaseDB baseDB;
	
	public List<Category> findAll() {
		return baseDB.findAll(Category.class, Constants.categoryCollectionName);
	}

	@Override
	public void save(Category category) {
		// TODO Auto-generated method stub
		
	}
}
