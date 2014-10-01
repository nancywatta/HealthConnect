package aucklanduni.ece.hc.webservice;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 * @ClassName: AppointmentMockTests 
 * @Description: JUnit test Cases for Appointment web service
 * @author Tech Geeks
 * @date 2014/09/16
 *
 */
@TransactionConfiguration(defaultRollback = true) 
@Transactional 
@RunWith(SpringJUnit4ClassRunner.class)
public class AppointmentMockTests extends BaseContextControllerTests {
	
	/**
	 *  Static String for Appointment Web Service url
	 */
   private static String URL = "/service/appointment";
   
   /**
	 * test viewAppointment for valid account 
	 */
	@Test  
	public void viewAppointmentForValidAccount() throws Exception {  

		this.mockMvc.perform(get(URL+"/viewAppointment")
				.contentType(MediaType.APPLICATION_JSON)
				.param("accountId", "1")
				)
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.status").value("200"))
				.andExpect(jsonPath("$.response.appointments").isArray())
				.andExpect(jsonPath("$.response.appointments[0].appointmentname").value("haha"));
	}

	/**
	 * test appointment for invalid account 
	 */
	@Test  
	public void viewAppointmentForInvalidAccount() throws Exception {  

		this.mockMvc.perform(get(URL+"/viewAppointment")
				.contentType(MediaType.APPLICATION_JSON)
				.param("accountId", "199")
				)
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.status").value("404"))
				.andExpect(jsonPath("$.error").value("Account does not exist"));
	}
	
	/**
	 * test viewAppointment for account that does not have any 
	 * appointment created yet  
	 */
	@Test  
	public void emptyAppointment() throws Exception {  

		this.mockMvc.perform(get(URL+"/viewAppointment")
				.contentType(MediaType.APPLICATION_JSON)
				.param("accountId", "2")
				)
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.status").value("404"))
				.andExpect(jsonPath("$.error").value("No Appointment Exists for given Account"));
	}	

	
	/**
	 * test createAppointment with correct inputs
	 */
	@Test  
	public void addAppointmentCorrect() throws Exception {  

		this.mockMvc.perform(post(URL+"/createAppointment")
				.contentType(MediaType.APPLICATION_JSON)
				.param("accountId", "1")
				.param("groupId", "1")
				.param("appointmentName", "withJam")
				.param("appointmentLocation", "University")
				)
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.status").value("200"));
	}

	/**
	 * test createAppointment with invalid inputs
	 */
	@Test  
	public void addAppointmentWrongByGroup() throws Exception {  

		this.mockMvc.perform(post(URL+"/createAppointment")
				.contentType(MediaType.APPLICATION_JSON)
				.param("accountId", "1")
				.param("groupId", "10000")//
				.param("appointmentName", "withLeehom")
				.param("appointmentLocation", "Cafe")
				)
				.andDo(print())
				.andExpect(jsonPath("$.status").value("404"))
				.andExpect(jsonPath("$.error").value("Group does not exist"));
	}

	/**
	 * test createAppointment with invalid inputs
	 */
	@Test  
	public void addAppointmentWrongByAccount() throws Exception {  

		this.mockMvc.perform(post(URL+"/createApppointment")
				.contentType(MediaType.APPLICATION_JSON)
				.param("accountId", "100000")//not found
				.param("roleId", "0")
				.param("appointmentName", "withRose")
				.param("appointmentLocation", "Boston")
				)
				.andDo(print())
				.andExpect(jsonPath("$.status").value("404"))
				.andExpect(jsonPath("$.error").value("Account does not exist"));
	}
	
	/**
	 * test createAppointment with invalid inputs
	 */
	@Test  
	public void addAppointmentWrongBySameName() throws Exception {  
		this.mockMvc.perform(post(URL+"/createAppointment")
				.contentType(MediaType.APPLICATION_JSON)
				.param("accountId", "1")
				.param("roleId", "0")
				.param("appointmentName", "withAlick")
				.param("appointmentLocation", "teaHouse")
				);
		this.mockMvc.perform(post(URL+"/createAppointment")
				.contentType(MediaType.APPLICATION_JSON)
				.param("accountId", "1")
				.param("roleId", "0")
				.param("appointmentName", "withAlick")//same name
				.param("appointmentLocation", "teaHouse")
				)
				.andDo(print())
				.andExpect(jsonPath("$.status").value("404"));
	}
	
