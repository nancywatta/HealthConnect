package aucklanduni.ece.hc.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import aucklanduni.ece.hc.repository.dao.AppointmentAccountRefDao;
import aucklanduni.ece.hc.repository.model.Account;
import aucklanduni.ece.hc.repository.model.AppointmentAccountRef;
import aucklanduni.ece.hc.service.AppointmentAccountRefService;

@Service
public class AppointmentAccountRefServiceImpl extends
		BaseServiceImpl<AppointmentAccountRef> implements
		AppointmentAccountRefService {
	@Autowired
	private AppointmentAccountRefDao aarDao;

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
		return aarDao.ifExist(accountId,appointmentId);
	}
	
	/**
	 * @Title: findByAppointmentId
	 * @Description: Function will return all non expired entries from APP_ACC_REF table
	 * based on input appointmentId.
	 * 
	 * @param appointmentId
	 * @return List<AppointmentAccountRef>
	 * @throws Exception
	 */
	public List<AppointmentAccountRef> findByAppointmentId(long appointmentId) throws Exception {
		try {
			List<AppointmentAccountRef> aarList = new ArrayList<AppointmentAccountRef>();
			
			aarList = aarDao.findByHql("from AppointmentAccountRef WHERE "
					+ "appointmentId=" + appointmentId 
					+ " and expirationDate IS NULL");
			
			return aarList;
			
		} catch (Exception e) {
			throw e;
		}
	}
	
	public List<AppointmentAccountRef> findByAppointmentIdAccountId(long appointmentId,long accountId) throws Exception {
		try {
			List<AppointmentAccountRef> aarList = new ArrayList<AppointmentAccountRef>();
			
			aarList = aarDao.findByHql("from AppointmentAccountRef WHERE "
					+ "appointmentId=" + appointmentId 
					+ " and accountId=" + accountId 
					+ " and expirationDate IS NULL");
			
			return aarList;
			
		} catch (Exception e) {
			throw e;
		}
	}
	
	
	
	//TODO: doc
		public boolean checkAppointmtShared(long accountId, long appointmtId) throws Exception {
			try {
				List<AppointmentAccountRef> refDtls = new ArrayList<AppointmentAccountRef>();
				refDtls = aarDao.findByHql("from AppointmentAccountRef ref WHERE "
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
	
	public void expireAppointmtSharedState(long appointmentId,List<Account> removeList) throws Exception {
		try {
			
			List<AppointmentAccountRef> aarList = new ArrayList<AppointmentAccountRef>();
			
			for(Account member: removeList){
				aarList = findByAppointmentIdAccountId(appointmentId,member.getId());
				for(AppointmentAccountRef aarEntry: aarList) {
					aarEntry.setExpirationDate(new Date());
					update(aarEntry);
				}
			}
		} catch (Exception e) {
			throw e;
		}
	}
	
	public void addAppointmtShared(long appointmentId,List<Account> addList,long groupId) throws Exception {
		try {

			for(Account member: addList){
				AppointmentAccountRef aarEntry = new AppointmentAccountRef();
					aarEntry.setAccountId(member.getId());
					aarEntry.setAppointmentId(appointmentId);
					aarEntry.setGroupId(groupId);
					aarDao.add(aarEntry);
				}
			}
		
		catch (Exception e) {
			throw e;
		}
	}

}
