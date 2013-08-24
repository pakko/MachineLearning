package com.ml.db;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import com.ml.bus.model.Category;
import com.ml.bus.model.News;
import com.ml.util.Constants;


public class MyBatisDB implements IBaseDB{
	private SqlSessionFactory sqlSessionFactory;

	public MyBatisDB(Properties props) {
		try {
		
			Reader reader = Resources.getResourceAsReader(Constants.mybatisDBConfigFile);
			sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader, props);
		    reader.close();
		    
		} catch (IOException e) {
			System.out.println(e.toString());
		}

	}

	public MyBatisDB() {
		try {
			String confFile = Constants.dbConfigFolder + Constants.separator + "MySQL_127.0.0.1" + Constants.defaultDBConfigFileSuffix;

			Properties props = new Properties();
			try {
				props.load(new FileInputStream(confFile));
			} catch (FileNotFoundException e) {
				System.out.println(e.toString());
				return;
			} catch (IOException e) {
				System.out.println(e.toString());
				return;
			}
			Reader reader = Resources.getResourceAsReader(Constants.mybatisDBConfigFile);
			sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader, props);
		    reader.close();
		    
		} catch (IOException e) {
			System.out.println(e.toString());
		}

	}
	
	public static void main(String[] args) {
		String confFile = Constants.dbConfigFolder + Constants.separator + "MySQL_127.0.0.1" + Constants.defaultDBConfigFileSuffix;
		Properties props = new Properties();
		try {
			props.load(new FileInputStream(confFile));
		} catch (FileNotFoundException e) {
			System.out.println(e.toString());
			return;
		} catch (IOException e) {
			System.out.println(e.toString());
			return;
		}
		MyBatisDB p = new MyBatisDB(props);
		//System.out.println(p.findAll("com.ml.bus.mapper.NewsMapper.save"));
		News news = new News(null, "好声音", "来吧~", "浙江卫视",
				new Date(), "", "http://happy", "", "", "");
		
		p.save(news, "com.ml.bus.mapper.NewsMapper.save");
	}

	@Deprecated
	@Override
	public void save(Object entity) {
		
	}

	@Override
	public void save(Object entity, String mapperFunc) {
		SqlSession session = sqlSessionFactory.openSession();
		try {
		  session.insert(mapperFunc, entity);
		  session.commit();
		} finally {
		  session.close();
		}
	}

	@Deprecated
	@Override
	public void update(Object entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(Object param, String mapperFunc) {
		SqlSession session = sqlSessionFactory.openSession();
		try {
		  session.update(mapperFunc, param);
		  session.commit();
		} finally {
		  session.close();
		}
	}

	@Deprecated
	@Override
	public void delete(Object entity) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void delete(Object param, String mapperFunc) {
		SqlSession session = sqlSessionFactory.openSession();
		try {
		  session.delete(mapperFunc, param);
		  session.commit();
		} finally {
		  session.close();
		}
	}

	@Override
	public <T> List<T> find(Object param, Object mapperFunc) {
		SqlSession session = sqlSessionFactory.openSession();
		try {
		  return session.selectList((String) mapperFunc, param);
		} finally {
		  session.close();
		}
	}

	@Deprecated
	@Override
	public <T> List<T> find(Object param, Object mapperFunc, String collectionName) {
		SqlSession session = sqlSessionFactory.openSession();
		try {
		  return session.selectList((String) mapperFunc, param);
		} finally {
		  session.close();
		}
	}
	
	@Override
	public <T> T findOne(Object param, Object mapperFunc) {
		SqlSession session = sqlSessionFactory.openSession();
		try {
		  return session.selectOne((String) mapperFunc, param);
		} finally {
		  session.close();
		}
	}

	@Deprecated
	@Override
	public <T> T findOne(Object param, Object mapperFunc, String collectionName) {
		SqlSession session = sqlSessionFactory.openSession();
		try {
		  return session.selectOne((String) mapperFunc, param);
		} finally {
		  session.close();
		}
	}

	@Override
	public <T> List<T> findAll(Object mapperFunc) {
		SqlSession session = sqlSessionFactory.openSession();
		try {
		  return session.selectList((String) mapperFunc);
		} finally {
		  session.close();
		}
	}

	@Deprecated
	@Override
	public <T> List<T> findAll(Object mapperFunc, String collectionName) {
		SqlSession session = sqlSessionFactory.openSession();
		try {
		  return session.selectList((String) mapperFunc);
		} finally {
		  session.close();
		}
	}

	@Override
	public long count(Object query, String collectionName) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	

}
