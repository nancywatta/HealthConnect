package aucklanduni.ece.hc.webservice;

import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;  
import org.springframework.test.context.ContextConfiguration;  
import org.springframework.test.context.web.WebAppConfiguration;  
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;  
  
@WebAppConfiguration  
@ContextConfiguration(locations = { "file:src/main/webapp/WEB-INF/spring/applicationContext.xml",    
"file:src/main/webapp/WEB-INF/spring/spring-mvc.xml" })  
public class BaseContextControllerTests {  
  
    @Autowired  
    protected WebApplicationContext wac; 
	protected MockMvc mockMvc;  

    @Before  
    public void setup() {  
    	mockMvc  = MockMvcBuilders.webAppContextSetup(wac).build();  
    }  
  
}  