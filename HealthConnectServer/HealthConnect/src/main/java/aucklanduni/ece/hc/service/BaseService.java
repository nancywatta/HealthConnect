package aucklanduni.ece.hc.service;

import java.io.Serializable;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;
/**
 * 
* @ClassName: BaseService 
* @Description: This is a base class for all service classes
* only containing basic methods
* @author Zhao Yuan
* @date 2014年9月15日 下午9:10:01 
* 
* @param <T>
 */
@Transactional
public interface BaseService<T> {
	public void add(T t) throws Exception;

	public void update(T t) throws Exception;

	public void deleteById(Serializable id) throws Exception;
	
	public T findById(Serializable id) throws Exception;
	
	public List<T> findAll() throws Exception;
	
	public void executeUpdate(final String hql, final Object ... objects)throws Exception;

	public int excuteUpdateBySql(String sql) throws Exception ;
	public List<T> findBySql(String sql);
	public List<T> findByHql(String hql) ;
}
