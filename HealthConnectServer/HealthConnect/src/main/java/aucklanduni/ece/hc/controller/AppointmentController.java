package aucklanduni.ece.hc.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;

import aucklanduni.ece.hc.repository.model.Account;
import aucklanduni.ece.hc.repository.model.Appointment;
import aucklanduni.ece.hc.repository.model.AppointmentAccountRef;
import aucklanduni.ece.hc.repository.model.Dictionary;
import aucklanduni.ece.hc.repository.model.Group;
import aucklanduni.ece.hc.repository.model.Member;
import aucklanduni.ece.hc.service.AccountService;
import aucklanduni.ece.hc.service.AppointmentAccountRefService;
import aucklanduni.ece.hc.service.AppointmentService;
import aucklanduni.ece.hc.service.DictionaryService;
import aucklanduni.ece.hc.service.GroupService;
import aucklanduni.ece.hc.service.MemberService;
import aucklanduni.ece.hc.service.NotifyService;
import aucklanduni.ece.hc.webservice.model.ValidationFailException;

/**
 * author: Tech Geeks
 * time:Sep.2014
 * This AppointmentController sets main functions of appointments, including creating, viewing, sharing, filtering.
 * Once modifying one of these functions, the relevant files need to be modified.
 * Relevant files could include AppointmentService, Appointment, ApppintmentDao, AppointmetDaoImpl.
 * 
 */
@Controller
@RequestMapping("/Appointment")
public class AppointmentController {
	@Autowired
	private AppointmentService appointmentService;
	@Autowired
	private AccountService accountService;
	@Autowired
	private DictionaryService roleService;
	@Autowired
	private GroupService groupService;
	@Autowired
	private AppointmentAccountRefService aafService;
	@Autowired
	private MemberService memberService;
	
	/**
	 * This method is used to handle /Appointment/createAppointment request. It create an appointment record
	 * if the current user is a nurse.
	 * @param request
	 * @param response
	 * @param accountId
	 * @param roleId
	 * @param appointmentTime
	 * @param appointmentName
	 * @param appointmentLocation
	 * @return
	 */
	@RequestMapping("/createAppointment")//As nurses or patients, they can create appointments.
	@ResponseBody
	public String createAppointment(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value="accountId") long accountId,
			@RequestParam(value="roleId") long roleId,
//			@RequestParam(value="appointmentTime") Date appointmentTime,
			@RequestParam(value="appointmentName") String appointmentName,
			@RequestParam(value="appointmentLocation") String appointmentLocation) {
		
		try {
			Dictionary role = null;
			role = roleService.findById(roleId);
			// check if the current user is a nurse
			if(! (role.getValue().compareTo("N")==0 ) ) {
				throw new ValidationFailException("Only Nurse can create an appointment");
			}
			
			Account account = null;
			// check if given accountId exists
			account = accountService.findById(accountId);
			if(account == null) {
				throw new ValidationFailException("Account does not exist");
			}
			
			Appointment appointment=new Appointment();
			AppointmentAccountRef aaf=new AppointmentAccountRef();
			appointment.setTime(new Date());
			appointment.setName(appointmentName);
			appointment.setLocation(appointmentLocation);
			appointmentService.add(appointment);
			
			//update the reference table
			aaf.setAccountId(accountId);
			aaf.setAppointmentId(appointment.getId());
			aafService.add(aaf);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return "createAppointment/ok";
	}
	
	/**
	 * This method is used to handle /Appointment/shareAppointment request, which share an appointment to a
	 * group. 
	 * @param request
	 * @param response
	 * @param groupId
	 * @param memberId
	 * @param appointmentId
	 * @return
	 */
	@RequestMapping("/shareAppointment")
	@ResponseBody
	public String shareGroup(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value="groupId") long groupId,
			@RequestParam(value="appointmentId") long appointmentId){
		try{
			Appointment appointment=null;
			// check if given appointment exists
			appointment=appointmentService.findById(appointmentId);
			if(appointment==null){
				throw new ValidationFailException("such appointment does not exist");
			}
			
			Group group=null;
			// check if given group exists
			group=groupService.findById(groupId);
			if(group==null){
				throw new ValidationFailException("such group does not exist");
			}
			
			//update the relationship in database
			Account account=accountService.getAccbyAppointmentId(appointment.getId());
			Member member=new Member();
			member.setAccountId(account.getId());
			member.setGroupId(group.getId());
			memberService.add(member);
			
		} catch(Exception e){
			e.printStackTrace();
		}
		return "shareAppointment/ok";
	}

	
	@RequestMapping("/viewAppointment")//As nurses or patients, they can view the appointments.
	@ResponseBody
	public String viewAppointment(HttpServletRequest request, HttpServletResponse response,
			@RequestParam("accountId") long accountId){
		System.out.println(">>>>>>>>>>>>>>>>>viewAppointment"+accountId);
		String appointments=null;
		try{
			Map<String, ArrayList<Appointment>> appointmentList=appointmentService.showAllAppointment(accountId);
			Gson gson = new Gson();
			appointments = gson.toJson(appointmentList);
			System.out.println(appointments);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return appointments;
	} 
	
}
