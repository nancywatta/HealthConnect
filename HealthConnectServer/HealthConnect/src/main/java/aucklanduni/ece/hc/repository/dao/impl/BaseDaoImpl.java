package aucklanduni.ece.hc.repository.dao.impl;


import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.metadata.ClassMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import aucklanduni.ece.hc.repository.dao.BaseDao;


public class BaseDaoImpl<T> implements BaseDao<T>{

	@Autowired  
    @Qualifier("sessionFactory")  
    private SessionFactory sessionFactory;

	protected Class<T> getEntityClass(){
		@SuppressWarnings("unchecked")
		Class<T> entityClass = (Class<T>)((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[0];
		return entityClass;
	}

	public String getPkColunmName(){
		ClassMetadata meta = sessionFactory.getCurrentSession().getSessionFactory().getClassMetadata(getEntityClass());
		return meta.getIdentifierPropertyName();
	}
	

	protected String getEntityClassName() {
		ClassMetadata meta = sessionFactory.getCurrentSession().getSessionFactory().getClassMetadata(getEntityClass());
		return meta.getEntityName();
	}
	
	public void add(T t) throws Exception {
		sessionFactory.getCurrentSession().save(t);
	}

	public void update(T t) throws Exception {
		sessionFactory.getCurrentSession().update(t);
	}

	public void deleteById(Serializable id) throws Exception {
		String hql = "delete " + getEntityClassName() + " where "+getPkColunmName()+"=?";
		executeUpdate(hql,id);
	}

	@SuppressWarnings("unchecked")
	public T findById(Serializable id) throws Exception {
		return (T)sessionFactory.getCurrentSession().get(getEntityClass(), id);
	}

	@SuppressWarnings("unchecked")
	public List<T> findAll() throws Exception {
		String hql = "from " + getEntityClassName();
		System.out.println(getEntityClassName());
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		return query.list();
		
	}

	public void executeUpdate(final String hql, final Object ... objects)  throws Exception{
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		for (int i = 0; i < objects.length; i++) {
			query.setParameter(i, objects[i]);
		}
		query.executeUpdate();
	}
    
    public int excuteUpdateBySql(String sql) {    
        Query query =sessionFactory.getCurrentSession().createSQLQuery(sql);
        return query.executeUpdate();    
    }    


	public Session getSession() {    
        return sessionFactory.getCurrentSession();    
    }    
    
    @SuppressWarnings("rawtypes")
	public List findBySql(String sql) {    
        @SuppressWarnings("unchecked")
		List<Object[]> list = getSession().createSQLQuery(sql).list();    
        return list;    
    }    
      
    @SuppressWarnings("rawtypes")
	public List findByHql(String hql) {    
        @SuppressWarnings("unchecked")
		List<Object[]> list = getSession().createQuery(hql).list();    
        return list;    
    }


}
