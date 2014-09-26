package aucklanduni.ece.hc.webservice;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.fortuna.ical4j.model.Calendar;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import aucklanduni.ece.hc.repository.model.Account;
import aucklanduni.ece.hc.repository.model.ApnUser;
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
import aucklanduni.ece.hc.util.ICalendarTool;
import aucklanduni.ece.hc.webservice.model.HCMessage;
import aucklanduni.ece.hc.webservice.model.ValidationFailException;

@RestController
@RequestMapping("/service/appointment/")
public class AppointmentRestController {

	Logger log = Logger.getLogger(AppointmentRestController.class);
	
	@Autowired
	private AppointmentService appointmentService;
	@Autowired
	private DictionaryService roleService;
	@Autowired
	private AccountService accountService;
	@Autowired
	private AppointmentAccountRefService aafService;
	@Autowired
	private GroupService groupService;
	@Autowired
	private MemberService memberService;
	@Autowired
	private NotifyService notifyService;
	
	/**
	 *  getICal4J object 
	 */
	@RequestMapping(value="/getICal4J/{appointmentId}",method = RequestMethod.GET
			,headers="Accept=application/json"
			)
	public HCMessage getICal4J(HttpServletRequest request, HttpServletResponse response,
			@PathVariable long appointmentId) {
		HCMessage message = new  HCMessage();
		
		try {
			Appointment app =  appointmentService.findById(appointmentId);
			List<Account> accList = accountService.getAccbyAppointmentId(appointmentId);
			
        	if(app == null){
        		message.setFail("404", "No valid appointmentId");
        		return message;
        	}

        	Calendar ical = ICalendarTool.getICal4J(app, accList);
    		message.setSuccess(ical);
        	log.debug(ical);
        	
		} catch (Exception e) {
			e.printStackTrace();
			message.setFail("400", e.getMessage());
		}
        return message;
	}
	
