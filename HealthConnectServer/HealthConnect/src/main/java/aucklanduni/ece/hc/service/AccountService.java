package aucklanduni.ece.hc.service;

import aucklanduni.ece.hc.repository.model.Account;

public interface AccountService extends BaseService<Account>{
	
	public Account getAccountbyEmail(String emailId)throws Exception;
	
	public void createAccount(String emailId, String password, String userName)throws Exception;
	
	public Account getAccbyEmailPswd(String emailId, String password)throws Exception;

}
