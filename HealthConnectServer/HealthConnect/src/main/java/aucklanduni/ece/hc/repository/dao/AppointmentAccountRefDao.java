package aucklanduni.ece.hc.repository.dao;

import aucklanduni.ece.hc.repository.model.AppointmentAccountRef;

public interface AppointmentAccountRefDao extends BaseDao<AppointmentAccountRef> {
	
	AppointmentAccountRef ifExist(long accountId, long appointmentId) throws Exception;

}
