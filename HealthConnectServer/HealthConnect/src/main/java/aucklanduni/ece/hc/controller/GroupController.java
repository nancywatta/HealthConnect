package aucklanduni.ece.hc.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import aucklanduni.ece.hc.repository.model.Account;
import aucklanduni.ece.hc.repository.model.Dictionary;
import aucklanduni.ece.hc.repository.model.Group;
import aucklanduni.ece.hc.service.AccountService;
import aucklanduni.ece.hc.service.DictionaryService;
import aucklanduni.ece.hc.service.GroupService;
import aucklanduni.ece.hc.service.NotifyService;
import aucklanduni.ece.hc.webservice.model.ValidationFailException;

import com.google.gson.Gson;

@Controller
@RequestMapping("/Group")
public class GroupController {
	@Autowired
	private GroupService groupService;
	@Autowired
	private AccountService accountService;
	@Autowired
	private DictionaryService dictionaryService;
	@Autowired
	private NotifyService notifyService;
	private Gson gson = new Gson();

	// http://localhost:8080/HealthConnect/Group/showGroups?accountId=123
	@RequestMapping(value="/showGroups")
	@ResponseBody
	public String showGroups(HttpServletRequest request, HttpServletResponse response,
			@RequestParam("accountId") long accountId){
		String groups = null;
		List<Group> groupList = new ArrayList<Group>();
		Map<String, ArrayList<Group>> groupArray = new HashMap<String, ArrayList<Group>>();
		try {
			groupList = groupService.findByHql("select distinct g from Group g, "
					+ "Member m "
					+ "WHERE "
					+ "g.id=m.groupId "
					+ "and m.accountId= " + accountId);
			groupArray.put("groups", (ArrayList<Group>)groupList);
			groups = gson.toJson(groupList);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return groups;
	}

	// http://localhost:8080/HealthConnect/Group/showMembers?accountId=123&groupId=1
	@RequestMapping(value="/showMembers")
	@ResponseBody
	public String showMembers(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "accountId",required=false) Long accountId,
			@RequestParam("groupId") long groupId){
		String members = null;
		try {
			long accId = 0;
			if(accountId!=null)
				accId = accountId.longValue();
			ArrayList<Account> memberList = groupService.GetMembers(accId,groupId);
			Map<String, ArrayList<Account>> memberArray = new HashMap<String, ArrayList<Account>>();
			memberArray.put("members", memberList);
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
		try {
			List<Dictionary> roles = new ArrayList<Dictionary>();
			roles = dictionaryService.findByHql(
					"select d from Dictionary d, Member m "
							+ "WHERE "
							+ "m.roleId=d.id "
							+ "and m.groupId= " + groupId
							+ " and m.accountId= " + accountId);
			
			if(roles== null || roles.size() < 1) {
				return "Invalid Input";
			}
			
			groupService.inviteValidation(roles.get(0).getId(), accountId, groupId, roleId,emailId);
			
			groupService.inviteUser(accountId, groupId, roleId, emailId);
			
			return "Succes";

		}catch(ValidationFailException ve) {
			return ve.getMessage();
		}
		catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}

	//Ben 09/2014
	// http://localhost:8080/HealthConnect/Group/deleteMember?accountId=123&groupId=1&memberId=123
	@RequestMapping(value="/deleteMember")
	@ResponseBody
	public String deleteUser(HttpServletRequest request, HttpServletResponse response,
			@RequestParam("accountId") long accountId,
			@RequestParam("groupId") long groupId,
			@RequestParam("memberId") long memberId){
		String result = null;
		System.out.println("deleteMember");
		try {
			result = groupService.deleteMemberValidation(accountId, groupId, memberId);
			if(result.compareTo("Succes")!=0) 
				return result;

			groupService.deleteMember(groupId,memberId);
			notifyService.notify(memberId, "You have been deleted from group", "email");

			return "Succes";

		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@RequestMapping(value="/deleteGroup")
	@ResponseBody
	public String deleteGroup(HttpServletRequest request, HttpServletResponse response,
			@RequestParam("accountId") long accountId
			,@RequestParam("groupId") long groupId){
		String result = null;
		System.out.println("deleteGroup");
		try{
			result = groupService.deleteGroupValidation(accountId, groupId);
			if(result.compareTo("Succes")!=0) 
				return result;
			groupService.deleteMember(accountId, groupId);
			groupService.deleteGroup(groupId);
			return "Succes";
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		
		
		return result;
	}

}
