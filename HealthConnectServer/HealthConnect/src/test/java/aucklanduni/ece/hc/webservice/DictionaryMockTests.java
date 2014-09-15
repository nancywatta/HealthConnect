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
 * @ClassName: DictionaryMockTests 
 * @Description: JUnit test Cases for Dictionary web service
 * @author Nancy Watta
 * @date 2014/09/15 
 *
 */
@TransactionConfiguration(defaultRollback = true) 
@Transactional 
@RunWith(SpringJUnit4ClassRunner.class)
public class DictionaryMockTests extends BaseContextControllerTests {

	/**
	 *  Static String for Dictionary Web Service url
	 */
   private static String URL = "/service/Dictionary";
   
   /**
	 * Test getRoles when Patient wants to invite 
	 * members to the group.
	 */
    @Test  
    public void getRolesForPatient() throws Exception {  

    	this.mockMvc.perform(get(URL+"/showRoles")
        		.contentType(MediaType.APPLICATION_JSON)  
        		.param("groupId", "1")
	      		.param("accountId", "1")
        		)
        		.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("200"))
                .andExpect(jsonPath("$.response.roles").isArray())
                .andExpect((jsonPath("$.response.roles[0].value").value("N")))
                .andExpect((jsonPath("$.response.roles[1].value").value("S")));
    }

    /**
	 * Test getRoles when Nurse wants to invite 
	 * members to the group.
	 */
    @Test  
    public void getRolesForNurse() throws Exception {  

    	this.mockMvc.perform(get(URL+"/showRoles")
        		.contentType(MediaType.APPLICATION_JSON)  
        		.param("groupId", "1")
	      		.param("accountId", "2")
        		)
        		.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("200"))
                .andExpect(jsonPath("$.response.roles").isArray())
                .andExpect((jsonPath("$.response.roles[0].value").value("P")));
    }
    
    /**
	 * Test getRoles when Support Member wants to invite 
	 * members to the group.
	 */
    @Test  
    public void getRolesForSM() throws Exception {  

    	this.mockMvc.perform(get(URL+"/showRoles")
        		.contentType(MediaType.APPLICATION_JSON)  
        		.param("groupId", "1")
	      		.param("accountId", "3")
        		)
        		.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("200"))
                .andExpect(jsonPath("$.response.roles").doesNotExist());
    }

    /**
	 * Test getRoles 
	 */
    @Test  
    public void getRoles() throws Exception {  

    	this.mockMvc.perform(get(URL+"/showRoles")
        		.contentType(MediaType.APPLICATION_JSON)
        		)
        		.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("200"))
                .andExpect(jsonPath("$.response.roles").isArray())
                .andExpect((jsonPath("$.response.roles[0].value").value("P")))
                .andExpect((jsonPath("$.response.roles[1].value").value("N")))
                .andExpect((jsonPath("$.response.roles[2].value").value("S")));
    }
}
