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
@Api(value = "apnuser", description = "Apnuser Demo")
@RestController
@RequestMapping("/service/apnuser/")
public class ApnRestController {
	@Autowired
	private ApnUserService apnUserService;
	Logger log = Logger.getLogger(ApnRestController.class);
	
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
	
	@RequestMapping(value="/add",method = RequestMethod.POST
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
	

	@RequestMapping(value="/{id}/update",method = RequestMethod.POST
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
	

	@RequestMapping(value="/{id}/delete",method = RequestMethod.GET
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
