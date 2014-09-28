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
 * @Description: JUnit test Cases for ensuring History is maintained
 * for Groups and Events
 * @author Nancy Watta
 * @date 2014/09/28 
 *
 */
@TransactionConfiguration(defaultRollback = true) 
@Transactional 
@RunWith(SpringJUnit4ClassRunner.class)
public class MaintainHistoryMockTests extends BaseContextControllerTests {
	/**
	 *  Static String for Group Web Service url
	 */
   private static String GROUP_URL = "/service/group";
   
   /**
	 * test showGroups to check if even the expired groups are returned 
	 */
	@Test  
	public void showExpiredGroup() throws Exception {  

		this.mockMvc.perform(get(GROUP_URL+"/showGroups")
				.contentType(MediaType.APPLICATION_JSON)
				.param("accountId", "13")
				)
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.status").value("200"))
				.andExpect(jsonPath("$.response.groups").isArray())
				.andExpect(jsonPath("$.response.groups[0].expirationDate").exists());
	}
	
	/**
	 * test showMembers to check if even the expired members are returned
	 * for the input group
	 */
	@Test  
	public void showExpiredMembers() throws Exception {  

		this.mockMvc.perform(get(GROUP_URL+"/showMembers")
				.contentType(MediaType.APPLICATION_JSON)
				.param("groupId", "15")
				)
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.status").value("200"))
				.andExpect(jsonPath("$.response.members").isArray())
				.andExpect(jsonPath("$.response.members[0].email").value("dummy@gmail.com"))
				.andExpect(jsonPath("$.response.members[0].memberDetails.expirationDate").exists())
				.andExpect(jsonPath("$.response.members[0].memberDetails.isActive").value("N"));
	}
	
	/**
	 * test inviteUser when the Nurse removed from the Group
	 * tries to invite patient
	 */
	@Test  
	public void inviteUserByExpiredNurse() throws Exception {  

		this.mockMvc.perform(post(GROUP_URL+"/inviteUser")
				.contentType(MediaType.APPLICATION_JSON)
				.param("accountId", "4")
				.param("groupId", "16")
				.param("emailId", "nancy.watta@gmail.com")
				.param("roleId", "1")
				)
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.status").value("404"))
				.andExpect(jsonPath("$.error").value("Invalid Input"));
	}	
	
	/**
	 * test inviteUser when the removed nurse is 
	 * invited again to the group
	 */
	@Test  
	public void inviteExpiredNurse() throws Exception {  

		this.mockMvc.perform(post(GROUP_URL+"/inviteUser")
				.contentType(MediaType.APPLICATION_JSON)
				.param("accountId", "14")
				.param("groupId", "16")
				.param("emailId", "vikramkrsingh321@gmail.com")
				.param("roleId", "2")
				)
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.status").value("200"));
	}
	
	/**
	 * test inviteUser when the removed Support member is 
	 * invited again to the group
	 */
	@Test  
	public void inviteExpiredUser() throws Exception {  

		this.mockMvc.perform(post(GROUP_URL+"/inviteUser")
				.contentType(MediaType.APPLICATION_JSON)
				.param("accountId", "14")
				.param("groupId", "16")
				.param("emailId", "sahilwatta@gmail.com")
				.param("roleId", "3")
				)
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.status").value("200"));
	}
	
	/**
	 * test inviteUser when the patient invites in Expired Group
	 */
	@Test  
	public void inviteInExpiredGroup() throws Exception {  

		this.mockMvc.perform(post(GROUP_URL+"/inviteUser")
				.contentType(MediaType.APPLICATION_JSON)
				.param("accountId", "13")
				.param("groupId", "15")
				.param("emailId", "sahilwatta@gmail.com")
				.param("roleId", "2")
				)
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.status").value("404"))
				.andExpect(jsonPath("$.error").value("Invalid Input"));
	}
	
	/**
	 * test createGroup with input Group Name same
	 * as the expired group
	 */
	@Test  
	public void addGroupWithSameNameAsExpiredGroup() throws Exception {  

		this.mockMvc.perform(post(GROUP_URL+"/createGroup")
				.contentType(MediaType.APPLICATION_JSON)
				.param("accountId", "3")
				.param("groupName", "group10")
				.param("roleId", "2")
				)
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.status").value("200"));
	}
}
