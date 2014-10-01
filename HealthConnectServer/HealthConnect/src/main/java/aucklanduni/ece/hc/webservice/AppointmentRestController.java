package aucklanduni.ece.hc.webservice;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.fortuna.ical4j.model.Calendar;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import aucklanduni.ece.hc.repository.model.Account;
import aucklanduni.ece.hc.repository.model.Appointment;
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
	 * 
	 * @Title: createAppointment 
	 * @Description: Service will create appointment for the given accountId
	 * and save details of the appointment in the APPOINTMENT table.
	 * If the appointment is shared with the group, the sharedType column
	 * in the Appointment table will be set as 'G'.
	 * If the appointment is shared with only specific members of the group
	 * the sharedType column in the Appointment table will be set as 'M' and
	 * details of the member will be saved in APP_ACC_REF table.
	 *  Input member array is of the below format
	 *  [
	 *    { "id":"1"
	 *    },
	 *    { "id":"2"
	 *    },
	 *  ]
	 *  Input Appointment object is of the below format
	 *  {
	 *    "name": "Appointment",
	 *    "location": "Mercy Hospital",
	 *    "startTime": "09:00:00",
	 *    "endTime": "10:00:00",
	 *    "executeTime": 1,
	 *    "description": "Diabetes",
	 *    "startDate": "2014-09-29",
	 *    "endDate": "3014-09-29"
	 *  }
	 *  
	 * @param request
	 * @param response
	 * @param accountId 
	 * @param groupId
	 * @param members - optional 
	 * @param appointment
	 * @return HCMessage
	 * @throws
	 */
	@RequestMapping(value="/createAppointment",method = RequestMethod.POST
			,headers="Accept=application/json"
			)
	public HCMessage createAppointment(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value="accountId") long accountId
			,@RequestParam(value="groupId") long groupId
			,@RequestParam(value="members",required=false) String members
			,@RequestBody Appointment appointment) {

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
			
			List<Dictionary> roles = new ArrayList<Dictionary>();
			roles = roleService.getRolesByGroupIdAccId(accountId, groupId);

			// if the input accountId and GroupId does not exist in database
			if(roles== null || roles.size() < 1) {
				throw new ValidationFailException("Invalid Input");
			}
			
			// check if the current user is a nurse or patient
			if( (roles.get(0).getValue().compareTo("S")==0 ) ) {
				throw new ValidationFailException("Support members are not allowed to create an appointment");
			}
			
			if(members==null) {
				appointmentService.createGroupAppointment(groupId, appointment);
			} else {
				appointmentService.createMemberAppointment(accountId, groupId, members, appointment);
			}
			
			notifyService.notify(accountId, "Appointment created successfully", "email");
			
			message.setSuccess();
		}
		catch(ValidationFailException ve) {
			message.setFail("404", ve.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			message.setFail("400", e.getMessage());
		}

		return message;

	}
	
	/**
	 * This method handle updateAppointment request, only nurse and patient can update appointment.
	 * @param request
	 * @param response
	 * @param accountId
	 * @param groupId
	 * @param appointmentName
	 * @param appointmentLocation
	 * @param isShare
	 * @return
	 */
	@RequestMapping(value="/updateAppointment",method = RequestMethod.POST
			,headers="Accept=application/json"
			)
	public HCMessage updateAppointment(HttpServletRequest request, HttpServletResponse response
			,@RequestParam(value="accountId") long accountId
			,@RequestParam(value="groupId") long groupId
//			,@RequestParam("appointmentTimeNew") Date appointmentTimeNew
			,@RequestParam("appointmentId") long appointmentId
			,@RequestParam("appointmentLocationNew") String appointmentLocationNew
			) {
		HCMessage message = new  HCMessage();
		try {
			Appointment appointment = null;
			// check if given appointment exists
			appointment = appointmentService.findById(appointmentId);
			if(appointment == null){
				throw new ValidationFailException("Appointment does not exist");
			}
			
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
				throw new ValidationFailException("Supportive members are not allowed to update an appointment");
			}
			
			//appointment.setTime(new Date());
			appointment.setUpdatedDate(new Date());
			appointment.setLocation(appointmentLocationNew);
			appointmentService.update(appointment);
			
			message.setSuccess(appointment);
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
			//System.out.println("These are the appointments that you created");
			List<Appointment> appointments=appointmentService.findAllByAccountId(accountId);
			for(Appointment appointment:appointments){
				System.out.println("appointmentName="+appointment.getName()+"   appointmentLocation="+appointment.getLocation());
			}
			//System.out.println("These are the appointments that being shared in your group");	
			//List<Appointment> appointments2=appointmentService.findAllByGroupShared(accountId);
			//for(Appointment appointment2:appointments2){
				//log.debug("appointmentName="+appointment2.getName()+"   appointmentLocation="+appointment2.getLocation());
			//}
			message.setSuccess(appointments);
			//message.setSuccess(appointments2);
		
		}catch(ValidationFailException ve) {
			message.setFail("404", ve.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			message.setFail("400", e.getMessage());
		}
		return message;
	
}
	/*
	 * /**
		 * 
		 * @Title: viewAppointmentsBuGroup 
		 * @Description: Service will return all the appointments of the given
		 * accountId and groupId.
		 *  
		 * @param request
		 * @param response
		 * @param accountId 
		 * @return HCMessage
		 * @throws
		 */
		/*
		@RequestMapping(value="/viewAppointmentByGroup",method = RequestMethod.GET
				,headers="Accept=application/json"
				)
		public HCMessage showAppointmentsByGroup(HttpServletRequest request, HttpServletResponse response,
				@RequestParam("accountId") long accountId,
				@RequestParam(value="groupId") long groupId){
			System.out.println(">>>>>>>>>>>>>>>>>viewAppointment"+accountId+"by"+groupId);
			HCMessage message = new  HCMessage();
			try{
				Account account = null;
				// check if given accountId exists
				account = accountService.findById(accountId);
				if(account == null) {
					throw new ValidationFailException("Account does not exist");
				}
				
				Group group=null;
				// check if given group exists
				group=groupService.findById(groupId);
				if(group==null){
					throw new ValidationFailException("such group does not exist");
				}
				
				//System.out.println("These are the appointments that you created");
				List<Appointment> appointments=appointmentService.findAllByGroupId(accountId,groupId);
				for(Appointment appointment:appointments){
					System.out.println("appointmentName="+appointment.getName()+"   appointmentLocation="+appointment.getLocation());
				}
				//System.out.println("These are the appointments that being shared in your group");	
				//List<Appointment> appointments2=appointmentService.findAllByGroupShared(accountId);
				//for(Appointment appointment2:appointments2){
					//log.debug("appointmentName="+appointment2.getName()+"   appointmentLocation="+appointment2.getLocation());
				//}
				message.setSuccess(appointments);
			}catch(ValidationFailException ve) {
				message.setFail("404", ve.getMessage());
			} catch (Exception e) {
				e.printStackTrace();
				message.setFail("400", e.getMessage());
			}
			return message;
		
	}
	*/
	
	/**
	 * 
	 * @Title: filterAppsByUsername
	 * @author Yalu You,Pengyi Li
	 * @Description: Service will filter appointments by patient's name.
	 * on business validation
	 * 1. Only nurse can filter appointments.
	 * 2. Nurse cannot filter appointments when he/she does not have a group as a nurse.
	 * 3. Nurse can only filter patient's appointments when the patient in one of the nurse's groups.
	 *  
	 * @param request
	 * @param response
	 * @param accountId - account in the group
	 * @param roleId - role in the group
	 * @param username - the person whose appointments will be seen
	 * @return HCMessage
	 * @throws
	 */
	
	@RequestMapping(value="/filterAppsByUsername",method = RequestMethod.POST
			,headers="Accept=application/json"
			)
	public HCMessage filterAppsByUsername(HttpServletRequest request, HttpServletResponse response,
			@RequestParam("accountId") long accountId,
			@RequestParam("roleId") long roleId,
			@RequestParam("username") String username){
		
		HCMessage message = new  HCMessage();
		try{
			
			if(roleId != 2){
				throw new ValidationFailException("only nurse can do this action!");
			}
			ArrayList groupIdList = new ArrayList();//get
			groupIdList = memberService.getGroupIdOfNurse(accountId, roleId);
//			System.out.println(groupIdList.get(0)+"-------------------------------------------");
			ArrayList<String> patientName = new ArrayList<String>();
			
			if(groupIdList.size() == 0){
				throw new ValidationFailException("invalid input!");
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
	
	/**
	 * 
	 * @Title: filterAppsByDate
	 * @author Yalu You,Pengyi Li
	 * @Description: Service will filter appointments by appointments' create date.
	 * on business validation
	 * 1. Only nurse can filter appointments.
	 * 2. Nurse cannot filter appointments when he/she does have a group as a nurse.
	 * 3. Nurse can only filter patient's appointments when the patient in one of the nurse's groups.
	 *  
	 * @param request
	 * @param response
	 * @param accountId - account in the group
	 * @param roleId - role in the group
	 * @param username - the person whose appointments will be seen
	 * @param startDate - the date before create date
	 * @param endDate - the date after create  date
	 * @return HCMessage
	 * @throws
	 */
	@RequestMapping(value="/filterAppsByDate",method = RequestMethod.POST
			,headers="Accept=application/json"
			)
	public HCMessage filterAppsByDate(HttpServletRequest request, HttpServletResponse response,
			@RequestParam("accountId") long accountId,
			@RequestParam("roleId") long roleId,
			@RequestParam("username") String username,
			@RequestParam("startDate") @DateTimeFormat(pattern="yyyy-MM-dd")  Date startDate,
			@RequestParam("endDate") @DateTimeFormat(pattern="yyyy-MM-dd")  Date endDate){
		
		HCMessage message = new  HCMessage();
		try{
			
			if(roleId != 2){
				System.out.println("222222222222");
				throw new ValidationFailException("only nurse can do this action!");
			}
			
			ArrayList groupIdList = new ArrayList();
			groupIdList = memberService.getGroupIdOfNurse(accountId, roleId);
//			System.out.println(groupIdList.get(0)+"-------------------------------------------");
			ArrayList<String> patientName = new ArrayList<String>();
			
			if(groupIdList.size() == 0){
				throw new ValidationFailException("invalid input!");
			}
			
			
			
			for(int i = 0; i < groupIdList.size(); i++){
				long groupId = (Long) groupIdList.get(i);
				String patientN = "";
				patientN = memberService.getPatientName(groupId);
				patientName.add(patientN);
			}
			
			if(patientName.contains(username)){
				long accIdOfPatient = accountService.getAccIdByUsername(username);
				List<Appointment> appointments=appointmentService.filterByDate(accIdOfPatient, startDate, endDate);
				message.setSuccess(appointments);
			} else {
				throw new ValidationFailException("invalid username!");
			}
			
			
			
		
		}catch(ValidationFailException ve) {
			System.out.println(ve.getMessage());
			message.setFail("404", ve.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			message.setFail("400", e.getMessage());
		}
		return message;
	
	}
	
	@RequestMapping(value="/filterAppByAccountId",method = RequestMethod.GET
			,headers="Accept=application/json"
			)
	public HCMessage filterAppByAccountId(HttpServletRequest request, HttpServletResponse response,
			@RequestParam("accountId") long accountId,
			@RequestParam(value="memberId",required=false) Long memberId,
			@RequestParam(value="groupId",required=false) Long groupId,
			@RequestParam(value="startDate",required=false) @DateTimeFormat(pattern="yyyy-MM-dd")  Date startDate,
			@RequestParam(value="endDate",required=false) @DateTimeFormat(pattern="yyyy-MM-dd")  Date endDate){
		HCMessage message = new  HCMessage();
		try{
			List<Long> groupIds = new ArrayList<Long>();
			List<Appointment> appoints = new ArrayList<Appointment>();
			
			Account account = null;
			// check if given accountId exists
			account = accountService.findById(accountId);
			if(account == null) {
				throw new ValidationFailException("Account does not exist");
			}
			
			if(memberId != null) {
				account = accountService.findById(memberId.longValue());
				if(account == null) {
					throw new ValidationFailException("Member does not exist");
				}
				List<Group> group = null;
				group = groupService.findCommonGroup(accountId, memberId.longValue());
				
				if(group == null || group.size() < 1)
					throw new ValidationFailException("Invalid Input");
				
				
				for(Group g: group){
					groupIds.add(g.getId());
				}

				appoints = appointmentService.findAppByGroupIdMemberId(groupIds, memberId.longValue());
			} else if(groupId != null) {
				Group group = null;
				//check if given groupId exists
				group = groupService.findById(groupId.longValue());
				if(group == null){
					throw new ValidationFailException("Group does not exist");
				}
				
				List<Member> memberDtls = new ArrayList<Member>();
				memberDtls = memberService.findByHql("from Member m WHERE "
						+ "m.accountId=" + accountId 
						+ " and m.groupId=" + groupId.longValue());
				if(memberDtls == null || memberDtls.size() < 1)
					throw new ValidationFailException("Input AccountId does not belong to the Group");
				
				groupIds.add(groupId.longValue());
			} else if(startDate !=null && endDate!=null) {
				List<Group> groupList = new ArrayList<Group>();
				groupList = groupService.getGroupByAccId(accountId);
				
				for(Group g: groupList){
					groupIds.add(g.getId());
				}

				appoints = appointmentService.findAppByGroupIdMemberId(groupIds, accountId);

			}
			
			List<Appointment> appointments = new ArrayList<Appointment>();
			appointments = appointmentService.findAppointmentByGroupId(groupIds);
			
			if(appoints!=null) 
				appointments.addAll(appoints);
			
			if(startDate !=null && endDate!=null) {
				appointments = appointmentService.findAppByDate(appointments, startDate, endDate);
			}
			
			System.out.println(appointments);
			message.setSuccess(appointments);
			
		}
		catch(ValidationFailException ve) {
			message.setFail("404", ve.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			message.setFail("400", e.getMessage());
		}
		return message;
	}

}
