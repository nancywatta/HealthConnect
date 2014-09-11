package aucklanduni.ece.hc.service;

import org.springframework.transaction.annotation.Transactional;

import aucklanduni.ece.hc.repository.model.Account;

@Transactional
public interface AccountService extends BaseService<Account>{
	
	public Account getAccountbyEmail(String emailId)throws Exception;
	
	public void createAccount(String emailId, String password, String userName)throws Exception;
	
	public Account getAccbyEmailPswd(String emailId, String password)throws Exception;

	public void createNewAccount(Account account) throws Exception;
	
	//Ben 09/2014
	public String getEmailByAccountId(long accountId)throws Exception;
}
