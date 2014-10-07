package aucklanduni.ece.hc.repository.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import aucklanduni.ece.hc.repository.dao.AccountDao;
import aucklanduni.ece.hc.repository.model.Account;


@Repository
public class AccountDaoImpl  extends BaseDaoImpl<Account> implements AccountDao{

	/**
	 * @Title: getAccountByEmail
	 * @Description: Function will return account details based on input emailId
	 * from the ACCOUNT table.
	 * 
	 * @param connection
	 * @param emailId
	 * @return Account
	 * @throws Exception
	 */
	public Account getAccountByEmail(Connection connection, String emailId) throws Exception {
		try
		{
			PreparedStatement ps = connection.prepareStatement(
					"SELECT a.* "
							+ "FROM ACCOUNT a "
							+ "WHERE "
							+ "a.email='" + emailId + "'");
			ResultSet rs = ps.executeQuery();
			while(rs.next())
			{
				Account account = new Account();
				account.setEmail(rs.getString("email"));
				account.setId(rs.getLong("id"));
				return account;
			}
			return null;
		}
		catch(Exception e)
		{
			throw e;
		}
	}

	/**
	 * @Title: createAccount
	 * @Description: Function will insert record into ACCOUNT table.
	 * 
	 * @param connection
	 * @param emailId
	 * @param password
	 * @param userName
	 * @throws Exception
	 */
	public void createAccount(Connection connection, String emailId, String password, String userName)throws Exception {
		try
		{
			PreparedStatement ps = connection.prepareStatement(
					"INSERT "
							+ "INTO ACCOUNT "
							+ "(USERNAME, EMAIL, PASSWORD, CREATED_DATE) VALUES"
							+ "(?,?,?,?)" );
			ps.setString(1, userName);
			ps.setString(2, emailId);
			ps.setString(3, password);
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
	
	/**
	 * @Title: getAccbyEmailPswd
	 * @Description: Function will return account details based on input emailId 
	 * and password from the ACCOUNT table.
	 * 
	 * @param connection
	 * @param emailId
	 * @param password
	 * @return Account
	 * @throws Exception
	 */
	public Account getAccbyEmailPswd(Connection connection, String emailId, String password) throws Exception{
		try
		{
			PreparedStatement ps = connection.prepareStatement(
					"SELECT a.* "
							+ "FROM ACCOUNT a "
							+ "WHERE "
							+ "a.email='" + emailId + "'"
							+ " and a.password='" + password + "'");
			ResultSet rs = ps.executeQuery();
			while(rs.next())
			{
				Account account = new Account();
				account.setEmail(rs.getString("email"));
				account.setId(rs.getLong("id"));
				return account;
			}
			return null;
		}
		catch(Exception e)
		{
			throw e;
		}
	}
	
	
	//Ben 09/2014
	/**
	 * @Title: getEmailByAccountId
	 * @Description: Function will return emailId based on input accountId 
	 * from the ACCOUNT table.
	 * 
	 * @param connection
	 * @param accountId
	 * @return String
	 * @throws Exception
	 */
	public String getEmailByAccountId(Connection connection, long accountId) throws Exception {
		try
		{
			PreparedStatement ps = connection.prepareStatement(
					"SELECT a.email "
							+ "FROM ACCOUNT a "
							+ "WHERE "
							+ "a.id='" + accountId + "'");
			ResultSet rs = ps.executeQuery();
			String emailAdd = "";
			while(rs.next())
			{emailAdd = rs.getString("email");}
			return emailAdd;
		}
		catch(Exception e)
		{
			throw e;
		}
	}
	
	//Wu
	/**
	 * @Title: getAccbyAppointmentId
	 * @Description: Function will return account details of all users 
	 * with which the given appointment is shared.
	 * 
	 * @param appointmentId
	 * @return List<Account>
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Account> getAccbyAppointmentId(long appointmentId) throws Exception {
		Session s=getSession();
		String hql="select acc "+
				"from Account acc, AppointmentAccountRef ref, Appointment app "+
				"where app.id=? and app.id=ref.appointmentId and acc.id=ref.accountId and app.sharedType='M'";
		List<Account> accounts = (List<Account>) s.createQuery(hql).setParameter(0, appointmentId).list();
		return accounts;
	}

	/**
	 * @Title: getAccIdByEmail
	 * @Description: Function will return accountId based on input emailId
	 * from the ACCOUNT table.
	 * 
	 * @param memberEmail
	 * @return long
	 * @throws Exception
	 */
	public long getAccIdByEmail(String memberEmail) throws Exception {
		Session s=getSession();
		String hql="select acc.id "+
				"from Account acc "+
				"where acc.email=?";
		return (Long)s.createQuery(hql).setParameter(0, memberEmail).uniqueResult();
	}

	/**
	 * @Title: getAccIdByUserName
	 * @Description: Function will return accountId based on input userName
	 * from the ACCOUNT table.
	 * 
	 * @param connection
	 * @param username
	 * @return long
	 * @throws Exception
	 */
	public long getAccIdByUserName(Connection connection, String username) throws Exception {
		
		try
		{
			PreparedStatement ps = connection.prepareStatement(
					"SELECT a.* "
							+ "FROM ACCOUNT a "
							+ "WHERE "
							+ "a.username='" + username + "'");
			ResultSet rs = ps.executeQuery();
			long accountId = 0;
			while(rs.next())
			{accountId = rs.getLong("id");}
			return accountId;
		}
		catch(Exception e)
		{
			throw e;
		}
	}
	
}
