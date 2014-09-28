package aucklanduni.ece.hc.webservice;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
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

import aucklanduni.ece.hc.repository.dao.MemberDao;
import aucklanduni.ece.hc.repository.model.Account;
import aucklanduni.ece.hc.repository.model.Database;
import aucklanduni.ece.hc.repository.model.Dictionary;
import aucklanduni.ece.hc.repository.model.Group;
import aucklanduni.ece.hc.repository.model.Member;
import aucklanduni.ece.hc.service.AccountService;
import aucklanduni.ece.hc.service.DictionaryService;
import aucklanduni.ece.hc.service.GroupService;
import aucklanduni.ece.hc.service.NotifyService;
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
	@Autowired
	private NotifyService notifyService;
	@Autowired
	private MemberDao memberDao;

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
			
			if(role == null)
				throw new ValidationFailException("No Roles exists in Database");

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
			groups = groupService.getGroupByName(groupName);
			if(groups.size() > 0 && 
					( groups.get(0).getExpirationDate() == null || 
					  groups.get(0).getExpirationDate().compareTo(new Date()) > 0)) {
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

			groupList = groupService.getGroupByAccId(accountId);

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
			roles = roleService.getRolesByGroupIdAccId(accountId, groupId);

			// if the input accountId and GroupId does not exist in database
			if(roles== null || roles.size() < 1) {
				throw new ValidationFailException("Invalid Input");
			}

			// perform business validation
			groupService.inviteValidation(roles.get(0).getId(), accountId, groupId, roleId,emailId);

			// save invited member details in the MEMBER table.
			groupService.inviteUser(accountId, groupId, roleId, emailId);
			
			notifyService.notify(emailId, "You have been invited to the group", "email");

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
	
	/**
	 * 
	 * @Title: deleteGroup 
	 * @author Yalu You,Pengyi Li
	 * @Description: Service will delete a specific group
	 * on business validation
	 * 1. Nurse can only delete empty group.
	 * 2. Support Member cannot delete the group.
	 * 3. Patient can delete a group whether it is empty or not, if it is not empty, delete members first, then 
	 * delete the group.
	 *  
	 * @param request
	 * @param response
	 * @param accountId - account in the group
	 * @param groupId - the group will be deleted
	 * @return HCMessage
	 * @throws
	 */
	@RequestMapping(value="/deleteGroup",method = RequestMethod.POST
			,headers="Accept=application/json"
			)
	public HCMessage deleteGroup(HttpServletRequest request, HttpServletResponse response
			,@RequestParam("accountId") long accountId
			,@RequestParam("groupId") long groupId) {
		HCMessage message = new  HCMessage();
		try {
			  groupService.deleteGroupValidation(accountId, groupId);
				groupService.expireAllMember(groupId);
				groupService.expireGroup(groupId);

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
	
	
	
	/**
	 * 
	 * @Title: deleteMember 
	 * @author Ben Zhong
	 * @Description: Service will delete a specific member from a group
	 * on business validation
	 * 1. Patient can delete nurse and support member.
	 * 2. Patient cannot delete itself.
	 * 3. Nurse can delete the patient.
	 * 4. Nurse or Support Member can delete itself.
	 * 5. Nurse or Support Member cannot delete another nurse or support member.
	 * 6. If the last member delete itself, the group will be deleted automatically 
	 *  
	 * @param request
	 * @param response
	 * @param accountId - of the current user (the one who execute the operation)
	 * @param groupId - of the group will be modified
	 * @param memberId - of the member will be deleted from the group
	 * @return HCMessage
	 * @throws
	 */
	
	@RequestMapping(value="/deleteMember",method = RequestMethod.POST
			,headers="Accept=application/json")
	public HCMessage deleteMember(HttpServletRequest request, HttpServletResponse response,
			@RequestParam("accountId") long accountId,
			@RequestParam("groupId") long groupId,
			@RequestParam("memberId") long memberId) {
		HCMessage message = new  HCMessage();
		try {
			boolean checkValidation = false;
			checkValidation = groupService.deleteMemberValidation(accountId, groupId, memberId);

			if(checkValidation) 
			{
				groupService.expireMember(groupId,memberId);
				List<Member> members = null;
				members = groupService.getEffectiveMembers(groupId);

				//if there is no effective member in the group, the group will be expired
				if (members == null || members.size() < 1){
					groupService.expireGroup(groupId);
					notifyService.notify(memberId, "You have been deleted from group", "email");
					message.setSuccess("group ("+groupId+") has been deleted");

				}
				else{
					notifyService.notify(memberId, "You have been deleted from group", "email");
					message.setSuccess("member ("+memberId+") has been deleted from group ("+groupId+")");
				}
			}

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