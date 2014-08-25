package aucklanduni.ece.hc.service.impl;

import java.io.Serializable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;




import aucklanduni.ece.hc.repository.dao.BaseDao;
import aucklanduni.ece.hc.service.BaseService;


public class BaseServiceImpl<T> implements BaseService<T> {
	@Autowired
	private BaseDao<T> baseDao;

	public void add(T t) throws Exception {
		baseDao.add(t);
	}

	public void update(T t) throws Exception {
		baseDao.update(t);
	}

	public void deleteById(Serializable id) throws Exception {
		baseDao.deleteById(id);
	}

	public T findById(Serializable id) throws Exception {
		return baseDao.findById(id);
	}

	public List<T> findAll() throws Exception {
		return baseDao.findAll();
	}
}
