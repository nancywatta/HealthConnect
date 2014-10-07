package aucklanduni.ece.hc.repository.dao.impl;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import aucklanduni.ece.hc.repository.dao.AppointmentAccountRefDao;
import aucklanduni.ece.hc.repository.model.AppointmentAccountRef;

@Repository
public class AppointmentAccountRefDaoImpl extends BaseDaoImpl<AppointmentAccountRef> implements AppointmentAccountRefDao{
	
	/**
	 * @Title: ifExist
	 * @Description: Function will return rows from APP_ACC_REF table
	 * based on input accountId and appointmentId.
	 * 
	 * @param accountId
	 * @param appointmentId
	 * @return AppointmentAccountRef
	 * @throws Exception
	 */
	public AppointmentAccountRef ifExist(long accountId, long appointmentId)
			throws Exception {
		Session s = getSession();
		String hql="select aaf "
				+"from AppointmentAccountRef aaf "
				+"where aaf.accountId=? and aaf.appointmentId=?";
		
		return (AppointmentAccountRef) s.createQuery(hql).setParameter(0, accountId).setParameter(1, appointmentId).uniqueResult();
	}
}
