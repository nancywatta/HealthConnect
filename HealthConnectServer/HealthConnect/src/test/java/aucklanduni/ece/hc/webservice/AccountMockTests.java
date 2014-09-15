package aucklanduni.ece.hc.webservice;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
 * @ClassName: AccountMockTests 
 * @Description: JUnit test Cases for Account web service
 * @author Nancy Watta
 * @date 2014/09/15 
 *
 */
@TransactionConfiguration(defaultRollback = true) 
@Transactional 
@RunWith(SpringJUnit4ClassRunner.class)

public class AccountMockTests extends BaseContextControllerTests {

	/**
	 *  Static String for Account Web Service url
	 */
	private static String URL = "/service/Account";

	/**
	 * test login for given emailId 
	 */
	@Test  
	public void loginWithDefaultPass() throws Exception {  

		this.mockMvc.perform(get(URL+"/login")
				.contentType(MediaType.APPLICATION_JSON)
				.param("emailId", "nancywatta@gmail.com")
				)
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.status").value("200"))
				.andExpect(jsonPath("$.response.email").value("nancywatta@gmail.com"))
				;  
	}

	/**
	 * test login for given emailId and password
	 */
	@Test  
	public void loginWithPass() throws Exception {  

		this.mockMvc.perform(get(URL+"/login")
				.contentType(MediaType.APPLICATION_JSON)
				.param("emailId", "nancy.watta@gmail.com")
				.param("password", "nancy")
				)
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.status").value("200"))
				.andExpect(jsonPath("$.response.email").value("nancy.watta@gmail.com"))
				;  
	}

	/**
	 * test login for given emailId and wrong password
	 */
	@Test  
	public void loginWrongPass() throws Exception {  

		this.mockMvc.perform(get(URL+"/login")
				.contentType(MediaType.APPLICATION_JSON)
				.param("emailId", "nancy.watta@gmail.com")
				.param("password", "nancy1")
				)
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.status").value("404"))
				.andExpect(jsonPath("$.error").value("Incorrect password"))
				;  
	}

	/**
	 * test register for given emailId and password
	 */
	@Test  
	public void registerAccount() throws Exception {  

		this.mockMvc.perform(get(URL+"/login")
				.contentType(MediaType.APPLICATION_JSON)
				.param("emailId", "nancy.watta88@gmail.com")
				.param("password", "nancy1")
				)
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.status").value("200"))
				.andExpect(jsonPath("$.response.email").value("nancy.watta88@gmail.com"))
				;  
	}

}
