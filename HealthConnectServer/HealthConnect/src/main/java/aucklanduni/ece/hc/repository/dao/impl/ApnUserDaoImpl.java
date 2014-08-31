package aucklanduni.ece.hc.repository.dao.impl;

import org.springframework.stereotype.Repository;

import aucklanduni.ece.hc.repository.dao.ApnUserDao;
import aucklanduni.ece.hc.repository.model.ApnUser;

@Repository
public class ApnUserDaoImpl extends BaseDaoImpl<ApnUser> implements ApnUserDao{
	public int myOwnSql(){
		return this.findBySql("select * from apn_user where username='ua28'").size();
	}
	
	public int myOwnHql(){
		return this.findByHql("from ApnUser where username='ua28'").size();
	}
}
