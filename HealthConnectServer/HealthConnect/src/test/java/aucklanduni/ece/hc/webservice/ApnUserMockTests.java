package aucklanduni.ece.hc.webservice;

import org.junit.Test;  
import org.junit.runner.RunWith;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;  
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;  
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;  
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*; 

/**
 * 
* @ClassName: ApnUserMockTests 
* @Description: This is a demo showing how do unit test for a web service
* @author Zhao Yuan
* @date 2014年9月13日 下午12:33:50 
*
 */
@TransactionConfiguration(defaultRollback = true) 
@Transactional 
@RunWith(SpringJUnit4ClassRunner.class)  
public class ApnUserMockTests extends BaseContextControllerTests {  
	/**
	 * This static String should match your controller request url
	 */
   private static String URL = "/service/apnuser";
   
   
	/**
	 * test get apn user by Id
	 * Note that this get(URL+"/2") should match your entire url
     * i.e. @RequestMapping(value="/{id}",method = RequestMethod.GET
     * .andExpect(jsonPath(...)) should match your output json path
	 */
    @Test  
    public void getCorrectApnUser() throws Exception {  

        this.mockMvc.perform(get(URL+"/1")
        		.contentType(MediaType.APPLICATION_JSON)  
        		)
        		.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("200"))
                .andExpect(jsonPath("$.response.id").value(1))
                ;  
    }
    
    @Test  
    public void getWrongApnUser() throws Exception {  
        this.mockMvc.perform(get(URL+"/1000")
        		.contentType(MediaType.APPLICATION_JSON)  
        		)
        		.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("404"))
                ;  
    } 
      
    /**
     * Note that here it should use post(URL) 
     * as @RequestMapping(value="/{id}/update",method = RequestMethod.POST
     * If you want to post params, just add .param(name,value) behind 
     * @Rollback(false)  use this annotation when you dont want to rollback. (i.e you need an exception)
     */
  @Test  
  public void updateCorrectApnUser() throws Exception {  
      this.mockMvc.perform(post(URL+"/{id}/update",1)
	      		.contentType(MediaType.APPLICATION_JSON)
	      		.param("email", "newEmail@cc.com")
	      		.param("username", "newUserName")
	      		)
				.andDo(print())
				.andExpect(jsonPath("$.status").value("200"))
                .andExpect(jsonPath("$.response.id").value(3))
                .andExpect(jsonPath("$.response.email").value("newEmail@cc.com"))
                .andExpect(jsonPath("$.response.username").value("newUserName"))
				;  
  } 
  
  @Test  
  public void updateWrongApnUser() throws Exception {  
      this.mockMvc.perform(post(URL+"/{id}/update",10000)
	      		.contentType(MediaType.APPLICATION_JSON)
	      		.param("email", "newEmail@cc.com")
	      		.param("username", "newUserName")
	      		)
				.andDo(print())
				.andExpect(jsonPath("$.status").value("404"));  
  } 
  
  	@Test  
    public void deleteWrongApnUser() throws Exception {  
        this.mockMvc.perform(get(URL+"/10000/delete")
        		.contentType(MediaType.APPLICATION_JSON)
        		)
        		.andDo(print())
                .andExpect(jsonPath("$.status").value("404"));  
    }
    
}
