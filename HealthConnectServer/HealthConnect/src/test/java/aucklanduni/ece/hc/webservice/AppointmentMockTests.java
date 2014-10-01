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
	 * test createAppointment with correct inputs
	 */
	@Test  
	public void addAppointmentCorrect() throws Exception {  

		this.mockMvc.perform(post(URL+"/createAppointment")
				.contentType(MediaType.APPLICATION_JSON)
				.param("accountId", "1")
				.param("roleId", "0")
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
	public void addAppointmentWrongByRole() throws Exception {  

		this.mockMvc.perform(post(URL+"/createGroup")
				.contentType(MediaType.APPLICATION_JSON)
				.param("accountId", "1")
				.param("roleId", "3")//only the nurse can create an appointment
				.param("appointmentName", "withLeehom")
				.param("appointmentLocation", "Cafe")
				)
				.andDo(print())
				.andExpect(jsonPath("$.status").value("404"));
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
				.andExpect(jsonPath("$.status").value("404"));
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
	 * test filterAppsByUsername : The groupId must exist.
	 */
	@Test 
	public void filterAppsByUsernameinvaildaccountId() throws Exception {  
		this.mockMvc.perform(post(URL+"/filterAppsByUsername")
				.contentType(MediaType.APPLICATION_JSON)
				.param("accountId", "1000000")//this accountId is not exist
				.param("roleId", "2")
				.param("username", "")
				)
				.andDo(print())
				.andExpect(jsonPath("$.status").value("404"));
	}
	/**
	 * test filterAppsByUsername : Nurse filter appointments when he/she have a group as a nurse.
	 */
	@Test 
	public void filterAppsByUsernamenogroups() throws Exception {  
		this.mockMvc.perform(post(URL+"/filterAppsByUsername")
				.contentType(MediaType.APPLICATION_JSON)
				.param("accountId", "2")
				.param("roleId", "2")
				.param("username", "")
				)
				.andDo(print())
				.andExpect(jsonPath("$.status").value("404"))
				.andExpect(jsonPath("$.error").value("invalid input!"));
	}
	/**
	 * test filterAppsByUsername : Only nurse can filter appointments.
	 */
	@Test 
	public void filterAppsByUsernameroleId() throws Exception {  
		this.mockMvc.perform(post(URL+"/filterAppsByUsername")
				.contentType(MediaType.APPLICATION_JSON)
				.param("accountId", "2")
				.param("roleId", "3")
				.param("username", "")
				)
				.andDo(print())
				.andExpect(jsonPath("$.status").value("404"))
				.andExpect(jsonPath("$.error").value("invalid input!"))
				;
	}
	/**
	 * test filterAppsByUsername : Only nurse can filter appointments.
	 */
	@Test 
	public void filterAppsByUserusername() throws Exception {  
		this.mockMvc.perform(post(URL+"/filterAppsByUsername")
				.contentType(MediaType.APPLICATION_JSON)
				.param("accountId", "1")
				.param("roleId", "2")
				.param("username", "lala")
				)
				.andDo(print())
				.andExpect(jsonPath("$.status").value("404"))
				.andExpect(jsonPath("$.error").value("invalid username!"))
				;
    }	
	/**
	 * test filterAppsByUsername with correct inputs
	 */
	@Test 
	public void filterAppsByUsernamecorrect() throws Exception {  
		this.mockMvc.perform(post(URL+"/filterAppsByUsername")
				.contentType(MediaType.APPLICATION_JSON)
				.param("accountId", "1")
				.param("roleId", "2")
				.param("username", "nancy")
				)
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.status").value("200"))
				;
    }	
	
	
	
	/**
	 * test filterAppsByDate : The groupId must exist.
	 */
	@Test 
	public void filterAppsByDateinvaildaccountId() throws Exception {  
		this.mockMvc.perform(post(URL+"/filterAppsByDate")
				.contentType(MediaType.APPLICATION_JSON)
				.param("accountId", "1000000")//this accountId is not exist
				.param("roleId", "2")
				.param("username", "")
				)
				.andDo(print())
				.andExpect(jsonPath("$.status").value("404"));
	}
	/**
	 * test filterAppsByDate : Nurse filter appointments when he/she have a group as a nurse.
	 */
	@Test 
	public void filterAppsByDatenogroups() throws Exception {  
		this.mockMvc.perform(post(URL+"/filterAppsByDate")
				.contentType(MediaType.APPLICATION_JSON)
				.param("accountId", "2")
				.param("roleId", "2")
				.param("username", "")
				.param("startDate", "2014-02-10")
				.param("endDate", "2016-02-10")
				)
				.andDo(print())
				.andExpect(jsonPath("$.status").value("404"))
				.andExpect(jsonPath("$.error").value("invalid input!"))
				;
	}
	/**
	 * test filterAppsByDate : Only nurse can filter appointments.
	 */
	@Test 
	public void filterAppsByDateroleId() throws Exception {  
		this.mockMvc.perform(post(URL+"/filterAppsByDate")
				.contentType(MediaType.APPLICATION_JSON)
				.param("accountId", "2")
				.param("roleId", "3")
				.param("username", "")
				.param("startDate", "2014-02-10")
				.param("endDate", "2016-02-10")
				)
				.andDo(print())
				.andExpect(jsonPath("$.status").value("404"))
				.andExpect(jsonPath("$.error").value("only nurse can do this action!"));
	}
	/**
	 * test filterAppsByDate : Only nurse can filter appointments.
	 */
	@Test 
	public void filterAppsByDateusername() throws Exception {  
		this.mockMvc.perform(post(URL+"/filterAppsByDate")
				.contentType(MediaType.APPLICATION_JSON)
				.param("accountId", "2")
				.param("roleId", "3")
				.param("username", "lala")
				.param("startDate", "2014-02-10")
				.param("endDate", "2016-02-10")
				)
				.andDo(print())
				.andExpect(jsonPath("$.status").value("404"))
				.andExpect(jsonPath("$.error").value("invalid username!"))
				;
    }	
//	/**
//	 * test filterAppsByDate with correct inputs
//	 */
//	@Test 
//	public void filterAppsByDatecorrect() throws Exception {  
//		this.mockMvc.perform(post(URL+"/filterAppsByDate")
//				.contentType(MediaType.APPLICATION_JSON)
//				.param("accountId", "1")
//				.param("roleId", "2")
//				.param("username", "nancy")
//				)
//				.andDo(print())
//				.andExpect(status().isOk())
//				.andExpect(jsonPath("$.status").value("200"))
//				;
//    }	
}