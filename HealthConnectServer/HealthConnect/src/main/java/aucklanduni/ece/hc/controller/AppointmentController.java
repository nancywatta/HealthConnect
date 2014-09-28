package aucklanduni.ece.hc.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Date;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
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

	Logger log = Logger.getLogger(AppointmentController.class);
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
	@Autowired
	private NotifyService notifyService;
	private Gson gson = new Gson();
	
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
	 * @param groupId
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
			
			// check if appointment name already exists
//			List<Appointment> appointments = new ArrayList<Appointment>();
//			appointments = appointmentService.findByHql("from Appointment app WHERE app.name="+ appointmentName);
//			if(appointments.size() > 0) {
//				throw new ValidationFailException("Appointment Name Already Exists");
//			}
			
			Appointment appointment=new Appointment();
			AppointmentAccountRef aaf=new AppointmentAccountRef();
			appointment.setTime(new Date());
			appointment.setName(appointmentName);
			appointment.setLocation(appointmentLocation);
			appointment.setCreateDate(new Date());
			appointment.setUpdatedDate(new Date());
			appointmentService.add(appointment);
			
			//update the reference table
			aaf.setAccountId(accountId);
			aaf.setAppointmentId(appointment.getId());
			aafService.add(aaf);
			notifyService.notify(accountId, "You already create an appountment", "email");
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		return "createAppointment/ok";
	}
	
	/**
	 * This method is used to handle /Appointment/shareAppointment request, which share an appointment to a
	 * group. 
	 * The parameter members is a String of all account's email address that you wish to share the
	 * appointment, and these addresses are separated by ",". For example: wuke12@gg,Ben18@hg,gajing@kk
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
			@RequestParam(value="appointmentId") long appointmentId,
			@RequestParam(value="members",required=false) String members){
		
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
			
			if(members==null){
			
				//update the relationship in database
				List<Account> accounts=accountService.getAccbyAppointmentId(appointment.getId());
				for(Account account:accounts){
					Member member=new Member();
					member.setAccountId(account.getId());
					member.setGroupId(group.getId());
					memberService.add(member);
				}
			}
			
			else{
				StringTokenizer st=new StringTokenizer(members,",");
				while(st.hasMoreTokens()){
					String memberEmail=st.nextToken();
					if(memberService.isMember(accountService.getAccIdByEmail(memberEmail),group.getId())){
						Member member=new Member();
						member.setAccountId(accountService.getAccIdByEmail(memberEmail));
						member.setGroupId(group.getId());
						memberService.add(member);
					}
				}
			}
		} catch(Exception e){
			e.printStackTrace();
		}
		return "shareAppointment/ok";
	}

	
	@RequestMapping("/viewAppointment")//As nurses or patients, they can view the appointments.
	@ResponseBody
	public String viewAppointment(HttpServletRequest request, HttpServletResponse response,
			@RequestParam("accountId") long accountId,
			@RequestParam(value="groupId") long groupId){
		log.debug(">>>>>>>>>>>>>>>>>viewAppointment"+accountId);
		try{
			
			Account account = null;
			// check if given accountId exists
			account = accountService.findById(accountId);
			if(account == null) {
				throw new ValidationFailException("Account does not exist");
			}
			//add nurse and patient can view appointment
			
			
			// print out the appointments that the user created.
			    System.out.println("These are the appointments that you create");
				List<Appointment> appointments=appointmentService.findAllByAccountId(accountId);
				for(Appointment appointment:appointments){
					log.debug("appointmentName="+appointment.getName()+"   appointmentLocation="+appointment.getLocation());
			// print out the appointments that being shared in groups.
				//System.out.println("These are the appointments that being shared in your group");	
				//List<Appointment> appointments2=appointmentService.findAllByGroupShared(accountId);
				//for(Appointment appointment2:appointments2){
					//log.debug("appointmentName="+appointment2.getName()+"   appointmentLocation="+appointment2.getLocation());
				//}
					
				};
			
			
			//List<Appointment> appointments=appointmentService.findAllByAccountId(accountId);
			//for(Appointment appointment:appointments){
			//	log.debug("appointmentName="+appointment.getName()+"   appointmentLocation="+appointment.getLocation());
			//}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return "viewAppointment/ok";
	}

	@RequestMapping("/viewAppointmentByGroup")//As nurses or patients, they can view the appointments.
	@ResponseBody
	public String viewAppointmentByGroup(HttpServletRequest request, HttpServletResponse response,
			@RequestParam("accountId") long accountId,
			@RequestParam(value="groupId") long groupId){
		log.debug(">>>>>>>>>>>>>>>>>viewAppointment"+accountId+"by"+groupId);
		try{
			
			Account account = null;
			// check if given accountId exists
			account = accountService.findById(accountId);
			if(account == null) {
				throw new ValidationFailException("Account does not exist");
			}
			//add nurse and patient can view appointment
			Group group=null;
			// check if given group exists
			group=groupService.findById(groupId);
			if(group==null){
				throw new ValidationFailException("such group does not exist");
			}
			
			// print out the appointments that the user created.
			    //System.out.println("These are the appointments that you create");
				List<Appointment> appointments=appointmentService.findAllByGroupId(accountId,groupId);
				for(Appointment appointment:appointments){
					log.debug("appointmentName="+appointment.getName()+"   appointmentLocation="+appointment.getLocation());
					// print out the appointments that being shared in groups.
					//System.out.println("These are the appointments that being shared in your group");	
					//List<Appointment> appointments2=appointmentService.findAllByGroupShared(accountId);
					//for(Appointment appointment2:appointments2){
						//log.debug("appointmentName="+appointment2.getName()+"   appointmentLocation="+appointment2.getLocation());
					//}
						
					};
				
				
				//List<Appointment> appointments=appointmentService.findAllByAccountId(accountId);
				//for(Appointment appointment:appointments){
				//	log.debug("appointmentName="+appointment.getName()+"   appointmentLocation="+appointment.getLocation());
				//}
			}
			catch(Exception e){
				e.printStackTrace();
			}
			return "viewAppointmentByGroup/ok";
		}
	
//	@RequestMapping(value="/viewAppointments")
//	@ResponseBody
//	public String viewAppointments(HttpServletRequest request, HttpServletResponse response,
//			@RequestParam("accountId") long accountId){
//		String appointments = null;
//		List<Appointment> appointmentList = new ArrayList<Appointment>();
//		Map<String, ArrayList<Appointment>> appointmentArray = new HashMap<String, ArrayList<Appointment>>();
//		try {
//			appointmentList = appointmentService.findByHql("select distinct g from Appointment g, "
//					+ "inner join app_acc_ref aar "
//					+ "on "
//					+ "g.id = arr.appointment_id"
//					+ "inner join account ac"
//					+ "on"
//					+ "ac.id=aar.account_id "
//					+ "and aar.account_id= " + accountId);
//			appointmentArray.put("appointments", (ArrayList<Appointment>)appointmentList);
//			appointments = gson.toJson(appointmentArray);
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return appointments;
//	}
	
}
