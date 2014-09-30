package aucklanduni.ece.hc.service.impl;

import java.io.Serializable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import aucklanduni.ece.hc.repository.dao.AccountDao;
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

}
