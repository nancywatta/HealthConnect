package aucklanduni.ece.hc.controller;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;







import aucklanduni.ece.hc.repository.model.Account;
import aucklanduni.ece.hc.repository.model.ApnUser;
import aucklanduni.ece.hc.service.AccountService;
import aucklanduni.ece.hc.service.ApnUserService;
import aucklanduni.ece.hc.service.GroupService;
import aucklanduni.ece.hc.service.NotifyService;
import aucklanduni.ece.hc.webservice.ApnRestController;
/**
 * 
* @ClassName: APNController 
* @Description: This is a Demo showing how to create a controller 
* and how to call its services.
* ApnUser used to support Push Notification Service.
* Right now this requirment is suspended
* @author Zhao Yuan
* @date 2014年9月15日 下午8:45:22 
*
 */
@Controller
@RequestMapping("/ApnUser")
public class APNController {

	Logger log = Logger.getLogger(APNController.class);
	/**
	 * Autowired services that it needs
	 */
	@Autowired
	private ApnUserService apnUserService;
	@Autowired
	private AccountService accountService;
	@Autowired
	private GroupService groupService;
	@Autowired
	private NotifyService notifyService;
	
	/**
	 * 
	* @Title: showAll 
	* @Description: list all users in ApnUser table
	* @param request
	* @param response
	* @return 
	* @return String
	* @throws
	 */
	@RequestMapping(value="/showAll")
	public String showAll(HttpServletRequest request, HttpServletResponse response){
		System.out.println("show all");
		try {
			List<ApnUser> list = apnUserService.findAll();
			log.debug(list.get(0).toString());
			
			List<Account> listAccount = accountService.findAll();
			log.debug("accountService>>>"+listAccount.size());
			
			
			log.debug(">>>"+apnUserService.executMySql());
			log.debug(">>>"+apnUserService.executMyHql());
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "apnUser/showAll";
	}
	
	/**
	 * 
	* @Title: add 
	* @Description: add new users to database
	* @param request
	* @param response
	* @return 
	* @return String
	* @throws
	 */
	@RequestMapping(value="/add")
	public String add(HttpServletRequest request, HttpServletResponse response){
		ApnUser t = new ApnUser();
		t.setCreateDate(new Date());
		t.setEmail("zyua826@aaa.com");
		t.setPassword("pwd");
		t.setUsername("ua"+(int)(Math.random()*100));
		try {
			apnUserService.add(t);
			
			log.debug(t.toString());
			
			notifyService.notify(01,"yy","yy");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "apnUser/add";
	}

	/**
	 * 
	* @Title: update 
	* @Description: update users in database
	* @param request
	* @param response
	* @return
	* @throws Exception 
	* @return String
	* @throws
	 */
	@RequestMapping(value="/update")
	public String update(HttpServletRequest request, HttpServletResponse response) throws Exception{
		ApnUser t = apnUserService.findById(new Long(5));
		t.setEmail("updateEmail");
		
		try {
			apnUserService.update(t);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "apnUser/delete";
	}
	
	/**
	 * 
	* @Title: delete 
	* @Description: delete user by id
	* @param request
	* @param response
	* @return 
	* @return String
	* @throws
	 */
	@RequestMapping(value="/delete")
	public String delete(HttpServletRequest request, HttpServletResponse response){

		try {
			apnUserService.deleteById(new Long(5));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "apnUser/delete";
	}
	
}
