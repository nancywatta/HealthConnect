package aucklanduni.ece.hc.service.impl;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import aucklanduni.ece.hc.repository.dao.AccountDao;
import aucklanduni.ece.hc.repository.dao.AppointmentAccountRefDao;
import aucklanduni.ece.hc.repository.dao.AppointmentDao;
import aucklanduni.ece.hc.repository.dao.MemberDao;
import aucklanduni.ece.hc.repository.dao.impl.AppointmentDaoImpl;
import aucklanduni.ece.hc.repository.model.Account;
import aucklanduni.ece.hc.repository.model.Appointment;
import aucklanduni.ece.hc.repository.model.AppointmentAccountRef;
import aucklanduni.ece.hc.repository.model.Database;
import aucklanduni.ece.hc.repository.model.Member;
import aucklanduni.ece.hc.service.AppointmentService;
import aucklanduni.ece.hc.webservice.model.ValidationFailException;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

@Service
public class AppointmentServiceImpl  extends BaseServiceImpl<Appointment> implements AppointmentService{
	@Autowired
	private AppointmentDao appointmentDao;
	@Autowired
	private AppointmentAccountRefDao appointRefDao;
	@Autowired
	private AccountDao accountDao;
	@Autowired
	private MemberDao memberDao;
	
	/*public Appointment findByName(String appointmentName) {
		return appointmentDao.findByName(appointmentName);
	}
	public Map<String, ArrayList<Group>> GetGroups(long accountId)throws Exception {
    Map<String, ArrayList<Group>> groups = null;
	 */
	public Map<String, ArrayList<Appointment>> showAllAppointment(long accountId) throws Exception{
		Map<String, ArrayList<Appointment>> appointments = null;
		try{
			Database database = new Database();
			Connection connection = database.Get_Connection();
			AppointmentDaoImpl appointmentDao = new AppointmentDaoImpl();
			appointments = appointmentDao.showAllAppointment(connection, accountId);
		}
		catch (Exception e){
			throw e;
		}
		return appointments;

	}
	//Chengchen 12/09/14
	public void createNewAppointment(long accountId, Date appointmentTime,
			String appointmentName, String appointmentLocation) throws Exception {

		try{
			//save appointment in APPOINTMENT table
			Appointment newAppointment = new Appointment();
			newAppointment.setName(appointmentName);
			//newAppointment.setTime(appointmentTime);
			newAppointment.setLocation(appointmentLocation);
			Account account = accountDao.findById(accountId);
			createAppointment(newAppointment,account,appointmentName,appointmentTime,appointmentLocation);



		}catch (ValidationFailException ve) {
			throw ve;
		}catch (Exception e) {
			throw e;
		}
	}

	private void createAppointment(Appointment appointment, Account account,String appointmentName, Date appointmentTime,
			String appointmentLocation) throws Exception {
		// TODO Auto-generated method stub
		appointmentDao.add(appointment);


	}
	
	public void setAppointmentGroupShare(long appointmtId) throws Exception {
		Appointment appointment = appointmentDao.findById(appointmtId);
		appointment.setSharedType("G");
		appointmentDao.update(appointment);


	}
	
	public void setAppointmentMemberShare(long accountId,long groupId,long appointmtId,String members) throws Exception {
		Appointment appointment = appointmentDao.findById(appointmtId);
		appointmentDao.expireSharedMembersByAppointmtId(appointmtId);
		appointment.setSharedType("M");
		appointmentDao.update(appointment);
		
		List<Account> accList = 
				new Gson().fromJson(members, new TypeToken<List<Account>>() {}.getType());

		for(Account  member : accList) {
			
			// Check if the MemberId is part of the Input GroupId
			List<Member> memberDtls = new ArrayList<Member>();
			memberDtls = memberDao.findByHql("from Member m WHERE "
					+ "m.accountId=" + member.getId() 
					+ " and m.groupId=" + groupId);
			if(memberDtls == null || memberDtls.size() < 1)
				throw new ValidationFailException("Incorrect Member ID");
			
			// add row in APP_ACC_REF table for the member
			AppointmentAccountRef aaf=new AppointmentAccountRef();
			aaf.setAccountId(member.getId());
			aaf.setAppointmentId(appointmtId);
			aaf.setGroupId(groupId);
			appointRefDao.add(aaf);
		}
		
		//add row in APP_ACC_REF table for the sharer of the appointment
		AppointmentAccountRef sharer=new AppointmentAccountRef();
		sharer.setAccountId(accountId);
		sharer.setAppointmentId(appointmtId);
		sharer.setGroupId(groupId);
		appointRefDao.add(sharer);


	}
	
	public List<Appointment> findAllByAccountId(long accountId) throws Exception{
		return appointmentDao.findAllByAccountId(accountId);
	}

	//public List<Appointment> findAllByGroupShared(long accountId)throws Exception{
	//return appointmentDao.findAllByGroupShared(accountId);
	//}
	public List<Appointment> findAllByGroupId(long accountId, long groupId)throws Exception{
		return appointmentDao.findAllByGroupId(accountId,groupId);
	}
	
	//Yalu
	public List<Appointment> filterByUsername(String username) throws Exception {

		try {
			Database database= new Database();
			Connection connection = database.Get_Connection();

			long accountId = accountDao.getAccIdByUserName(connection, username);

			return appointmentDao.findAllByAccountId(accountId);
		}
		catch (Exception e) {
			throw e;
		}

	}

