package aucklanduni.ece.hc.repository.dao;
import aucklanduni.ece.hc.repository.model.ApnUser;

/**
 * 
* @ClassName: ApnUserDao 
* @Description: This demo interface is for the use of ApnUser
* usually we can add our own sql methods here and implement them in impl classes
* @author Zhao Yuan
* @date 2014年9月15日 下午8:51:45 
*
 */
public  interface ApnUserDao extends BaseDao<ApnUser> {
	public int myOwnSql();
	public int myOwnHql();
}
