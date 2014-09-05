package aucklanduni.ece.hc.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import aucklanduni.ece.hc.repository.model.Account;
import aucklanduni.ece.hc.repository.model.Group;
import aucklanduni.ece.hc.service.AccountService;
import aucklanduni.ece.hc.service.GroupService;

import com.google.gson.Gson;

@Controller
@RequestMapping("/Group")
public class GroupController {
	@Autowired
	private GroupService groupService;
	@Autowired
	private AccountService accountService;
	private Gson gson = new Gson();
	
	@RequestMapping(value="/showGroups")
	@ResponseBody
	public String showGroups(HttpServletRequest request, HttpServletResponse response,
			@RequestParam("accountId") long accountId){
		String groups = null;
		System.out.println("showGroups");
		try {
			
			Map<String, ArrayList<Group>> groupList = groupService.GetGroups(accountId);
			Gson gson = new Gson();
			System.out.println(gson.toJson(groupList));
			groups = gson.toJson(groupList);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return groups;
	}

	@RequestMapping(value="/showMembers")
	@ResponseBody
	public String showMembers(HttpServletRequest request, HttpServletResponse response,
			@RequestParam("accountId") long accountId,
			@RequestParam("groupId") long groupId){
		String members = null;
		System.out.println("showMembers");
		try {
			
			ArrayList<Account> memberList = groupService.GetMembers(accountId,groupId);
			Map<String, ArrayList<Account>> memberArray = new HashMap<String, ArrayList<Account>>();
			memberArray.put("members", memberList);
			Gson gson = new Gson();
			System.out.println(gson.toJson(memberArray));
			members = gson.toJson(memberArray);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return members;
	}
	
	@RequestMapping(value="/inviteUser")
	@ResponseBody
	public String inviteUser(HttpServletRequest request, HttpServletResponse response,
			@RequestParam("accountId") long accountId,
			@RequestParam("groupId") long groupId,
			@RequestParam("emailId") String emailId,
			@RequestParam("roleId") long roleId){
		String result = null;
		long accId;
		System.out.println("inviteUser");
		try {
			result = groupService.inviteUserValidation(accountId, groupId, roleId,emailId);
			if(result.compareTo("Succes")!=0) 
				return result;
			Account account = accountService.getAccountbyEmail(emailId);
			if(account == null) {
				String userName=null;
				accountService.createAccount(emailId, "heathConnect",userName);
				Account acc = accountService.getAccountbyEmail(emailId);
				accId = acc.getId();
			}
			else{
				accId = account.getId();
			}
			
			groupService.saveMember(groupId,accId,emailId,roleId);
			return "Succes";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	@RequestMapping(value="/createGroup")
	@ResponseBody
	public String createGroup(HttpServletRequest request, HttpServletResponse response,
			@RequestParam("accountId") long accountId
			,@RequestParam("groupName") String groupName
			,@RequestParam("roleId") long roleId){
		
		System.out.println("createGroup");

		//prepare group
		Group group = new Group();
		group.setGroupname(groupName);
		group.setCreateDate(new Date());

		//get account
		Account account = null;
		try {
			account = accountService.findById(accountId);
		} catch (Exception e) {
			return "Fail";
//			return "{\"status\":\"Fail\""
//					+ ",\"error\":\"Invalid Account Id \""+e.getMessage()+"}";
		}	
		
		try{
			//call create group service
			groupService.createGroup(group, account, roleId);
		}catch(Exception e){
			return "Fail";
//			return "{\"status\":\"Fail\""
//					+ ",\"error\":\"Create Group Failed \""+e.getMessage()+"}";
		}

//		return "{\"status\":\"Success\""
//				+ ",\"response\":"+gson.toJson(group)+"}";
		return group.getId()+ "";
	}
	
}
