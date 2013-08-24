package com.ml.bus.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import com.ml.bus.model.Category;

@Repository
public interface CategoryMapper {
	
	final String SELECT_ALL = "SELECT * FROM category";
	final String INSERT = "INSERT INTO category(id, name) VALUES(#{id}, #{name})";
	
	@Select(SELECT_ALL)
	List<Category> findAll();
	
	@Insert(INSERT)
	void save(Category category);
	
	
}