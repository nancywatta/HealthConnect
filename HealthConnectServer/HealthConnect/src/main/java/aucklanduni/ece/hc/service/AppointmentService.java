package aucklanduni.ece.hc.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

import aucklanduni.ece.hc.repository.model.Appointment;
import aucklanduni.ece.hc.webservice.model.ValidationFailException;

@Transactional
public interface AppointmentService  extends BaseService<Appointment> {


	public Map<String, ArrayList<Appointment>> showAllAppointment(long accountId) throws Exception;
	//View the target appointments according to the account id.

	public void createNewAppointment(long accountId, Date appointmentTime,
			String appointmentName, String appointmentLocation) throws Exception;

	public List<Appointment> findAllByAccountId(long accountId) throws Exception; 

	//public List<Appointment> findAllByGroupShared(long accountId)throws Exception;

	//Yalu
	public List<Appointment> filterByUsername(String username) throws Exception;

	public List<Appointment> filterByDate(long accountId, Date startDate, Date endDate) throws Exception;

	public List<Appointment> findAllByGroupId(long accountId, long groupId)throws Exception;

	public void createGroupAppointment(long groupId, Appointment appointment) throws Exception;

	public void createMemberAppointment(long accountId, long groupId, 
			String members, Appointment appointment) throws ValidationFailException, Exception;

	public List<Appointment> findAppointmentsByGroup(long groupId) throws Exception;

	public List<Appointment> findAppointmentByGroupId(List<Long> groupId) throws Exception;

	public List<Appointment> findAppByGroupIdMemberId(List<Long> groupId, long memberId, long accountId) throws Exception;
		
	public List<Appointment> findAppByGroupIdAccountId(List<Long> groupId, long accountId) throws Exception;

	public List<Appointment> findAppByDate(List<Appointment> appointments, 
			Date startDate, Date endDate) throws Exception;

	public void setAppointmentGroupShare(long appointmtId) throws Exception;
	
	public void setAppointmentMemberShare(long accountId,long groupId,long appointmtId,String members) throws Exception;
	
	public boolean checkAppointmtShared(long accountId, long appointmtId) throws Exception;
}
