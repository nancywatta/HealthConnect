package aucklanduni.ece.hc.service;

import org.springframework.transaction.annotation.Transactional;

import aucklanduni.ece.hc.repository.model.AppointmentAccountRef;

@Transactional
public interface AppointmentAccountRefService extends
		BaseService<AppointmentAccountRef> {

	public AppointmentAccountRef ifExist(long accountId, long appointmentId) throws Exception;

}
