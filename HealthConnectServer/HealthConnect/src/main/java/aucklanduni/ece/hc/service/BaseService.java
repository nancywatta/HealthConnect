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
}
