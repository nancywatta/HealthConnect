package aucklanduni.ece.hc.webservice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import aucklanduni.ece.hc.repository.model.Account;
import aucklanduni.ece.hc.repository.model.Dictionary;
import aucklanduni.ece.hc.repository.model.Group;
import aucklanduni.ece.hc.service.AccountService;
import aucklanduni.ece.hc.service.DictionaryService;
import aucklanduni.ece.hc.service.GroupService;
import aucklanduni.ece.hc.webservice.model.HCMessage;
import aucklanduni.ece.hc.webservice.model.ValidationFailException;

import com.wordnik.swagger.annotations.Api;

@Api(value = "group", description = "Manage Groups")
@RestController
@RequestMapping("/service/group/")
public class GroupRestController {
	@Autowired
	private GroupService groupService;
	@Autowired
	private DictionaryService roleService;
	@Autowired
	private AccountService accountService;

	@RequestMapping(value="/createGroup",method = RequestMethod.POST
			,headers="Accept=application/json"
			)
	public HCMessage createGroup(HttpServletRequest request, HttpServletResponse response
			,@RequestParam(value="accountId") long accountId
			,@RequestParam(value="groupName") String groupName
			,@RequestParam(value="roleId") long roleId
			,@RequestParam(value="members",required=false) String members
			) {
		HCMessage message = new  HCMessage();
		try {
			Dictionary role = null;
			role = roleService.findById(roleId);

			// Only Patient or Nurse can create a Group
			if(! (role.getValue().compareTo("P")==0 
					|| role.getValue().compareTo("N")==0)) {
				throw new ValidationFailException("Only Patient or Nurse can create a Group");
			}

			Account account = null;
			// check if given accountId exists
			account = accountService.findById(accountId);
			if(account == null) {
				throw new ValidationFailException("Account does not exist");
			}

			// check if group name already exists
			List<Group> groups = new ArrayList<Group>();
			groups = groupService.findByHql("from Group WHERE groupname='" + groupName + "'");
			if(groups.size() > 0) {
				throw new ValidationFailException("Group Name Already Exists");
			}

			groupService.createNewGroup(accountId, groupName, roleId, members);

		}catch(ValidationFailException ve) {
			message.setFail("404", ve.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			message.setFail("400", e.getMessage());
		}

		return message;
	}

	@RequestMapping(value="/showGroups",method = RequestMethod.GET
			,headers="Accept=application/json"
			)
	public HCMessage showGroups(HttpServletRequest request, HttpServletResponse response,
			@RequestParam("accountId") long accountId){
		HCMessage message = new  HCMessage();
		List<Group> groupList = new ArrayList<Group>();
		Map<String, ArrayList<Group>> groupArray = new HashMap<String, ArrayList<Group>>();
		try {
			Account account = null;
			// check if given accountId exists
			account = accountService.findById(accountId);
			if(account == null) {
				throw new ValidationFailException("Account does not exist");
			}
			
			groupList = groupService.findByHql("select distinct g from Group g, "
					+ "Member m "
					+ "WHERE "
					+ "g.id=m.groupId "
					+ "and m.accountId= " + accountId);
			
			if(groupList == null || groupList.size() < 1) 
				throw new ValidationFailException("No Group Exists for given Account");

			groupArray.put("groups", (ArrayList<Group>)groupList);
			message.setSuccess(groupArray);

		}catch(ValidationFailException ve) {
			message.setFail("404", ve.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			message.setFail("400", e.getMessage());
		}
		return message;
	}

	@RequestMapping(value="/showMembers",method = RequestMethod.GET
			,headers="Accept=application/json"
			)
	public HCMessage showMembers(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "accountId",required=false) Long accountId,
			@RequestParam("groupId") long groupId){
		HCMessage message = new  HCMessage();
		try {
			long accId = 0;
			if(accountId!=null) {
				accId = accountId.longValue();
				Account account = null;
				// check if given accountId exists
				account = accountService.findById(accId);
				if(account == null) {
					throw new ValidationFailException("Account does not exist");
				}
			}
			ArrayList<Account> memberList = groupService.GetMembers(accId,groupId);
			if(memberList== null || memberList.size() < 1) {
				throw new ValidationFailException("GroupId does not exist");
			}
			Map<String, ArrayList<Account>> memberArray = new HashMap<String, ArrayList<Account>>();
			memberArray.put("members", memberList);
			message.setSuccess(memberArray);

		}catch(ValidationFailException ve) {
			message.setFail("404", ve.getMessage());
		}catch (Exception e) {
			e.printStackTrace();
			message.setFail("400", e.getMessage());
		}
		return message;
	}

	@RequestMapping(value="/inviteUser",method = RequestMethod.POST
			,headers="Accept=application/json"
			)
	public HCMessage inviteUser(HttpServletRequest request, HttpServletResponse response,
			@RequestParam("accountId") long accountId,
			@RequestParam("groupId") long groupId,
			@RequestParam("emailId") String emailId,
			@RequestParam("roleId") long roleId){
		HCMessage message = new  HCMessage();
		try {
			Account account = null;
			// check if given accountId exists
			account = accountService.findById(accountId);
			if(account == null) {
				throw new ValidationFailException("Account does not exist");
			}

			List<Dictionary> roles = new ArrayList<Dictionary>();
			roles = roleService.findByHql(
					"select d from Dictionary d, Member m "
							+ "WHERE "
							+ "m.roleId=d.id "
							+ "and m.groupId= " + groupId
							+ " and m.accountId= " + accountId);

			if(roles== null || roles.size() < 1) {
				throw new ValidationFailException("Invalid Input");
			}

			groupService.inviteValidation(roles.get(0).getId(), accountId, groupId, roleId,emailId);

			groupService.inviteUser(accountId, groupId, roleId, emailId);

			message.setSuccess();

		}catch(ValidationFailException ve) {
			message.setFail("404", ve.getMessage());
		}
		catch (Exception e) {
			e.printStackTrace();
			message.setFail("400", e.getMessage());
		}
		return message;
	}
}
