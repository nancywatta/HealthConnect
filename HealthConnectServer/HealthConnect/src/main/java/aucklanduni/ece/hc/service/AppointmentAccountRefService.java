package aucklanduni.ece.hc.service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import aucklanduni.ece.hc.repository.model.Account;
import aucklanduni.ece.hc.repository.model.AppointmentAccountRef;

@Transactional
public interface AppointmentAccountRefService extends
		BaseService<AppointmentAccountRef> {

	public AppointmentAccountRef ifExist(long accountId, long appointmentId) throws Exception;
	
	public List<AppointmentAccountRef> findByAppointmentId(long appointmentId) throws Exception;
	
	public List<AppointmentAccountRef> findByAppointmentIdAccountId(long appointmentId,long accountId) throws Exception;
	
	public boolean checkAppointmtShared(long accountId, long appointmtId) throws Exception;
	
	public void expireAppointmtSharedState(long appointmentId,List<Account> removeList) throws Exception;
	
	public void addAppointmtShared(long appointmentId,List<Account> addList,long groupId) throws Exception;
}
