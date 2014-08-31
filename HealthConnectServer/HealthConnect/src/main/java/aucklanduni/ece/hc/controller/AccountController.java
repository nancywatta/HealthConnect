package aucklanduni.ece.hc.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import aucklanduni.ece.hc.repository.model.Account;
import aucklanduni.ece.hc.service.AccountService;

@Controller
@RequestMapping("/Account")
public class AccountController {
	@Autowired
	private AccountService accountService;

	@RequestMapping(value="/login")
	@ResponseBody
	public String showGroups(HttpServletRequest request, HttpServletResponse response,
			@RequestParam("emailId") String emailId,
			@RequestParam(value="password",required=false) String password,
			@RequestParam(value="userName",required=false) String userName){
		Account account;
		System.out.println("login");
		try {

			if(password==null) {
				// Check if account exist with Default password
				account = accountService.getAccbyEmailPswd(emailId,"healthConnect");
				// If no then return false, so that user puts his own password
				if(account == null) {
					System.out.println("false");
					return "false";
				}
				// if yes, return the accountId
				else {
					System.out.println(Long.toString(account.getId()));
					return Long.toString(account.getId());
				}
			}
			else {
				// check if account exist with given email and password
				account = accountService.getAccbyEmailPswd(emailId,password);
				// if No, check if the email already exist in DB
				if(account == null) {
					account = accountService.getAccountbyEmail(emailId);
					// if email id exists, it means wrong password entered
					if(account!=null)
						return "false";
					// else register the account and return created accountId
					else {
						accountService.createAccount(emailId, password,userName);
						account = accountService.getAccountbyEmail(emailId);
						return Long.toString(account.getId());
					}
				}
				// if input email and password correct, return accountId
				else {
					return Long.toString(account.getId());
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return "false";
	}

}
