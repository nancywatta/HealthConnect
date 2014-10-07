package aucklanduni.ece.hc.service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import aucklanduni.ece.hc.repository.model.AppointmentAccountRef;

@Transactional
public interface AppointmentAccountRefService extends
		BaseService<AppointmentAccountRef> {

	public AppointmentAccountRef ifExist(long accountId, long appointmentId) throws Exception;
	
	public List<AppointmentAccountRef> findByAppointmentId(long appointmentId) throws Exception;
}
