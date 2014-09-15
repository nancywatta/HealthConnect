package aucklanduni.ece.hc.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import aucklanduni.ece.hc.repository.dao.ApnUserDao;
import aucklanduni.ece.hc.repository.model.ApnUser;
import aucklanduni.ece.hc.service.ApnUserService;
/**
 * 
* @ClassName: ApnUserServiceImpl 
* @Description: This is a demo showing how to implement a service
* Note that have to add the annotation @Service so that spring can autowire beans
* @author Zhao Yuan
* @date 2014年9月15日 下午9:11:27 
*
 */
@Service
public class ApnUserServiceImpl extends BaseServiceImpl<ApnUser> implements ApnUserService{
	@Autowired
	private ApnUserDao apnUserDao;
	
	public int executMySql() throws Exception {
		 return apnUserDao.myOwnSql();
		 
	}
	public int executMyHql() throws Exception {
		 return apnUserDao.myOwnHql();
		 
	}

}