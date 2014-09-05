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
import aucklanduni.ece.hc.repository.model.Dictionary;
import aucklanduni.ece.hc.repository.model.Group;
import aucklanduni.ece.hc.repository.model.Member;
import aucklanduni.ece.hc.service.AccountService;
import aucklanduni.ece.hc.service.DictionaryService;
import aucklanduni.ece.hc.service.GroupService;
import aucklanduni.ece.hc.webservice.model.HCMessage;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

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
				message.setFail("404", "Only Patient or Nurse can create a Group");
				return message;
			}

			Account account = null;
			// check if given accountId exists
			account = accountService.findById(accountId);
			if(account == null) {
				message.setFail("404", "Account does not exist");
				return message;
			}

			// check if group name already exists
			List<Group> groups = new ArrayList<Group>();
			groups = groupService.findBySql("SELECT * from GROUP_INFO WHERE groupname='" + groupName + "'");
			if(groups.size() > 0) {
				message.setFail("404", "Group Name Already Exists");
				return message;
			}

			// save group in GROUP_INFO Table
			Group newGroup = new Group();
			newGroup.setGroupname(groupName);
			newGroup.setCreateDate(new Date());
			groupService.createGroup(newGroup, account, roleId);

			if(members !=null) {
				List<Account> accList = 
						new Gson().fromJson(members, new TypeToken<List<Account>>() {}.getType());

				for(Account  member : accList) {
					// Perform Member Validations
					String val = groupService.inviteUserValidation(accountId, 
							newGroup.getId(), member.getRole().getId(), member.getEmail());
					if(val.compareTo("Succes")!=0) 
						return message.setFail("404", val);

					Account memberAcc = accountService.getAccountbyEmail(member.getEmail());
					// if invited account does not exist, create the account with default password
					if(memberAcc == null) {
						memberAcc = new Account();
						memberAcc.setCreateDate(new Date());
						memberAcc.setEmail(member.getEmail());
						memberAcc.setPassword("healthConnect");
						accountService.createNewAccount(memberAcc);
					}

					// save member details in the MEMBER table
					Member newMember = new Member();
					newMember.setAccountId(memberAcc.getId());
					newMember.setCreateDate(new Date());
					newMember.setGroupId(newGroup.getId());
					newMember.setRoleId(member.getRole().getId());

					groupService.saveNewMember(newMember);
				}
			}


		}catch (Exception e) {
			e.printStackTrace();
			message.setFail("400", e.getMessage());
		}

		return message;
	}
}
