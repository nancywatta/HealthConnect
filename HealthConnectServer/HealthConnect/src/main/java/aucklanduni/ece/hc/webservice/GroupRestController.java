package aucklanduni.ece.hc.webservice;

import java.util.ArrayList;
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
import aucklanduni.ece.hc.service.AccountService;
import aucklanduni.ece.hc.service.DictionaryService;
import aucklanduni.ece.hc.service.GroupService;
import aucklanduni.ece.hc.webservice.model.HCMessage;
import aucklanduni.ece.hc.webservice.model.ValidationFailException;

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
}
