package aucklanduni.ece.hc.webservice;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wordnik.swagger.annotations.Api;

import aucklanduni.ece.hc.repository.model.ApnUser;
import aucklanduni.ece.hc.service.ApnUserService;
import aucklanduni.ece.hc.webservice.model.HCMessage;
/**
 * 
* @ClassName: ApnRestController 
* @Description: This is a demo showing how to create a REST service
* Note that annotation @API is for swagger ui
* All the return object will be wrapped into HCMessage
* @author Zhao Yuan
* @date 2014年9月15日 下午9:15:50 
*
 */
@Api(value = "apnuser", description = "Apnuser Demo")
@RestController
@RequestMapping("/service/apnuser/")
public class ApnRestController {
	@Autowired
	private ApnUserService apnUserService;
	Logger log = Logger.getLogger(ApnRestController.class);
	
	/**
	 * This is to get ApnUser resource, path variable is id
	 * 
	 */
	@RequestMapping(value="/{id}",method = RequestMethod.GET
			,headers="Accept=application/json"
			)
    public HCMessage getApnUser(@PathVariable long id) {
		HCMessage message = new  HCMessage();
        try {
        	ApnUser apnUser =  apnUserService.findById(id);
        	if(apnUser != null){
        		message.setSuccess(apnUser);
        	}else{
        		message.setFail("404", "No valid resource");
        	}
        	
        	log.debug(apnUser);
		} catch (Exception e) {
			e.printStackTrace();
			message.setFail("400", e.getMessage());
		}
        return message;
    }
	
	/**
	 * This is to list ApnUser resource
	 * 
	 */
	@RequestMapping(value="/",method = RequestMethod.GET
			,headers="Accept=application/json"
			)
	public HCMessage listApnUser() {
		HCMessage message = new  HCMessage();
		List<ApnUser> list = new ArrayList<ApnUser> ();
        try {
        	list =  apnUserService.findAll();
        	message.setSuccess(list);
		} catch (Exception e) {
			e.printStackTrace();
			message.setFail("400", e.getMessage());
		}
        return message;
    }
	/**
	 * This is to create ApnUser resource
	 * 
	 */
	@RequestMapping(value="/",method = RequestMethod.POST
			,headers="Accept=application/json"
			)
	public HCMessage addApnUser(@RequestParam(value="email",required = true) String email
			,@RequestParam(value="name",required=false) String name
			,@RequestParam(value="password",required=false) String password
			,@RequestParam(value="username",required=true) String username
			) {
		HCMessage message = new  HCMessage();
		ApnUser apnUser = new ApnUser();
		apnUser.setCreateDate(new Date());
		apnUser.setEmail(email);
		apnUser.setName(name);
		apnUser.setPassword(password);
		apnUser.setUsername(username);
		
        try {
        	apnUserService.add(apnUser);
        	message.setSuccess(apnUser);
		} catch (Exception e) {
			e.printStackTrace();
			message.setFail("400", e.getMessage());
		}
        return message;
    }
	
	/**
	 * This is to update ApnUser resource
	 * 
	 */
	@RequestMapping(value="/{id}",method = RequestMethod.PUT
			,headers="Accept=application/json"
			)
	public HCMessage updateApnUser(@PathVariable long id
			,@RequestParam(value="email",required = true) String email
			,@RequestParam(value="name",required=false) String name
			,@RequestParam(value="password",required=false) String password
			,@RequestParam(value="username",required=true) String username
			) {
		HCMessage message = new  HCMessage();
		ApnUser apnUser = new ApnUser();
		
        try {
        	apnUser = apnUserService.findById(id);
        	if(apnUser == null){
        		message.setFail("404", "No valid resource");
        		return message;
        	}
        	
        	apnUser.setEmail(email);
        	apnUser.setName(name);
        	apnUser.setPassword(password);
        	apnUser.setUsername(username);
        	apnUser.setUpdatedDate(new Date());
        	
        	apnUserService.update(apnUser);
        	message.setSuccess(apnUser);
		} catch (Exception e) {
			e.printStackTrace();
			message.setFail("400", e.getMessage());
		}
        return message;
    }
	
	/**
	 * This is to delete ApnUser resource
	 * 
	 */
	@RequestMapping(value="/{id}",method = RequestMethod.DELETE
			,headers="Accept=application/json"
			)
	public HCMessage deleteApnUser(@PathVariable long id) {
		HCMessage message = new  HCMessage();
		ApnUser apnUser = new ApnUser();
		
        try {
        	apnUser = apnUserService.findById(id);
        	if(apnUser == null){
        		message.setFail("404", "No valid resource");
        		return message;
        	}

        	apnUserService.deleteById(id);
        	message.setSuccess();
		} catch (Exception e) {
			e.printStackTrace();
			message.setFail("400", e.getMessage());
		}
        return message;
    }
}
