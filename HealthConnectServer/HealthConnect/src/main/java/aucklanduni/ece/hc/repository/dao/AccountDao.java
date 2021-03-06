package aucklanduni.ece.hc.repository.dao;

import java.sql.Connection;
import java.util.List;

import aucklanduni.ece.hc.repository.model.Account;


public interface AccountDao  extends BaseDao<Account> {
	
	
	public Account getAccountByEmail(Connection connection, String emailId) throws Exception;
	
	public void createAccount(Connection connection, String emailId, String password,
			String userName)throws Exception;
	
	public Account getAccbyEmailPswd(Connection connection, String emailId, String password) throws Exception;
	
	//Ben 09/2014
	public String getEmailByAccountId(Connection connection, long accountId) throws Exception;
    
	//Wu
	public List<Account> getAccbyAppointmentId(long appointmentId) throws Exception;
	//wu
	public long getAccIdByEmail(String memberEmail) throws Exception;
	
	//Yalu
	public long getAccIdByUserName(Connection connection,String username) throws Exception;
	
}
