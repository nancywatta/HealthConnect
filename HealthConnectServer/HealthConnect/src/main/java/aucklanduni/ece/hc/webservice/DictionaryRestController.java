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

@Api(value = "role", description = "Manage roles")
@RestController
@RequestMapping("/service/Dictionary/")
public class DictionaryRestController {
	@Autowired
	private DictionaryService roleService;
	@Autowired
	private AccountService accountService;
	@Autowired
	private GroupService groupService;

	@RequestMapping(value="/showRoles",method = RequestMethod.GET
			,headers="Accept=application/json"
			)
	public HCMessage showRoles(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value="groupId",required=false) Long groupId,
			@RequestParam(value="accountId",required=false) Long accountId){
		HCMessage message = new  HCMessage();
		Map<String, ArrayList<Dictionary>> rolesArray = new HashMap<String, ArrayList<Dictionary>>();
		List<Dictionary> roleList = new ArrayList<Dictionary>();
		try {
			if(groupId == null || accountId == null) 
				roleList = roleService.findByHql(
						"from Dictionary "
								+ "WHERE type='Role' ");
			else {
				Account account = null;
				// check if given accountId exists
				account = accountService.findById(accountId.longValue());
				if(account == null) {
					throw new ValidationFailException("Account does not exist");
				}
				
				Group group = null;
				// check if given groupId exists
				group = groupService.findById(groupId.longValue());
				if(group == null) {
					throw new ValidationFailException("Group does not exist");
				}
				
				roleList = roleService.GetSpecificRoles(accountId.longValue(), groupId.longValue());
			}

			rolesArray.put("roles", (ArrayList<Dictionary>)roleList);
			message.setSuccess(rolesArray);

		} catch (Exception e) {
			e.printStackTrace();
			message.setFail("400", e.getMessage());
		}

		return message;
	}

}
