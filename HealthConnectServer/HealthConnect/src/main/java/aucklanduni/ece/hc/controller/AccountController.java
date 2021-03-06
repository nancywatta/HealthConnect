package aucklanduni.ece.hc.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;

import aucklanduni.ece.hc.repository.model.Account;
import aucklanduni.ece.hc.service.AccountService;

@Controller
@RequestMapping("/Account")
public class AccountController {
	@Autowired
	private AccountService accountService;
	private Gson gson = new Gson();

	@RequestMapping(value="/login")
	@ResponseBody
	public String login(HttpServletRequest request, HttpServletResponse response,
			@RequestParam("emailId") String emailId,
			@RequestParam(value="password",required=false) String password,
			@RequestParam(value="userName",required=false) String userName){
		String accountDetails = "";
		List<Account> memberAccs = new ArrayList<Account>();
		Account account = new Account();
		try {

			// Check if account exist with given email and (given password or Default password)
			String pswd = (password == null) ? "healthConnect" : password;
			memberAccs = accountService.findByHql(
					"from Account a "
							+ "WHERE "
							+ "a.email='" + emailId + "'"
							+ " and a.password='" + 
							pswd + "'");
			
			if(password==null) {
				// If no then return false, so that user puts his own password
				if(memberAccs == null || memberAccs.size() <1) {
					return null;
				}
				// if yes, return the accountId
				else {
					memberAccs.get(0).setLastLoginDate(new Date());
					accountService.update(memberAccs.get(0));
					accountDetails = gson.toJson(memberAccs.get(0));
					return accountDetails;
				}
			}
			else {
				// if No, check if the email already exist in DB
				if(memberAccs == null || memberAccs.size() < 1) {
					memberAccs = accountService.findByHql(
							"from Account a "
									+ "WHERE "
									+ "a.email='" + emailId + "'");
					
					// if email id exists, it means wrong password entered
					if(memberAccs.size() > 0)
						return null;
					// else register the account and return created accountId
					else {
						account.setCreateDate(new Date());
						account.setEmail(emailId);
						account.setPassword(password);
						account.setUsername(userName);
						accountService.createNewAccount(account);
						accountDetails = gson.toJson(account);
						return accountDetails;
					}
				}
				// if input email and password correct, return accountId
				else {
					memberAccs.get(0).setLastLoginDate(new Date());
					accountService.update(memberAccs.get(0));
					accountDetails = gson.toJson(memberAccs.get(0));
					return accountDetails;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