	/**
	 * test updateAppointment with invalid inputs
	 */
	@Test  
	public void updateAppointmentWrongByAppointment() throws Exception {  

		this.mockMvc.perform(post(URL+"/updateAppointment")
				.contentType(MediaType.APPLICATION_JSON)
				.param("accountId", "1")//not found
				.param("groupId", "1")
				.param("appointmentId", "10000")
				.param("appointmentLocation", "Boston")
				)
				.andDo(print())
				.andExpect(jsonPath("$.status").value("404"))
				.andExpect(jsonPath("$.error").value("Appointment does not exist"));
	}
	/**
	 * test updateAppointment with invalid inputs
	 */
	@Test  
	public void updateAppointmentWrongByAccount() throws Exception {  

		this.mockMvc.perform(post(URL+"/updateAppointment")
				.contentType(MediaType.APPLICATION_JSON)
				.param("accountId", "1")//not found
				.param("groupId", "1")
				.param("appointmentId", "10000")
				.param("appointmentLocation", "Boston")
				)
				.andDo(print())
				.andExpect(jsonPath("$.status").value("404"))
				.andExpect(jsonPath("$.error").value("Account does not exist"));
	}
	/**
	 * test updateAppointment with invalid inputs
	 */
	@Test  
	public void updateAppointmentWrongByGroup() throws Exception {  

		this.mockMvc.perform(post(URL+"/updateAppointment")
				.contentType(MediaType.APPLICATION_JSON)
				.param("accountId", "1")//not found
				.param("groupId", "100000")
				.param("appointmentId", "1")
				.param("appointmentLocation", "Boston")
				)
				.andDo(print())
				.andExpect(jsonPath("$.status").value("404"))
				.andExpect(jsonPath("$.error").value("Group does not exist"));
	}
	
	
	//Junit test for view appointments by group
		 /**
			 * test viewAppointment for valid account 
			 */
			@Test  
			public void viewAppointmentByGroupForValidAccount() throws Exception {  

				this.mockMvc.perform(get(URL+"/viewAppointmentByGroup")
						.contentType(MediaType.APPLICATION_JSON)
						.param("accountId", "1")
						.param("groupId", "1")
						)
						.andDo(print())
						.andExpect(status().isOk())
						.andExpect(jsonPath("$.status").value("200"))
						.andExpect(jsonPath("$.response.appointments").isArray())
						.andExpect(jsonPath("$.response.appointments[0].appointmentname").value("haha"));
			}

			/**
			 * test appointment for invalid account 
			 */
			@Test  
			public void viewAppointmentbyGroupForInvalidAccount() throws Exception {  

				this.mockMvc.perform(get(URL+"/viewAppointmentByGroup")
						.contentType(MediaType.APPLICATION_JSON)
						.param("accountId", "199")
						.param("groupId", "1")
						)
						.andDo(print())
						.andExpect(status().isOk())
						.andExpect(jsonPath("$.status").value("404"))
						.andExpect(jsonPath("$.error").value("Account does not exist"));
			}
			
			/**
			 * test appointment for invalid account 
			 */
			@Test  
			public void viewAppointmentbyGroupForInvalidGroup() throws Exception {  

				this.mockMvc.perform(get(URL+"/viewAppointmentByGroup")
						.contentType(MediaType.APPLICATION_JSON)
						.param("accountId", "1")
						.param("groupId", "199")
						)
						.andDo(print())
						.andExpect(status().isOk())
						.andExpect(jsonPath("$.status").value("404"))
						.andExpect(jsonPath("$.error").value("Group does not exist"));
			}
			
			
			/**
			 * test viewAppointment for account that does not have any 
			 * appointment created yet  
			 */
			@Test  
			public void emptyAppointmentByGroup() throws Exception {  

				this.mockMvc.perform(get(URL+"/viewAppointmentByGroup")
						.contentType(MediaType.APPLICATION_JSON)
						.param("accountId", "3")
						)
						.andDo(print())
						.andExpect(status().isOk())
						.andExpect(jsonPath("$.status").value("404"))
						.andExpect(jsonPath("$.error").value("No Appointment Exists for given Account"));
			}	

