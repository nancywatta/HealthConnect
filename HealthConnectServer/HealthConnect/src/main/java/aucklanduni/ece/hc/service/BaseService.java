package aucklanduni.ece.hc.service;

import java.io.Serializable;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;
@Transactional
public interface BaseService<T> {
	public void add(T t) throws Exception;

	public void update(T t) throws Exception;

	public void deleteById(Serializable id) throws Exception;
	
	public T findById(Serializable id) throws Exception;
	
	public List<T> findAll() throws Exception;
	
	public void executeUpdate(final String hql, final Object ... objects)throws Exception;

	public int excuteUpdateBySql(String sql) throws Exception ;
	public List findBySql(String sql);
	public List findByHql(String hql) ;
}
