package aucklanduni.ece.hc.repository.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import aucklanduni.ece.hc.repository.dao.DictionaryDao;
import aucklanduni.ece.hc.repository.model.Dictionary;

@Repository
public class DictionaryDaoImpl  extends BaseDaoImpl<Dictionary> implements DictionaryDao{
	
	/**
	 * @Title: findByValue
	 * @Description: Function will return record from DICTIONARY table 
	 * for the TYPE as 'Role' and VALUE as input roleValue.
	 * 
	 * @param connection
	 * @param roleValue
	 * @return ArrayList<Dictionary>
	 * @throws Exception
	 */
	public ArrayList<Dictionary> findByValue(Connection connection, String roleValue) throws Exception
	{
		ArrayList<Dictionary> roleData = new ArrayList<Dictionary>();
		try
		{
			PreparedStatement ps = connection.prepareStatement(
					"SELECT d.id, d.value, d.name "
					+ "FROM DICTIONARY d "
					+ "WHERE d.type='Role' "
					+ "and d.value= '" + roleValue +"'");
			ResultSet rs = ps.executeQuery();
			while(rs.next())
			{
				Dictionary roleObject = new Dictionary();
				roleObject.setId(rs.getLong("id"));
				roleObject.setValue(rs.getString("value"));
				roleObject.setName(rs.getString("name"));
				roleData.add(roleObject);
			}
			
			return roleData;
		}
		catch(Exception e)
		{
			throw e;
		}
	}

	/**
	 * @Title: findRoleByAccountIdAndGroupId
	 * @Description: Function will return record from the DICTIONARY table 
	 * stating the role of the input accountId within the given groupId.
	 * 
	 * @param accountId
	 * @param groupId
	 * @return Dictionary
	 * @throws Exception
	 */
	public Dictionary findRoleByAccountIdAndGroupId(long accountId, long groupId)
			throws Exception {
		Session s=getSession();
		String hql="select role "+
				"from Dictionary role, Member m "+
				"where m.accountId=? and m.groupId=? and m.roleId=role.id";
		System.out.println(s.createQuery(hql).setParameter(0, accountId).setParameter(1, groupId).uniqueResult());
		return (Dictionary)s.createQuery(hql).setParameter(0, accountId).setParameter(1, groupId).uniqueResult();
	}
	
}