	/**
	 * This is for passing Json object to the method
	 */
	@RequestMapping(value="/createAppointmentByObj",method = RequestMethod.POST
			,headers="Accept=application/json"
			)
	public HCMessage createAppointmentByObj(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value="accountId") long accountId
			,@RequestParam(value="groupId") long groupId
			,@RequestBody Appointment appointment) {

		log.debug(appointment.getTime());
		return this.createAppointment(request, response
				, accountId, groupId
				, appointment.getName(), appointment.getLocation()
				, appointment.getIsShared());
	}

	@RequestMapping(value="/createAppointment",method = RequestMethod.POST
			,headers="Accept=application/json"
			)
	public HCMessage createAppointment(HttpServletRequest request, HttpServletResponse response
			,@RequestParam(value="accountId") long accountId
			,@RequestParam(value="groupId") long groupId
//			,@RequestParam("appointmentTime") Date appointmentTime
			,@RequestParam("appointmentName") String appointmentName
			,@RequestParam("appointmentLocation") String appointmentLocation
			,@RequestParam("isShare") String isShare
//			,@RequestParam(value="members",required=false) String members
			) {
		HCMessage message = new  HCMessage();
		try {
			Account account = null;
			// check if given accountId exists
			account = accountService.findById(accountId);
			if(account == null) {
				throw new ValidationFailException("Account does not exist");
			}
			
			Group group = null;
			//check if given groupId exists
			group = groupService.findById(groupId);
			if(group == null){
				throw new ValidationFailException("Group does not exist");
			}
			
			Dictionary role = null;
			role = roleService.findRoleByAccountIdAndGroupId(accountId,groupId);
			// check if the current user is a nurse or patient
			if( (role.getValue().compareTo("S")==0 ) ) {
				throw new ValidationFailException("Supportive members are not allowed to create an appointment");
			}

			// check if appointment name already exists
//			List<Appointment> appointments = new ArrayList<Appointment>();
//			appointments = appointmentService.findByHql("from Appointment WHERE appointmentname='" + appointmentName + "'");
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
			if(isShare.compareTo("T")==0)
				appointment.setIsShared("T");
			else
				appointment.setIsShared("F");
			appointmentService.add(appointment);
			
			//update the reference table
			aaf.setAccountId(accountId);
			aaf.setAppointmentId(appointment.getId());
			aafService.add(aaf);
			
			notifyService.notify(accountId, "You already create an appountment", "email");
			
			message.setSuccess(appointment);
//			appointmentService.createNewAppointment(accountId, appointmentTime, appointmentName, appointmentLocation);

		}catch(ValidationFailException ve) {
			message.setFail("404", ve.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			message.setFail("400", e.getMessage());
		}

		return message;
	}
	
//	/**
//	 * This method is used to handle /Appointment/shareAppointment request, which share an appointment to a
//	 * group.
//	 * The parameter members is a String of all account's email address that you wish to share the
//	 * appointment, and these addresses are separated by ",". For example: wuke12@gg,Ben18@hg,gajing@kk 
//	 * @param request
//	 * @param response
//	 * @param groupId
//	 * @param appointmentId
//	 * @return
//	 */
//	@RequestMapping(value="/shareAppointment",method = RequestMethod.POST
//			,headers="Accept=application/json"
//			)
//	public HCMessage shareAppointment(HttpServletRequest request, HttpServletResponse response
//			,@RequestParam(value="groupId") long groupId
//			,@RequestParam(value="appointmentId") long appointmentId
//			,@RequestParam(value="members",required=false) String members
//			) {
//		HCMessage message = new  HCMessage();
//		try {
//			Appointment appointment=null;
//			// check if given appointment exists
//			appointment=appointmentService.findById(appointmentId);
//			if(appointment==null){
//				throw new ValidationFailException("such appointment does not exist");
//			}
//
//			Group group=null;
//			// check if given group exists
//			group=groupService.findById(groupId);
//			if(group==null){
//				throw new ValidationFailException("such group does not exist");
//			}
//			
//			if(members==null){
//				//update the relationship in database
//				List<Account> accounts=accountService.getAccbyAppointmentId(appointment.getId());
//				for(Account account:accounts){
//					Member member=new Member();
//					member.setAccountId(account.getId());
//					member.setGroupId(group.getId());
//					memberService.add(member);
//				}
//			}
//			
//			else{
//				StringTokenizer st=new StringTokenizer(members,",");
//				while(st.hasMoreTokens()){
//					String memberEmail=st.nextToken();
//					if(memberService.isMember(accountService.getAccIdByEmail(memberEmail),group.getId())){
//						Member member=new Member();
//						member.setAccountId(accountService.getAccIdByEmail(memberEmail));
//						member.setGroupId(group.getId());
//						memberService.add(member);
//					}
//				}
//			}
//			
//			message.setSuccess();
//
//		}catch(ValidationFailException ve) {
//			message.setFail("404", ve.getMessage());
//		} catch (Exception e) {
//			e.printStackTrace();
//			message.setFail("400", e.getMessage());
//		}
//
//		return message;
//	}



/*
 * /**
	 * 
	 * @Title: viewAppointments 
	 * @Description: Service will return all the appointments of the given
	 * accountId.
	 *  
	 * @param request
	 * @param response
	 * @param accountId 
	 * @return HCMessage
	 * @throws
	 */
	
	@RequestMapping(value="/viewAppointment",method = RequestMethod.GET
			,headers="Accept=application/json"
			)
	public HCMessage showAppointments(HttpServletRequest request, HttpServletResponse response,
			@RequestParam("accountId") long accountId){
		System.out.println(">>>>>>>>>>>>>>>>>viewAppointment"+accountId);
		HCMessage message = new  HCMessage();
		try{
			Account account = null;
			// check if given accountId exists
			account = accountService.findById(accountId);
			if(account == null) {
				throw new ValidationFailException("Account does not exist");
			}
			
			List<Appointment> appointments=appointmentService.findAllByAccountId(accountId);
			for(Appointment appointment:appointments){
				System.out.println("appointmentName="+appointment.getName()+"   appointmentLocation="+appointment.getLocation());
			}
			
			message.setSuccess(appointments);
		
		}catch(ValidationFailException ve) {
			message.setFail("404", ve.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			message.setFail("400", e.getMessage());
		}
		return message;
	
}
	
	@RequestMapping(value="/filterAppsByUsername",method = RequestMethod.GET
			,headers="Accept=application/json"
			)
	public HCMessage filterAppsByUsername(HttpServletRequest request, HttpServletResponse response,
			@RequestParam("accountId") long accountId,
			@RequestParam("roleId") long roleId,
			@RequestParam("username") String username){
		
		HCMessage message = new  HCMessage();
		try{
			ArrayList groupIdList = new ArrayList();
			groupIdList = memberService.getGroupIdOfNurse(accountId, roleId);
//			System.out.println(groupIdList.get(0)+"-------------------------------------------");
			ArrayList<String> patientName = new ArrayList<String>();
			
			if(groupIdList.size() < 2){
				throw new ValidationFailException("invalid input!");
			}
			
			if(roleId != 2){
				throw new ValidationFailException("only nurse can do this action!");
			}
			
			for(int i = 0; i < groupIdList.size(); i++){
				long groupId = (Long) groupIdList.get(i);
				String patientN = "";
				patientN = memberService.getPatientName(groupId);
				patientName.add(patientN);
			}
			
			if(patientName.contains(username)){
				long accIdOfPatient = accountService.getAccIdByUsername(username);
				List<Appointment> appointments=appointmentService.findAllByAccountId(accIdOfPatient);
				message.setSuccess(appointments);
			} else {
				throw new ValidationFailException("invalid username!");
			}
			
			
		
		}catch(ValidationFailException ve) {
			message.setFail("404", ve.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			message.setFail("400", e.getMessage());
		}
		return message;
	
	}
	
	

}
