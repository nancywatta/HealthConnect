package aucklanduni.ece.hc.repository.dao;

import java.sql.Connection;

import aucklanduni.ece.hc.repository.model.Account;


public interface AccountDao  extends BaseDao<Account> {
	
	public Account getAccountByEmail(Connection connection, String emailId) throws Exception;
	
	public void createAccount(Connection connection, String emailId)throws Exception;
	
	public Account getAccbyEmailPswd(Connection connection, String emailId, String password) throws Exception;
}