	public List<Appointment> filterByDate(long accountId, Date startDate, Date endDate) throws Exception{

		try {
			Database database= new Database();
			Connection connection = database.Get_Connection();
			return appointmentDao.filterByDate(connection,accountId, startDate, endDate);
		}
		catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Function will save appointment shared with the input group.
	 * Shared_Type column will be set as 'G' in the APPOINTMENT table.
	 */
	public void createGroupAppointment(long groupId, Appointment appointment) throws Exception {
		try {

			Appointment appoint = new Appointment();
			appoint.setCreateDate(new Date());
			appoint.setSharedType("G");
			appoint.setGroupId(groupId);
			appoint.setAppointmentType(appointment.getAppointmentType());
			appoint.setDescription(appointment.getDescription());
			appoint.setEndDate(appointment.getEndDate());
			appoint.setExecuteTime(appointment.getExecuteTime());
			appoint.setLocation(appointment.getLocation());
			appoint.setName(appointment.getName());
			appoint.setStartDate(appointment.getStartDate());
			appoint.setEndTime(appointment.getEndTime());
			appoint.setStartTime(appointment.getStartTime());
			appoint.setStatus("A");

			appointmentDao.add(appoint);

		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Function will save appointment shared with the specific members of
	 * the input group.
	 * Shared_Type column will be set as 'M' in the APPOINTMENT table.
	 * and entry will be saved in APP_ACC_REF table for every member
	 * with whom the appointment is shared.
	 */
	public void createMemberAppointment(long accountId, long groupId, 
			String members, Appointment appointment) throws ValidationFailException, Exception {
		try {

			Appointment appoint = new Appointment();
			appoint.setCreateDate(new Date());
			appoint.setSharedType("M");
			appoint.setGroupId(groupId);
			appoint.setAppointmentType(appointment.getAppointmentType());
			appoint.setDescription(appointment.getDescription());
			appoint.setEndDate(appointment.getEndDate());
			appoint.setExecuteTime(appointment.getExecuteTime());
			appoint.setLocation(appointment.getLocation());
			appoint.setName(appointment.getName());
			appoint.setStartDate(appointment.getStartDate());
			appoint.setEndTime(appointment.getEndTime());
			appoint.setStartTime(appointment.getStartTime());
			appoint.setStatus("A");

			appointmentDao.add(appoint);

			List<Account> accList = 
					new Gson().fromJson(members, new TypeToken<List<Account>>() {}.getType());

			for(Account  member : accList) {
				
				// Check if the MemberId is part of the Inout GroupId
				List<Member> memberDtls = new ArrayList<Member>();
				memberDtls = memberDao.findByHql("from Member m WHERE "
						+ "m.accountId=" + member.getId() 
						+ " and m.groupId=" + groupId);
				if(memberDtls == null || memberDtls.size() < 1)
					throw new ValidationFailException("Incorrect Member ID");
				
				// add row in APP_ACC_REF table for the member
				AppointmentAccountRef aaf=new AppointmentAccountRef();
				aaf.setAccountId(member.getId());
				aaf.setAppointmentId(appoint.getId());
				aaf.setGroupId(groupId);
				appointRefDao.add(aaf);
			}
			
			//add row in APP_ACC_REF table for the creater of the appointment
			AppointmentAccountRef creater=new AppointmentAccountRef();
			creater.setAccountId(accountId);
			creater.setAppointmentId(appoint.getId());
			creater.setGroupId(groupId);
			appointRefDao.add(creater);

		} catch (Exception e) {
			throw e;
		}
	}

	public List<Appointment> findAppointmentsByGroup(long groupId) throws Exception {
		return appointmentDao.findAppointmentsByGroup(groupId);
	}
	
	/**
	 * Function will return all appointments that are shared with input groupList
	 */
	public List<Appointment> findAppointmentByGroupId(List<Long> groupId) throws Exception {
		try {

			return appointmentDao.findAppointmentByGroupId(groupId);

		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Function will return all appointments that are shared with input memberId and accountId both
	 * within the input groupList 
	 */
	public List<Appointment> findAppByGroupIdMemberId(List<Long> groupId, long memberId,long accountId) throws Exception {
		try {

			return appointmentDao.findAppByGroupIdMemberId(groupId, memberId,accountId);

		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Function will filter input appointments on the basis of input date
	 */
	public List<Appointment> findAppByDate(List<Appointment> appointments, 
			Date startDate, Date endDate) throws Exception {
		try {

			return appointmentDao.findAppByDate(appointments, startDate, endDate);

		} catch (Exception e) {
			throw e;
		}
	}
	
	/**
	 * Function will return all appointments that are shared with or owned to the input accountId
	 * within the input groupList 
	 */
	public List<Appointment> findAppByGroupIdAccountId(List<Long> groupId, long accountId) throws Exception {
		try {

			return appointmentDao.findAppByGroupIdAccountId(groupId, accountId);

		} catch (Exception e) {
			throw e;
		}

	}
	
	public boolean checkAppointmtShared(long accountId, long appointmtId) throws Exception {
		try {
			List<AppointmentAccountRef> refDtls = new ArrayList<AppointmentAccountRef>();
			refDtls = appointRefDao.findByHql("from AppointmentAccountRef ref WHERE "
						+ "ref.accountId=" + accountId 
						+ " and ref.appointmentId=" + appointmtId
						+ " and ref.expirationDate= null"
						);
			if(refDtls == null || refDtls.size() < 1)
				return false;
			else
				return true;

		} catch (Exception e) {
			throw e;
		}
	}
}


