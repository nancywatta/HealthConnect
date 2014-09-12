package aucklanduni.ece.hc.webservice;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import aucklanduni.ece.hc.repository.model.Account;
import aucklanduni.ece.hc.service.AccountService;
import aucklanduni.ece.hc.webservice.model.HCMessage;
import aucklanduni.ece.hc.webservice.model.ValidationFailException;

import com.wordnik.swagger.annotations.Api;

@Api(value = "account", description = "Manage Account")
@RestController
@RequestMapping("/service/Account/")
public class AccountRestController {

	@Autowired
	private AccountService accountService;

	@RequestMapping(value="/login",method = RequestMethod.GET
			,headers="Accept=application/json"
			)
	public HCMessage login(HttpServletRequest request, HttpServletResponse response,
			@RequestParam("emailId") String emailId,
			@RequestParam(value="password",required=false) String password,
			@RequestParam(value="userName",required=false) String userName){
		HCMessage message = new  HCMessage();
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
					throw new ValidationFailException("Please enter a password");
				}
				// if yes, return the accountId
				else {
					memberAccs.get(0).setLastLoginDate(new Date());
					accountService.update(memberAccs.get(0));
					message.setSuccess(memberAccs.get(0));
					return message;
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
						throw new ValidationFailException("Incorrect password");
					// else register the account and return created accountId
					else {
						account.setCreateDate(new Date());
						account.setEmail(emailId);
						account.setPassword(password);
						account.setUsername(userName);
						accountService.createNewAccount(account);
						message.setSuccess(account);
						return message;
					}
				}
				// if input email and password correct, return accountId
				else {
					memberAccs.get(0).setLastLoginDate(new Date());
					accountService.update(memberAccs.get(0));
					message.setSuccess(memberAccs.get(0));
					return message;
				}
			}

		}catch(ValidationFailException ve) {
			message.setFail("404", ve.getMessage());
		}catch (Exception e) {
			e.printStackTrace();
			message.setFail("400", e.getMessage());
		}
		return message;
	}

}
