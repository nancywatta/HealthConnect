package aucklanduni.ece.hc.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import aucklanduni.ece.hc.repository.dao.AppointmentAccountRefDao;
import aucklanduni.ece.hc.repository.model.AppointmentAccountRef;
import aucklanduni.ece.hc.service.AppointmentAccountRefService;

@Service
public class AppointmentAccountRefServiceImpl extends
		BaseServiceImpl<AppointmentAccountRef> implements
		AppointmentAccountRefService {
	@Autowired
	private AppointmentAccountRefDao aafDao;

	public AppointmentAccountRef ifExist(long accountId, long appointmentId)
			throws Exception {
		return aafDao.ifExist(accountId,appointmentId);
	}
	
	public List<AppointmentAccountRef> findByAppointmentId(long appointmentId) throws Exception {
		try {
			List<AppointmentAccountRef> aarList = new ArrayList<AppointmentAccountRef>();
			
			aarList = aafDao.findByHql("from AppointmentAccountRef WHERE "
					+ "appointmentId=" + appointmentId 
					+ " and expirationDate IS NULL");
			
			return aarList;
			
		} catch (Exception e) {
			throw e;
		}
	}

}
