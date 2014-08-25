package aucklanduni.ece.hc.repository.dao;

import java.io.Serializable;
import java.util.List;

public interface BaseDao<T> {
	public void add(T t) throws Exception;

	public void update(T t) throws Exception;
	
	public void deleteById(Serializable id) throws Exception;
	
	public T findById(Serializable id) throws Exception;

	public List<T> findAll() throws Exception;
}
