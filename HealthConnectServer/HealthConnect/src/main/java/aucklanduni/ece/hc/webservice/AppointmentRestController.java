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

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

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
	private AppointmentAccountRefService aarService;
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
	 *    }
	 *  ]
	 *  Input Appointment object is of the below format
	 *  {
	 *    "name": "Appointment",
	 *    "location": "Mercy Hospital",
	 *    "startTime": "09:00:00",
	 *    "endTime": "10:00:00",
	 *    "executeTime": 1,
	 *    "description": "Diabetes",
	 *    "startDate": "2014-09-29 00:00:00",
	 *    "endDate": "3014-09-29 00:00:00"
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
	 * 
	 * @Title: shareAppointment 
	 * @Description: Service will share the given appointment with either the
	 * entire group or with specific members based on input received.
	 * If the appointment is shared with the group, the sharedType column
	 * in the Appointment table will be set as 'G'.
	 * If the appointment is shared with only specific members of the group,
	 * the sharedType column in the Appointment table will be set as 'M' and
	 * details of the member will be saved in APP_ACC_REF table.
	 *  Input member array is of the below format
	 *  [
	 *    { "id":"1"
	 *    },
	 *    { "id":"2"
	 *    }
	 *  ]
	 *  	
	 * @param request
	 * @param response
	 * @param accountId 
	 * @param appointmtId
	 * @param members - optional 
	 * @return HCMessage
	 * @throws
	 */
	@RequestMapping(value="/shareAppointment",method = RequestMethod.POST
			,headers="Accept=application/json"
			)
	public HCMessage shareAppointment(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value="accountId") long accountId
			,@RequestParam(value="appointmtId") long appointmtId
			,@RequestParam(value="members",required=false) String members
			){

		HCMessage message = new  HCMessage();
		try {

			Account account = null;
			// check if given accountId exists
			account = accountService.findById(accountId);
			if(account == null) {
				throw new ValidationFailException("Account does not exist");
			}

			//check if given appointmtId exists
			Appointment appointmt = null;
			appointmt = appointmentService.findById(appointmtId);
			if(appointmt == null){
				throw new ValidationFailException("Appointment does not exist");
			} else if(appointmt.getExpirationDate()!=null)
				throw new ValidationFailException("Appointment is Expired");

			long groupId = appointmt.getGroupId();
			List<Dictionary> roles = new ArrayList<Dictionary>();
			roles = roleService.getRolesByGroupIdAccId(accountId, groupId);

			// if the input accountId and GroupId does not exist in database
			if(roles== null || roles.size() < 1) {
				throw new ValidationFailException("Invalid Input");
			}

			// check if the current user is a support member
			if( (roles.get(0).getValue().compareTo("S")==0 ) ) {
				throw new ValidationFailException("Support members are not allowed to share appointment");
			}

			// check if the appointment is shared with the current user
			if(appointmt.getSharedType().compareTo("M")==0){
				if(!aarService.checkAppointmtShared(accountId, appointmtId)){
					throw new ValidationFailException("The appointment is not shared with this user");
				}
			}

			// when member is null then, appointment will be shared with entire group 
			if(members==null){
				/* if the appointment is shared with specific members, then expire the entry
				 * in APP_ACC_REF table for the given appointment id and accountId and set
				 * the SHARED_TYPE as 'G' in APPOINTMENT table to share with entire group.
				 */
				if(appointmt.getSharedType().compareTo("M")==0) {
					List<Account> currentSharedAccList = accountService.getAccbyAppointmentId(appointmtId);
					aarService.expireAppointmtSharedState(appointmtId, currentSharedAccList);
				}
				appointmentService.setAppointmentGroupShare(appointmtId);
			}else {

				List<Account> sharedAccList = 
						new Gson().fromJson(members, new TypeToken<List<Account>>() {}.getType());

				// Check if the MemberId is part of the Group associated with the appointment
				for(Account  tempAccount : sharedAccList) {
					Member member = null;
					member = memberService.findByAccountIdAndGroupId(tempAccount.getId(), groupId);
					if(member == null)
						throw new ValidationFailException("Incorrect Member ID");
				}	

				sharedAccList.add(account);
				
				// Set SHARED_TYPE as 'M' in APPOINTMENT table to share with specific members of group.
				appointmentService.setAppointmentMemberShare(appointmtId);
				
				// retrieve all accounts with which the appointment is shared.
				List<Account> currentSharedAccList = accountService.getAccbyAppointmentId(appointmtId);

				List<Account> addList = new ArrayList<Account>();
				for(Account tempAccount0:sharedAccList){
					boolean flag = true;
					for(Account tempAccount1:currentSharedAccList){
						if(tempAccount0.getId()==tempAccount1.getId())
						{
							flag = false;
							break;
						}
					}
					if(flag){
						addList.add(tempAccount0);
					}
				}

				List<Account> removeList = new ArrayList<Account>();
				for(Account tempAccount0:currentSharedAccList){
					boolean flag = true;
					for(Account tempAccount1:sharedAccList){
						if(tempAccount0.getId()==tempAccount1.getId())
						{
							flag = false;
							break;
						}
					}
					if(flag){
						removeList.add(tempAccount0);
					}
				}

				/* expire entry from APP_ACC_REf table for account with whom, 
				 * user no longer wants to share appointment.
				 */
				aarService.expireAppointmtSharedState(appointmtId, removeList);
				
				/* insert record in APP_ACC_REf table for account with whom, 
				 * user wants to share appointment.
				 */
				aarService.addAppointmtShared(appointmtId,addList,groupId);

			}

			notifyService.notify(accountId, "Appointment is shared successfully", "email");

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
	 * 
	 * @Title: updateAppointment 
	 * @Description: Service will update appointment details
	 * for the given appointment.
	 * Only Nurse and Patient are allowed to update appointment.
	 * If the input expiration date in the appointment object is 
	 * populated, the service will expire the appointment in the 
	 * APPOINTMENT table and APP_ACC_REF table if applicable.
	 * 
	 *  Input Appointment object is of the below format
	 *  {
	 *    "id": 1,
	 *    "name": "Appointment",
	 *    "location": "Mercy Hospital",
	 *    "startTime": "09:00:00",
	 *    "endTime": "10:00:00",
	 *    "executeTime": 1,
	 *    "description": "Diabetes",
	 *    "startDate": "2014-09-29 00:00:00",
	 *    "endDate": "3014-09-29 00:00:00",
	 *    "expirationDate": "2014-09-29 00:00:00"
	 *  }
	 *  
	 * @param request
	 * @param response
	 * @param accountId 
	 * @param appointment
	 * @return HCMessage
	 * @throws
	 */
	@RequestMapping(value="/updateAppointment",method = RequestMethod.POST
			,headers="Accept=application/json"
			)
	public HCMessage updateAppointment(HttpServletRequest request, HttpServletResponse response
			,@RequestParam(value="accountId") long accountId
			,@RequestBody Appointment appointment) 
	{
		HCMessage message = new  HCMessage();
		try {
			Appointment app = null;
			// check if given appointment exists
			app = appointmentService.findById(appointment.getId());
			if(app == null){
				throw new ValidationFailException("Appointment does not exist");
			} else if(app.getExpirationDate()!=null)
				throw new ValidationFailException("Appointment is Expired");

			Account account = null;
			// check if given accountId exists
			account = accountService.findById(accountId);
			if(account == null) {
				throw new ValidationFailException("Account does not exist");
			}

			List<Dictionary> role = null;
			role = roleService.getRolesByGroupIdAccId(accountId,app.getGroupId());
			// check if the current user is a nurse or patient
			if(role == null || role.size() < 1) 
				throw new ValidationFailException("Invalid Input");

			if( role.get(0).getValue().compareTo("S") == 0 ) {
				throw new ValidationFailException("Support members are not allowed to update an appointment");
			}

			if(appointment.getAppointmentType() != null)
				app.setAppointmentType(appointment.getAppointmentType());
			if(appointment.getDescription() != null)
				app.setDescription(appointment.getDescription());
			if(appointment.getEndTime() != null)
				app.setEndTime(appointment.getEndTime());
			if(appointment.getEndDate() != null)
				app.setEndDate(appointment.getEndDate());
			if(appointment.getExpirationDate() != null)
				app.setExpirationDate(new Date());
			if(appointment.getLocation() != null)
				app.setLocation(appointment.getLocation());
			if(appointment.getExecuteTime() != 0)
				app.setExecuteTime(appointment.getExecuteTime());
			if(appointment.getName() != null)
				app.setName(appointment.getName());
			if(appointment.getStartDate() != null)
				app.setStartDate(appointment.getStartDate());
			if(appointment.getStartTime()!=null)
				app.setStartTime(appointment.getStartTime());

			// set updated date as today's date in the APPOINTMENT table
			app.setUpdatedDate(new Date());

			appointmentService.update(app);

			/* if the input expiration date is not null and the appointment is shared with
			 * specific members, then record in the APP_ACC_REF table should also be expired.
			 */
			if(appointment.getExpirationDate() != null 
					&& app.getSharedType().compareTo("M") == 0) {
				List<AppointmentAccountRef> aarList = null;
				aarList = aarService.findByAppointmentId(appointment.getId());

				for(AppointmentAccountRef aarEntry: aarList) {
					aarEntry.setExpirationDate(new Date());
					aarService.update(aarEntry);
				}
			}

			message.setSuccess(app);
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
	 * @Title: viewAppointment 
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

		List<Appointment> appointmentListShown = new ArrayList<Appointment>();
		HCMessage message = new  HCMessage();
		try{
			Account account = null;
			// check if given accountId exists
			account = accountService.findById(accountId);
			if(account == null) {
				throw new ValidationFailException("Account does not exist");
			}
			// find all groups that this account has
			List<Group> groups=groupService.getGroupByAccId(accountId);
			for(Group group:groups){
				List<Appointment> appointments = appointmentService.
						findAppointmentsByGroup(group.getId());
				for(Appointment appointment:appointments){
					if(appointment.getSharedType().equals("M")){
						if(aarService.ifExist(accountId,appointment.getId())!=null)
							appointmentListShown.add(appointment);
					}
					else{
						appointmentListShown.add(appointment);
					}
				}
			}
			//System.out.println("These are the appointments that you created");
			for(Appointment appointment:appointmentListShown){
				System.out.println("appointmentName="+appointment.getName()+"   appointmentLocation="+appointment.getLocation()
						+"   startDate="+appointment.getStartDate());
			}

			message.setSuccess(appointmentListShown);
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

	//	@RequestMapping(value="/filterAppsByUsername",method = RequestMethod.POST
	//			,headers="Accept=application/json"
	//			)
	//	public HCMessage filterAppsByUsername(HttpServletRequest request, HttpServletResponse response,
	//			@RequestParam("accountId") long accountId,
	//			@RequestParam("roleId") long roleId,
	//			@RequestParam("username") String username){
	//		
	//		HCMessage message = new  HCMessage();
	//		try{
	//			
	//			if(roleId != 2){
	//				throw new ValidationFailException("only nurse can do this action!");
	//			}
	//			ArrayList groupIdList = new ArrayList();//get
	//			groupIdList = memberService.getGroupIdOfNurse(accountId, roleId);
	////			System.out.println(groupIdList.get(0)+"-------------------------------------------");
	//			ArrayList<String> patientName = new ArrayList<String>();
	//			
	//			if(groupIdList.size() == 0){
	//				throw new ValidationFailException("invalid input!");
	//			}
	//			
	//			
	//			
	//			for(int i = 0; i < groupIdList.size(); i++){
	//				long groupId = (Long) groupIdList.get(i);
	//				String patientN = "";
	//				patientN = memberService.getPatientName(groupId);
	//				patientName.add(patientN);
	//			}
	//			
	//			if(patientName.contains(username)){
	//				long accIdOfPatient = accountService.getAccIdByUsername(username);
	//				List<Appointment> appointments=appointmentService.findAllByAccountId(accIdOfPatient);
	//				message.setSuccess(appointments);
	//			} else {
	//				throw new ValidationFailException("invalid username!");
	//			}
	//			
	//			
	//		
	//		}catch(ValidationFailException ve) {
	//			message.setFail("404", ve.getMessage());
	//		} catch (Exception e) {
	//			e.printStackTrace();
	//			message.setFail("400", e.getMessage());
	//		}
	//		return message;
	//	
	//	}
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
	//	@RequestMapping(value="/filterAppsByDate",method = RequestMethod.POST
	//			,headers="Accept=application/json"
	//			)
	//	public HCMessage filterAppsByDate(HttpServletRequest request, HttpServletResponse response,
	//			@RequestParam("accountId") long accountId,
	//			@RequestParam("roleId") long roleId,
	//			@RequestParam("username") String username,
	//			@RequestParam("startDate") @DateTimeFormat(pattern="yyyy-MM-dd")  Date startDate,
	//			@RequestParam("endDate") @DateTimeFormat(pattern="yyyy-MM-dd")  Date endDate){
	//		
	//		HCMessage message = new  HCMessage();
	//		try{
	//			
	//			if(roleId != 2){
	//				System.out.println("222222222222");
	//				throw new ValidationFailException("only nurse can do this action!");
	//			}
	//			
	//			ArrayList groupIdList = new ArrayList();
	//			groupIdList = memberService.getGroupIdOfNurse(accountId, roleId);
	////			System.out.println(groupIdList.get(0)+"-------------------------------------------");
	//			ArrayList<String> patientName = new ArrayList<String>();
	//			
	//			if(groupIdList.size() == 0){
	//				throw new ValidationFailException("invalid input!");
	//			}
	//			
	//			
	//			
	//			for(int i = 0; i < groupIdList.size(); i++){
	//				long groupId = (Long) groupIdList.get(i);
	//				String patientN = "";
	//				patientN = memberService.getPatientName(groupId);
	//				patientName.add(patientN);
	//			}
	//			
	//			if(patientName.contains(username)){
	//				long accIdOfPatient = accountService.getAccIdByUsername(username);
	//				List<Appointment> appointments=appointmentService.filterByDate(accIdOfPatient, startDate, endDate);
	//				message.setSuccess(appointments);
	//			} else {
	//				throw new ValidationFailException("invalid username!");
	//			}
	//			
	//			
	//			
	//		
	//		}catch(ValidationFailException ve) {
	//			System.out.println(ve.getMessage());
	//			message.setFail("404", ve.getMessage());
	//		} catch (Exception e) {
	//			e.printStackTrace();
	//			message.setFail("400", e.getMessage());
	//		}
	//		return message;
	//	
	//	}

	/**
	 * 
	 * @Title: filterAppointment
	 * @author Yalu You,Pengyi Li
	 * @Description: Service will filter appointments based on given inputs.
	 *  
	 * @param request
	 * @param response
	 * @param accountId - account id of the user who wants to view the appointment
	 * @param memberId - optional
	 *                   accountId of the person whose appointment user wants to view
	 * @param groupId - optional 
	 *                  user can view all appointments within a group which are shared to
	 *                   entire group or himself
	 * @param startDate - optional
	 *                  can be combined with either the memberId or groupId
	 *                  or user can view appointments by startDate and endDate
	 * @param endDate - optional
	 *                  mandatory if startDate provided in input
	 * @return HCMessage
	 * @throws
	 */
	@RequestMapping(value="/filterAppointment",method = RequestMethod.GET
			,headers="Accept=application/json"
			)
	public HCMessage filterAppointment(HttpServletRequest request, HttpServletResponse response,
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
				// check if given memberId exists
				account = accountService.findById(memberId.longValue());
				if(account == null) {
					throw new ValidationFailException("Member does not exist");
				}

				// find all groups in which the accountId and memberId are part of.
				List<Group> group = null;
				group = groupService.findCommonGroup(accountId, memberId.longValue());

				if(group == null || group.size() < 1)
					throw new ValidationFailException("Invalid Input");

				for(Group g: group){
					groupIds.add(g.getId());
				}

				// find all appointments shared with the memberId and accountId both within the retrieved groups 
				appoints = appointmentService.findAppByGroupIdMemberId(groupIds, memberId.longValue(),accountId );
			} else if(groupId != null) {
				Group group = null;
				//check if given groupId exists
				group = groupService.findById(groupId.longValue());
				if(group == null){
					throw new ValidationFailException("Group does not exist");
				}

				// check if the input accountId is member of the input GroupId
				List<Member> memberDtls = new ArrayList<Member>();
				memberDtls = memberService.findByHql("from Member m WHERE "
						+ "m.accountId=" + accountId 
						+ " and m.groupId=" + groupId.longValue());
				if(memberDtls == null || memberDtls.size() < 1)
					throw new ValidationFailException("Input AccountId does not belong to the Group");

				groupIds.add(groupId.longValue());
			} else {

				// find all the groups of the input accountId
				List<Group> groupList = new ArrayList<Group>();
				groupList = groupService.getGroupByAccId(accountId);

				for(Group g: groupList){
					groupIds.add(g.getId());
				}

				// find all appointments shared with the accountId within the retrieved groups
				appoints = appointmentService.findAppByGroupIdAccountId(groupIds, accountId);

			}

			// find all appointments shared with the entire group
			List<Appointment> appointments = new ArrayList<Appointment>();
			appointments = appointmentService.findAppointmentByGroupId(groupIds);

			if(appoints!=null) 
				appointments.addAll(appoints);

			// filter appointment on basis of input Date
			if(startDate !=null && endDate!=null) {
				appointments = appointmentService.findAppByDate(appointments, startDate, endDate);
			}

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
