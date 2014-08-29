package aucklanduni.ece.hc.repository.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import org.springframework.stereotype.Repository;

import aucklanduni.ece.hc.repository.dao.MemberDao;
import aucklanduni.ece.hc.repository.model.Account;
import aucklanduni.ece.hc.repository.model.Dictionary;
import aucklanduni.ece.hc.repository.model.Member;

@Repository
public class MemberDaoImpl extends BaseDaoImpl<Member> implements MemberDao{
	
	public ArrayList<Account> GetMembers(Connection connection, long groupId) throws Exception
	{
		ArrayList<Account> accountData = new ArrayList<Account>();
		try
		{
			PreparedStatement ps = connection.prepareStatement(
					"SELECT a.id AS accountId, a.email, a.username, d.* "
					+ "FROM ACCOUNT a "
					+ "INNER JOIN MEMBER m "
					+ "ON "
					+ "a.id=m.account_id "
					+ "and m.group_id= " + groupId
					+ " INNER JOIN DICTIONARY d "
					+ "ON "
					+ "m.role_id=d.id");
			ResultSet rs = ps.executeQuery();
			while(rs.next())
			{
				Account accountObject = new Account();
				accountObject.setId(rs.getLong("accountId"));
				accountObject.setEmail(rs.getString("email"));
				accountObject.setUsername(rs.getString("username"));
				Dictionary roleObject = new Dictionary();
				roleObject.setId(rs.getLong("id"));
				roleObject.setType(rs.getString("type"));
				roleObject.setValue(rs.getString("value"));
				roleObject.setName(rs.getString("name"));
				roleObject.setDescription(rs.getString("description"));
				accountObject.setRole(roleObject);
				accountData.add(accountObject);
			}
			return accountData;
		}
		catch(Exception e)
		{
			throw e;
		}
	}
	
	public String GetMemberRole(Connection connection, long accountId, long groupId) throws Exception
	{
		String roleName = "";
		try
		{
			PreparedStatement ps = connection.prepareStatement(
					"SELECT d.name "
					+ "FROM DICTIONARY d "
					+ "INNER JOIN MEMBER m "
					+ "ON "
					+ "m.role_id=d.id "
					+ "and m.group_id= " + groupId
					+ " and m.account_id= " + accountId);
			ResultSet rs = ps.executeQuery();
			while(rs.next())
			{
				roleName = rs.getString("name");
			}
		}
		catch(Exception e)
		{
			throw e;
		}
		return roleName;
		
	}
		
}
