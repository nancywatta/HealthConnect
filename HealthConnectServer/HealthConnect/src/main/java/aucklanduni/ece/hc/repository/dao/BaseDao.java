package aucklanduni.ece.hc.repository.dao;

import java.io.Serializable;
import java.util.List;

/**
 * 
* @ClassName: BaseDao 
* @Description: This is a Base class supporting basic DB actions
* usually we need your own Dao classes extending this baseDao
* @author Zhao Yuan
* @date 2014年9月15日 下午8:54:15 
* 
* @param <T>
 */
public interface BaseDao<T> {
	public void add(T t) throws Exception;

	public void update(T t) throws Exception;
	
	public void deleteById(Serializable id) throws Exception;
	
	public T findById(Serializable id) throws Exception;

	public List<T> findAll() throws Exception;
	
	public void executeUpdate(final String hql, final Object ... objects)  throws Exception;
	
	public int excuteUpdateBySql(String sql);
	
	public List<T> findBySql(String sql);
	
	public List<T> findByHql(String hql);
	
}
