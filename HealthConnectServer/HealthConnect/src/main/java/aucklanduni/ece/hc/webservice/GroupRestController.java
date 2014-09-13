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
import org.springframework.web.bind.annotation.ResponseBody;
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

/**
 * 
 * @ClassName: GroupRestController 
 * @Description: Group REST service to receive requests
 * to manage the groups
 * @author Nancy Watta
 *
 */

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

	/**
	 * 
	 * @Title: createGroup 
	 * @Description: Service will create group for the given accountId
	 * and save details of the group in the GROUP_INFO table.
	 * The service will also save the owner role in the MEMBER table. 
	 * If the member array is passed in input, service will perform business validations.
	 * 1. Nurse can only invite Patient.
	 * 2. Support Member can neither create the group nor invite anyone to the group.
	 * 3. Patient can invite Nurse and Support Member to the group.
	 * 4. A group can have only one Patient.
	 *  After validation passes successfully, service will save member details in the
	 *  MEMBER table.
	 *  Input member array is of the below format
	 *  [
	 *    { "email":"google1@gmail.com",
	 *      "role":
	 *             { "id":2 }
	 *    },
	 *    { "email":"google2@gmail.com",
	 *      "role":
	 *             { "id":3 }
	 *    },
	 *  ]
	 *  if the invited member does not exist in database, account will be registered
	 *  and emailId and default password will be saved in the ACCOUNT table.
	 *  
	 * @param request
	 * @param response
	 * @param accountId 
	 * @param groupName
	 * @param roleId 
	 * @param members  - optional
	 * @return HCMessage
	 * @throws
	 */
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
			
			message.setSuccess();

		}catch(ValidationFailException ve) {
			message.setFail("404", ve.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			message.setFail("400", e.getMessage());
		}

		return message;
	}

	/**
	 * 
	 * @Title: showGroups 
	 * @Description: Service will return all the groups of the given
	 * accountId.
	 *  
	 * @param request
	 * @param response
	 * @param accountId 
	 * @return HCMessage
	 * @throws
	 */
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

			// if no group exists for the given account
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

	/**
	 * 
	 * @Title: showMembers 
	 * @Description: Service will return all the members of the given
	 * groupId from MEMBER table.
	 *  
	 * @param request
	 * @param response
	 * @param accountId - optional
	 * @param groupId
	 * @return HCMessage
	 * @throws
	 */
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
			
			// if no member exists for the given group
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

	/**
	 * 
	 * @Title: inviteUser 
	 * @Description: Service will invite member to the input group based
	 * on business validation
	 * 1. Nurse can only invite Patient.
	 * 2. Support Member cannot invite anyone to the group.
	 * 3. Patient can invite Nurse and Support Member to the group.
	 * 4. A group can have only one Patient.
	 * After validation passes successfully, service will save member details in the
	 *  MEMBER table.
	 *  If the invited member does not exist in database, account will be registered
	 *  and emailId and default password will be saved in the ACCOUNT table.
	 *  
	 * @param request
	 * @param response
	 * @param accountId - of the member inviting other user
	 * @param groupId - of the group to be invited in
	 * @param emailId - of the user being invited
	 * @param roleId - of the user being invited
	 * @return HCMessage
	 * @throws
	 */
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

			// if the input accountId and GroupId does not exist in database
			if(roles== null || roles.size() < 1) {
				throw new ValidationFailException("Invalid Input");
			}

			// perform business validation
			groupService.inviteValidation(roles.get(0).getId(), accountId, groupId, roleId,emailId);

			// save invited member details in the MEMBER table.
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
	
	
	@RequestMapping(value="/deleteGroup",method = RequestMethod.POST
			,headers="Accept=application/json"
			)
	public HCMessage createGroup(HttpServletRequest request, HttpServletResponse response
			,@RequestParam("accountId") long accountId
			,@RequestParam("groupId") long groupId) {
		HCMessage message = new  HCMessage();
		try {
			  groupService.deleteGroupValidation(accountId, groupId);
				groupService.deleteAllMember(groupId);
				groupService.deleteGroup(groupId);

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
	
	
	
	//Ben 09/2014
	// http://localhost:8080/HealthConnect/Group/deleteMember?accountId=123&groupId=1&memberId=123
	@RequestMapping(value="/deleteMember",method = RequestMethod.POST
			,headers="Accept=application/json")
	public HCMessage deleteUser(HttpServletRequest request, HttpServletResponse response,
			@RequestParam("accountId") long accountId,
			@RequestParam("groupId") long groupId,
			@RequestParam("memberId") long memberId) {
		HCMessage message = new  HCMessage();
		try {
			String result = null;
			  result = groupService.deleteMemberValidation(accountId, groupId, memberId);
			  if(result.compareTo("Succes")!=0) 
					return message;else{
						groupService.deleteMember(groupId,memberId);
						//notifyService.notify(memberId, "You have been deleted from group", "email");

				message.setSuccess("member ("+memberId+") has been deleted from group ("+groupId+")");}

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
