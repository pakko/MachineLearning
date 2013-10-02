package com.ml.bus.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.ml.bus.model.Cluster;
import com.ml.db.IBaseDB;
import com.ml.util.Constants;

public class ClusterDAO implements IClusterDAO {
	
	@Autowired
	IBaseDB baseDB;

	@Override
	public List<Cluster> findAll() {
		return baseDB.findAll(Cluster.class, Constants.clusterCollectionName);
	}

	@Override
	public List<Cluster> findByCategory(String categoryId) {
		Query query = new Query();
		query.addCriteria(Criteria.where("categoryId").is(categoryId));
		return baseDB.find(query, Cluster.class, Constants.clusterCollectionName);
	}

	@Override
	public void save(Cluster cluster) {
		baseDB.save(cluster, Constants.clusterCollectionName);
	}

	@Override
	public void delete(Cluster cluster) {
		baseDB.delete(cluster, Constants.clusterCollectionName);
	}
	
}
