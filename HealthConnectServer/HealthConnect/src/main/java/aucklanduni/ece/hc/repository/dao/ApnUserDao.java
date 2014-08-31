package aucklanduni.ece.hc.repository.dao;
import aucklanduni.ece.hc.repository.model.ApnUser;


public  interface ApnUserDao extends BaseDao<ApnUser> {
	public int myOwnSql();
	public int myOwnHql();
}
