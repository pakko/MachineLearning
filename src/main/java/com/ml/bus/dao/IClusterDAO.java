package com.ml.bus.dao;

import java.util.List;

import com.ml.bus.model.Cluster;

public interface IClusterDAO {
	List<Cluster> findAll();
	List<Cluster> findByCategory(String categoryId);
	void save(Cluster cluster);
	void delete(Cluster cluster);
}
