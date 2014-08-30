package aucklanduni.ece.hc.repository.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.springframework.stereotype.Repository;

import aucklanduni.ece.hc.repository.dao.AccountDao;
import aucklanduni.ece.hc.repository.model.Account;


@Repository
public class AccountDaoImpl  extends BaseDaoImpl<Account> implements AccountDao{

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

	public void createAccount(Connection connection, String emailId)throws Exception {
		try
		{
			PreparedStatement ps = connection.prepareStatement(
					"INSERT "
							+ "INTO ACCOUNT ("
							+ "(EMAIL, PASSWORD, CREATED_DATE) VALUES"
							+ "(?,?,?)" );
			ps.setString(1, emailId);
			ps.setString(2, "default");
			long time = System.currentTimeMillis();
			java.sql.Timestamp timestamp = new java.sql.Timestamp(time);
			ps.setTimestamp(3, timestamp);
			ps.executeUpdate();
		}
		catch(Exception e)
		{
			throw e;
		}
	}
	
}
