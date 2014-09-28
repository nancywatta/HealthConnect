package aucklanduni.ece.hc.repository.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
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
					"SELECT a.id AS accountId, a.email, a.username, m.*, d.* "
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
				roleObject.setId(rs.getLong("d.id"));
				roleObject.setType(rs.getString("type"));
				roleObject.setValue(rs.getString("value"));
				roleObject.setName(rs.getString("name"));
				roleObject.setDescription(rs.getString("description"));
				accountObject.setRole(roleObject);
				Member member = new Member();
				member.setAccountId(rs.getLong("m.account_id"));
				member.setId(rs.getLong("m.id"));
				member.setGroupId(rs.getLong("m.group_id"));
				member.setRoleId(rs.getLong("m.role_id"));
				member.setCreateDate(rs.getDate("m.created_date"));
				member.setExpirationDate(rs.getDate("m.expiration_date"));
				member.setIsActive(rs.getString("m.isActive"));
				accountObject.setMemberDetails(member);
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
		String roleValue = "";
		try
		{
			PreparedStatement ps = connection.prepareStatement(
					"SELECT d.value "
					+ "FROM DICTIONARY d "
					+ "INNER JOIN MEMBER m "
					+ "ON "
					+ "m.role_id=d.id "
					+ "and m.group_id= " + groupId
					+ " and m.account_id= " + accountId);
			ResultSet rs = ps.executeQuery();
			while(rs.next())
			{
				roleValue = rs.getString("value");
			}
			return roleValue;
		}
		catch(Exception e)
		{
			throw e;
		}
		
	}
	
	public int checkPatientCount(Connection connection, long groupId) throws Exception {
		try
		{
			PreparedStatement ps = connection.prepareStatement(
					"SELECT COUNT(*) "
					+ "FROM MEMBER m "
					+ "INNER JOIN DICTIONARY d "
					+ "ON "
					+ "m.role_id=d.id "
					+ "and m.group_id= " + groupId
					+ " and d.type = 'Role' "
					+ "and d.value = 'P' ");
			ResultSet rs = ps.executeQuery();
			while(rs.next())
			{
				return rs.getInt(1);
			}
			return 0;
		}
		catch(Exception e)
		{
			throw e;
		}
	}

	public int checkSupportMemberCount(Connection connection, long groupId) throws Exception {
		try
		{
			PreparedStatement ps = connection.prepareStatement(
					"SELECT COUNT(*) "
					+ "FROM MEMBER m "
					+ "INNER JOIN DICTIONARY d "
					+ "ON "
					+ "m.role_id=d.id "
					+ "and m.group_id= " + groupId
					+ " and d.type = 'Role' "
					+ "and d.value = 'S' "
					+ "and m.isActive = 'Y'");
			ResultSet rs = ps.executeQuery();
			while(rs.next())
			{
				return rs.getInt(1);
			}
			return 0;
		}
		catch(Exception e)
		{
			throw e;
		}
	}
	
	public int checkMemberCount(Connection connection, long groupId) throws Exception {
		try
		{
			PreparedStatement ps = connection.prepareStatement(
					"SELECT COUNT(*) "
					+ "FROM MEMBER m "
					+ "WHERE "
					+ "m.group_id= " + groupId
					);
			ResultSet rs = ps.executeQuery();
			while(rs.next())
			{
				return rs.getInt(1);
			}
			return 0;
		}
		catch(Exception e)
		{
			throw e;
		}
	}
	
	public void saveMember(Connection connection, long groupId, long accountId, String emailId, long roleId) throws Exception {
		try
		{
			PreparedStatement ps = connection.prepareStatement(
					"INSERT "
							+ "INTO MEMBER "
							+ "(ACCOUNT_ID, GROUP_ID, ROLE_ID,CREATED_DATE) VALUES"
							+ "(?,?,?,?)" );
			ps.setLong(1, accountId);
			ps.setLong(2, groupId);
			ps.setLong(3, roleId);
			long time = System.currentTimeMillis();
			java.sql.Timestamp timestamp = new java.sql.Timestamp(time);
			ps.setTimestamp(4, timestamp);
			ps.executeUpdate();
		}
		catch(Exception e)
		{
			throw e;
		}
	}
	

	public void deleteMember(Connection connection, long groupId, long memberId) throws Exception {
		try
		{
			PreparedStatement ps = connection.prepareStatement(
					"DELETE "
							+ "FROM MEMBER"
							+ " WHERE "
							+ "ACCOUNT_ID = ?"
							+ " AND "
							+ "GROUP_ID = ?" );
			ps.setLong(1, memberId);
			ps.setLong(2, groupId);
			ps.executeUpdate();
		}
		catch(Exception e)
		{
			throw e;
		}
	}
	
	//empty the members of a group
	public void deleteAllMember(Connection connection, long groupId) throws Exception {
		try
		{
			PreparedStatement ps = connection.prepareStatement(
					"DELETE "
							+ "FROM MEMBER"
							+ " WHERE "
							+ "GROUP_ID = ?" );
			ps.setLong(1, groupId);
			ps.executeUpdate();
		}
		catch(Exception e)
		{
			throw e;
		}
	}

	public boolean isMember(long accountId, long groupId) throws Exception {
		Session s=getSession();
		String hql="select member "+
				"from Account acc, Group group, Member member "+
				"where acc.id=? and group.id=? and acc.id=member.accountId and group.id=member.groupId";		
		return (Boolean) s.createQuery(hql).setParameter(0, accountId).setParameter(1, groupId).uniqueResult();
	}

	public Member findByAccountIdAndGroupId(long accountId, long groupId)
			throws Exception {
		Session s=getSession();
		String hql="select member "+
				"from Member member "+
				"where member.accountId=? and member.groupId=?";
		return (Member)s.createQuery(hql).setParameter(0,accountId).setParameter(1,groupId).uniqueResult();
	}

	//Yalu
	public String getPatientName(Connection connection, long groupId)
			throws Exception {
		String patientName = "";
		try
		{
			PreparedStatement ps = connection.prepareStatement(
					"SELECT a.username FROM ACCOUNT a INNER JOIN MEMBER m ON a.id=m.account_id AND m.group_id= ? AND m.role_id=1");
			ps.setLong(1, groupId);
			ResultSet rs = ps.executeQuery();
			while(rs.next())
			{
				patientName = rs.getString("username");
				
			}
			return patientName;
		}
		catch(Exception e)
		{
			throw e;
		}
		
	}

	public ArrayList<Long> getGroupIdOfNurse(Connection connection,long accountId, long roleId)
			throws Exception {
		ArrayList<Long> groupIdList = new ArrayList<Long>();
		
		try{
			PreparedStatement ps = connection.prepareStatement(
					"SELECT group_id FROM member WHERE ACCOUNT_ID = ?  AND  ROLE_ID = ?" );
			ps.setLong(1, accountId);
			ps.setLong(2, roleId);
			ResultSet rs = ps.executeQuery();
			while(rs.next())
			{
				long groupId = 0;
				groupId = rs.getLong("group_id");
				groupIdList.add(groupId);
			}
			return groupIdList;
		}
		catch(Exception e)
		{
			throw e;
		}
		
	}

	public List<Account> findAllMembersInGroup(long groupId) throws Exception {
		Session s=getSession();
		String hql="select acc "
				+"from Account acc, Member m "
				+"where acc.id=m.accountId and m.groupId=?";
		
		return (List<Account>)s.createQuery(hql).setParameter(0, groupId).list();
	}
	
	
		
}
