package aucklanduni.ece.hc.webservice;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 * @ClassName: GroupMockTests 
 * @Description: JUnit test Cases for Group web service
 * @author Nancy Watta
 * @date 2014/09/15 
 *
 */
@TransactionConfiguration(defaultRollback = true) 
@Transactional 
@RunWith(SpringJUnit4ClassRunner.class)
public class GroupMockTests extends BaseContextControllerTests {
	
	/**
	 *  Static String for Group Web Service url
	 */
   private static String URL = "/service/group";
   
   /**
	 * test showGroup for valid account 
	 */
	@Test  
	public void showGroupForValidAccount() throws Exception {  

		this.mockMvc.perform(get(URL+"/showGroups")
				.contentType(MediaType.APPLICATION_JSON)
				.param("accountId", "1")
				)
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.status").value("200"))
				.andExpect(jsonPath("$.response.groups").isArray())
				.andExpect(jsonPath("$.response.groups[0].groupname").value("group1"));
	}

	/**
	 * test group for invalid account 
	 */
	@Test  
	public void showGroupForInvalidAccount() throws Exception {  

		this.mockMvc.perform(get(URL+"/showGroups")
				.contentType(MediaType.APPLICATION_JSON)
				.param("accountId", "199")
				)
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.status").value("404"))
				.andExpect(jsonPath("$.error").value("Account does not exist"));
	}
	
	/**
	 * test showGroup for account that does not have any 
	 * group created yet  
	 */
	@Test  
	public void emptyGroup() throws Exception {  

		this.mockMvc.perform(get(URL+"/showGroups")
				.contentType(MediaType.APPLICATION_JSON)
				.param("accountId", "22")
				)
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.status").value("404"))
				.andExpect(jsonPath("$.error").value("No Group Exists for given Account"));
	}	

	/**
	 * test showMembers for valid groupId
	 */
	@Test  
	public void showMembersForValidGroup() throws Exception {  

		this.mockMvc.perform(get(URL+"/showMembers")
				.contentType(MediaType.APPLICATION_JSON)
				.param("groupId", "1")
				)
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.status").value("200"))
				.andExpect(jsonPath("$.response.members").isArray())
				.andExpect(jsonPath("$.response.members[0].email").value("nancy.watta@gmail.com"));
	}	

	/**
	 * test showMembers for invalid groupId
	 */
	@Test  
	public void showMembersForInvalidGroup() throws Exception {  

		this.mockMvc.perform(get(URL+"/showMembers")
				.contentType(MediaType.APPLICATION_JSON)
				.param("groupId", "199")
				)
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.status").value("404"))
				.andExpect(jsonPath("$.error").value("GroupId does not exist"));
	}	
	
	/**
	 * test inviteUser for invalid accountId
	 */
	@Test  
	public void inviteUserInvalidAccount() throws Exception {  

		this.mockMvc.perform(post(URL+"/inviteUser")
				.contentType(MediaType.APPLICATION_JSON)
				.param("accountId", "199")
				.param("groupId", "199")
				.param("emailId", "nancy.watta@gmail.com")
				.param("roleId", "1")
				)
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.status").value("404"))
				.andExpect(jsonPath("$.error").value("Account does not exist"));
	}	
	
	/**
	 * test inviteUser for invalid groupId
	 */
	@Test  
	public void inviteUserInvalidGroup() throws Exception {  

		this.mockMvc.perform(post(URL+"/inviteUser")
				.contentType(MediaType.APPLICATION_JSON)
				.param("accountId", "1")
				.param("groupId", "199")
				.param("emailId", "nancy.watta@gmail.com")
				.param("roleId", "1")
				)
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.status").value("404"))
				.andExpect(jsonPath("$.error").value("Invalid Input"));
	}	

	/**
	 * test inviteUser by accountId which does not
	 * own the given groupId
	 */
	@Test  
	public void inviteUserUnknownGroup() throws Exception {  

		this.mockMvc.perform(post(URL+"/inviteUser")
				.contentType(MediaType.APPLICATION_JSON)
				.param("accountId", "1")
				.param("groupId", "34")
				.param("emailId", "nancy.watta@gmail.com")
				.param("roleId", "1")
				)
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.status").value("404"))
				.andExpect(jsonPath("$.error").value("Invalid Input"));
	}
	
	/**
	 * test inviteUser by inviting patient when already
	 * one patient exist in the given group
	 */
	@Test  
	public void invitePatientAgain() throws Exception {  

		this.mockMvc.perform(post(URL+"/inviteUser")
				.contentType(MediaType.APPLICATION_JSON)
				.param("accountId", "1")
				.param("groupId", "1")
				.param("emailId", "nancy.watta@gmail.com")
				.param("roleId", "1")
				)
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.status").value("404"))
				.andExpect(jsonPath("$.error").value("Only one patient allowed per group"));
	}	

	/**
	 * test inviteUser when nurse invites
	 * support member in the given group
	 */
	@Test  
	public void nurseInviteSM() throws Exception {  

		this.mockMvc.perform(post(URL+"/inviteUser")
				.contentType(MediaType.APPLICATION_JSON)
				.param("accountId", "2")
				.param("groupId", "1")
				.param("emailId", "nancy.watta@gmail.com")
				.param("roleId", "3")
				)
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.status").value("404"))
				.andExpect(jsonPath("$.error").value("Nurse can Invite only Patient to Group"));
	}	

	/**
	 * test inviteUser when support member invites
	 * anyone in the given group
	 */
	@Test  
	public void sMInviteUser() throws Exception {  

		this.mockMvc.perform(post(URL+"/inviteUser")
				.contentType(MediaType.APPLICATION_JSON)
				.param("accountId", "3")
				.param("groupId", "1")
				.param("emailId", "nancy.watta@gmail.com")
				.param("roleId", "3")
				)
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.status").value("404"))
				.andExpect(jsonPath("$.error").value("Support Member cannot Invite Member to Group"));
	}	

	/**
	 * test inviteUser when user invites
	 * already existing member of the group again
	 * in the same group
	 */
	@Test  
	public void inviteMemberAgain() throws Exception {  

		this.mockMvc.perform(post(URL+"/inviteUser")
				.contentType(MediaType.APPLICATION_JSON)
				.param("accountId", "1")
				.param("groupId", "1")
				.param("emailId", "nancy.watta@gmail.com")
				.param("roleId", "3")
				)
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.status").value("404"))
				.andExpect(jsonPath("$.error").value("nancy.watta@gmail.com is already a member in this Group"));
	}	

	/**
	 * test inviteUser with correct inputs
	 */
	@Test  
	public void inviteUserCorrect() throws Exception {  

		this.mockMvc.perform(post(URL+"/inviteUser")
				.contentType(MediaType.APPLICATION_JSON)
				.param("accountId", "1")
				.param("groupId", "1")
				.param("emailId", "nancywattaa@gmail.com")
				.param("roleId", "3")
				)
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.status").value("200"));
	}	

}
