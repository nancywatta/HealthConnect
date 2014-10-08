package aucklanduni.ece.hc.service.impl;

import java.sql.Connection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import aucklanduni.ece.hc.repository.dao.AccountDao;
import aucklanduni.ece.hc.repository.model.Account;
import aucklanduni.ece.hc.repository.model.Database;
import aucklanduni.ece.hc.service.AccountService;

@Service
public class AccountServiceImpl extends BaseServiceImpl<Account> implements AccountService{
	@Autowired
	private AccountDao accountDao;
	
	/**
	 * @Title: getAccountbyEmail
	 * @Description: Function will return account details based on input emailId
	 * from the ACCOUNT table.
	 * 
	 * @param emailId
	 * @return List<Account>
	 * @throws Exception
	 */
	public List<Account> getAccountbyEmail(String emailId)throws Exception {
		try {
			List<Account> memberAccs = accountDao.findByHql(
					"from Account a "
							+ "WHERE "
							+ "a.email='" + emailId + "'");
			return memberAccs;
		}
		catch (Exception e) {
			throw e;
		}
	}

	/**
	 * @Title: createAccount
	 * @Description: Function will insert record into ACCOUNT table.
	 * 
	 * @param emailId
	 * @param password
	 * @param userName
	 * @throws Exception
	 */
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
	
	/**
	 * @Title: getAccbyEmailPswd
	 * @Description: Function will return account details based on input emailId 
	 * and password from the ACCOUNT table.
	 * 
	 * @param emailId
	 * @param password
	 * @return List<Account>
	 * @throws Exception
	 */
	public List<Account> getAccbyEmailPswd(String emailId, String password)throws Exception{
		try {
			
			List<Account> memberAccs = accountDao.findByHql(
					"from Account a "
							+ "WHERE "
							+ "a.email='" + emailId + "'"
							+ " and a.password='" + 
							password + "'");
			return memberAccs;
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
	/**
	 * @Title: getEmailByAccountId
	 * @Description: Function will return emailId based on input accountId 
	 * from the ACCOUNT table.
	 * 
	 * @param accountId
	 * @return String
	 * @throws Exception
	 */
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

	/**
	 * @Title: getAccbyAppointmentId
	 * @Description: Function will return account details of all users 
	 * with which the given appointment is shared.
	 * 
	 * @param appointmentId
	 * @return List<Account>
	 * @throws Exception
	 */
	public List<Account> getAccbyAppointmentId(long appointmentId) throws Exception {
		List<Account> accounts=accountDao.getAccbyAppointmentId(appointmentId);
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
		return accountDao.getAccIdByEmail(memberEmail);
	}

	/**
	 * @Title: getAccIdByUserName
	 * @Description: Function will return accountId based on input userName
	 * from the ACCOUNT table.
	 * 
	 * @param username
	 * @return long
	 * @throws Exception
	 */
	public long getAccIdByUsername(String username) throws Exception {
		try {
			Database database= new Database();
			Connection connection = database.Get_Connection();

			long accountId = accountDao.getAccIdByUserName(connection, username);
			return accountId;
		}
		catch (Exception e) {
			throw e;
		}
		
	}

}
