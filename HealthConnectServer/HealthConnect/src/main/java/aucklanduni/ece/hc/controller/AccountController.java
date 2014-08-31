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
			@RequestParam(value="password",required=false) String password){
		Account account;
		System.out.println("login");
		try {
			
			if(password==null) {
				account = accountService.getAccbyEmailPswd(emailId,"healthConnect");
				if(account == null) {
					System.out.println("false");
					return "false";
				}
				else {
					System.out.println(Long.toString(account.getId()));
					return Long.toString(account.getId());
				}
			}
			else {
				account = accountService.getAccbyEmailPswd(emailId,password);
				if(account == null)
					return "false";
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
