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
}
