package aucklanduni.ece.hc.service.impl;

import java.sql.Connection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import aucklanduni.ece.hc.repository.dao.AccountDao;
import aucklanduni.ece.hc.repository.model.Account;
import aucklanduni.ece.hc.repository.model.Database;
import aucklanduni.ece.hc.repository.model.Group;
import aucklanduni.ece.hc.repository.model.Member;
import aucklanduni.ece.hc.service.AccountService;

@Service
public class AccountServiceImpl extends BaseServiceImpl<Account> implements AccountService{
	@Autowired
	private AccountDao accountDao;
		
	public Account getAccountbyEmail(String emailId)throws Exception {
		try {
			Database database= new Database();
			Connection connection = database.Get_Connection();

			Account account = accountDao.getAccountByEmail(connection,emailId);
			return account;
		}
		catch (Exception e) {
			throw e;
		}
	}

	public void createAccount(String emailId, String password, String userName)throws Exception {
		try {
			Database database= new Database();
			Connection connection = database.Get_Connection();

			accountDao.createAccount(connection,emailId, password, userName);
		}
		catch (Exception e) {
			throw e;
		}

	}
	
	public Account getAccbyEmailPswd(String emailId, String password)throws Exception{
		try {
			Database database= new Database();
			Connection connection = database.Get_Connection();

			Account account = accountDao.getAccbyEmailPswd(connection,emailId, password);
			return account;
		}
		catch (Exception e) {
			throw e;
		}

	}
	
	public void createNewAccount(Account account) throws Exception {
		// create account
		accountDao.add(account);
		
	}
	
	//Ben 09/2014
	public String getEmailByAccountId(long accountId)throws Exception {
		try {
			Database database= new Database();
			Connection connection = database.Get_Connection();

			String emailAdd = accountDao.getEmailByAccountId(connection, accountId);
			return emailAdd;
		}
		catch (Exception e) {
			throw e;
		}
	}

	public Account getAccbyAppointmentId(long appointmentId) throws Exception {
		Account account=accountDao.getAccbyAppointmentId(appointmentId);
		return account;
	}

}