	/**
	 * test filterAppointment: The accountId must exist.
	 */
	@Test 
	public void filterAppointmentinvaildaccountId() throws Exception {  
		this.mockMvc.perform(post(URL+"/filterAppointment")
				.contentType(MediaType.APPLICATION_JSON)
				.param("accountId", "1000000")//this accountId is not exist
				)
				.andDo(print())
				.andExpect(jsonPath("$.status").value("404"))
				.andExpect(jsonPath("$.error").value("Account does not exist"));
	}
	/**
	 * test filterAppointment: If input memberId, the account must have the member.
	 */
	@Test 
	public void filterAppointmentinvailmemeberId() throws Exception {  
		this.mockMvc.perform(post(URL+"/filterAppointment")
				.contentType(MediaType.APPLICATION_JSON)
				.param("accountId", "1")//this accountId is not exist
				.param("memberId", "34")//this memberId does not belongs to the account
				
				)
				.andDo(print())
				.andExpect(jsonPath("$.status").value("404"))
				.andExpect(jsonPath("$.error").value("Member does not exist"));
	}
	/**
	 * test filterAppointment: If user does not input groupId, then it  will
	 * find all groups in which the accountId and memberId are part of.
	 */
	@Test 
	public void filterAppointmentnogroups() throws Exception {  
		this.mockMvc.perform(post(URL+"/filterAppointment")
				.contentType(MediaType.APPLICATION_JSON)
				.param("accountId", "3")
				.param("memberId", "2")
				
				)
				.andDo(print())
				.andExpect(jsonPath("$.status").value("404"))
				.andExpect(jsonPath("$.error").value("Invalid Input"));
	}
	/**
	 * test filterAppointment: If user does not input groupId, then it  will
	 * find all groups in which the accountId and memberId are part of.
	 */
	@Test 
	public void filterAppointmentgroupId() throws Exception {  
		this.mockMvc.perform(post(URL+"/filterAppointment")
				.contentType(MediaType.APPLICATION_JSON)
				.param("accountId", "1")
				.param("memberId", "1")
				.param("groupId", "100000")
				
				)
				.andDo(print())
				.andExpect(jsonPath("$.status").value("404"))
				.andExpect(jsonPath("$.error").value("Group does not exist"));
	}
	
	/**
	 * test filterAppointment: the input startDate should be suitable*/
	@Test 
	public void filterAppointmentinvalidSDate() throws Exception {  
		this.mockMvc.perform(post(URL+"/filterAppointment")
				.contentType(MediaType.APPLICATION_JSON)
				.param("accountId", "1")
				.param("startDate","17536287")
				.param("endDate", "2016-02-10")
				
				)
				.andDo(print())
				.andExpect(jsonPath("$.status").value("404"));
	}
   
	/**
	 * test filterAppointment: the input endDate should be suitable*/
	@Test 
	public void filterAppointmentinvalidEDate() throws Exception {  
		this.mockMvc.perform(post(URL+"/filterAppointment")
				.contentType(MediaType.APPLICATION_JSON)
				.param("accountId", "1")
				.param("startDate","2014-02-10")
				.param("endDate", "45466575")
				
				)
				.andDo(print())
				.andExpect(jsonPath("$.status").value("404"));
	}
	
	/**
	 * test filterAppointment: the input Date should be suitable*/
	@Test 
	public void filterAppointmentinvalidDate() throws Exception {  
		this.mockMvc.perform(post(URL+"/filterAppointment")
				.contentType(MediaType.APPLICATION_JSON)
				.param("accountId", "1")
				.param("startDate","gdfjjerhrj")
				.param("endDate", "45466575")
				
				)
				.andDo(print())
				.andExpect(jsonPath("$.status").value("404"));
	}
//	/**
//	 * test filterAppsByDate : Nurse filter appointments when he/she have a group as a nurse.
//	 */
//	@Test 
//	public void filterAppsByDatenogroups() throws Exception {  
//		this.mockMvc.perform(post(URL+"/filterAppsByDate")
//				.contentType(MediaType.APPLICATION_JSON)
//				.param("accountId", "2")
//				.param("roleId", "2")
//				.param("username", "")
//				.param("startDate", "2014-02-10")
//				.param("endDate", "2016-02-10")
//				)
//				.andDo(print())
//				.andExpect(jsonPath("$.status").value("404"))
//				.andExpect(jsonPath("$.error").value("invalid input!"))
//				;
//	}


}