package aucklanduni.ece.hc.controller;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import aucklanduni.ece.hc.repository.model.Account;
import aucklanduni.ece.hc.repository.model.Appointment;
import aucklanduni.ece.hc.repository.model.AppointmentAccountRef;
import aucklanduni.ece.hc.repository.model.Dictionary;
import aucklanduni.ece.hc.repository.model.Group;
import aucklanduni.ece.hc.service.*;

/**
 * This controller is used to write data into database, so that you don't have to use command prompt to
 * do that.
 * It is created to help to debug, not part of the project.
 * 
 */

@Controller
@RequestMapping("/test")
public class TestController {
	@Autowired
	private AccountService accountService;
	@Autowired
	private ApnUserService apnUserService;
	@Autowired
	private AppointmentService appointmentService;
	@Autowired
	private DictionaryService dictionaryService;
	@Autowired
	private GroupService groupService;
	@Autowired
	private NotifyService notifyService;
	@Autowired
	private TaskService taskService;
	@Autowired
	private AppointmentAccountRefService aafService;
	
	/**
	 * This is a method simply write an appointment into database without any group, account condition.
	 * That is this appointment is isolated.
	 */
	@RequestMapping(value="/createAppointment")
	public String createAppointment(HttpServletRequest request, HttpServletResponse response,
//			@RequestParam(value="appointmentTime") Date appointmentTime,
			@RequestParam(value="appointmentName") String appointmentName){
		Appointment appointment=new Appointment();
		appointment.setTime(new Date());
		appointment.setName(appointmentName);
		try {
			appointmentService.add(appointment);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "createAppointment/ok";
	}
	/**
	 * This method simply write an account into database  
	 */
	@RequestMapping(value="/createAccount")
	public String createAccount(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value="email") String email){
		Account account=new Account();
		account.setEmail(email);
		try {
			accountService.add(account);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "createAccount/ok";
	}
	/**
	 * This method simply write a group into database
	 */
	@RequestMapping(value="/createGroup")
	public String createGroup(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value="groupName") String groupName){
		Group group=new Group();
		group.setGroupname(groupName);
		try {
			groupService.add(group);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "createGroup/ok";
	}
	/**
	 * This method simply write a role into database
	 */
	@RequestMapping(value="/createRole")
	public String createRole(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value="roleName") String roleName,
			@RequestParam(value="roleType") String roleType,
			@RequestParam(value="roleValue") String roleValue){
		Dictionary role=new Dictionary();
		role.setName(roleName);
		role.setType(roleType);
		role.setValue(roleValue);
		try {
			dictionaryService.add(role);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "createRole/ok";
	}
	/**
	 * This method simply write a appointment_account_ref into database
	 */
	@RequestMapping(value="/createRef")
	public String createRef(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value="appointment_id") long appointment_id,
			@RequestParam(value="account_id") long account_id){
		AppointmentAccountRef aaf=new AppointmentAccountRef();
		aaf.setAppointmentId(appointment_id);
		aaf.setAccountId(account_id);
		try {
			aafService.add(aaf);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "createRole/ok";
	}
}
