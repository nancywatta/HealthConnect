package aucklanduni.ece.hc.repository.dao.impl;

/*
 * 
 * author: Tech Geeks
 * time:Sep.2014
 * This AppointmentDaoImpl contact SQL with Sever.
 * 
 */

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import aucklanduni.ece.hc.repository.dao.AppointmentDao;
import aucklanduni.ece.hc.repository.model.Appointment;
import aucklanduni.ece.hc.repository.model.Group;
@Repository
public class AppointmentDaoImpl  extends BaseDaoImpl<Appointment> implements AppointmentDao{

	//private SessionFactory sessionFactory;

	/*public Appointment findByName(String appointmentName) {
		String hql="from Appointment as a where a.name=?";
		Appointment appointment=(Appointment) sessionFactory.getCurrentSession().createQuery(hql);
		return appointment;
	}*/
	

	public Map<String, ArrayList<Appointment>> showAllAppointment(Connection connection,
			long accountId) throws Exception{
		Map<String, ArrayList<Appointment>> appointments = new HashMap<String, ArrayList<Appointment>>();
		List<Appointment> appointmentData = new ArrayList<Appointment>();
		try
		{
			PreparedStatement ps = connection.prepareStatement(
					"SELECT DISTINCT ap.id, ap.name "
					+ "FROM APPOINTMENT ap "
					+ "INNER JOIN APP_ACC_REF aar "//APP_ACC_REF can be as the third variable.
					+ "ON "
					+ "ap.id=aar.appointment_id "
					+ "INNER JOIN ACCOUNT ac"
					+ "ON"
					+ "ac.id=aar.account_id"
					+ "and aar.account_id= " + accountId);//contact appointment with SQL.
			ResultSet rs = ps.executeQuery();
			while(rs.next())
			{
				Appointment appointmentObject = new Appointment();
				appointmentObject.setId(rs.getLong("id"));
				appointmentObject.setName(rs.getString("name"));
				appointmentData.add(appointmentObject);
			}
			appointments.put("appointments", (ArrayList<Appointment>) appointmentData);
			//appointments.("groups", appointments);
			return appointments;
		}
		
		// TODO Auto-generated method stub
	
		catch(Exception e)
		{
			throw e;
		}


}

	public List<Appointment> findAllByAccountId(long accountId)
			throws Exception {
		Session s=getSession();
		String hql="select app "+
				"from Appointment app, Account acc, AppointmentAccountRef aaf, Member mem "+
				"where acc.id=? and acc.id=aaf.accountId and app.id=aaf.appointmentId "
				+"UNION "
				+"select app "
				+"from Appointment app, Group gro "
				+"where app.groupId=gro.id and app.sharedType=G and gro in (select gro from Account acc, Member mem, Group gro where acc.id=? and acc.id=mem.accountId and gro.id=mem.groupId) ";
		List<Appointment> appointments=(List<Appointment>)s.createQuery(hql).setParameter(0, accountId).list();
		return appointments;
	}
	
	public List<Appointment> findAllByGroupId(long accountId, long groupId) throws Exception{
		Session s=getSession();
		String hql="select app "+
		          "from Appointment app, AppointmentAccountRef aaf, Member mem "+
				  "where mem.accountId=? and app.groupId=? and mem.accountId=aaf.accountId and app.id=aaf.appointmentId and mem.groupId=app.groupId";
		List<Appointment> appointments=(List<Appointment>)s.createQuery(hql).setParameter(0, accountId).list();
		return appointments;
	}
	//add view appointments that has being shared in user's group
	/*public List<Appointment> findAllByGroupShared(long accountId) throws Exception{
		Session s = getSession();
		String hql="select app "+
		          "from Appointment app, Account acc, Member mem "+
				  "where acc.id=? and mem.accountId=acc.id and mem.groupId=app.groupId and app.isShared=T";
		List<Appointment> appointments=(List<Appointment>)s.createQuery(hql).setParameter(0, accountId).list();
		return appointments;
	}*/

	public List<Appointment> filterByDate(Connection connection,
			long accountId, Date startDate, Date endDate) throws Exception {
		
		List<Appointment> appointments = new ArrayList<Appointment>();
		try
		{
			PreparedStatement ps = connection.prepareStatement(
					"SELECT app.* FROM appointment app  "
					+ "INNER JOIN app_acc_ref aar ON app.id = aar.appointment_id AND aar.account_id=?"
					+ " WHERE app.created_date BETWEEN ? AND ?");
			ps.setLong(1, accountId);
			ps.setDate(2, (java.sql.Date) startDate);
			ps.setDate(3, (java.sql.Date) endDate);
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				Appointment app = new Appointment();
				app.setId(rs.getLong("id"));
				app.setName(rs.getString("name"));
				//app.setTime(rs.getDate("time"));
				app.setLocation(rs.getString("location"));
				app.setDescription(rs.getString("description"));
				app.setCreateDate(rs.getDate("created_date"));
				appointments.add(app);
				
			}
			return appointments;
		}
		catch(Exception e)
		{
			throw e;
		}
	}

	public List<Appointment> findAppointmentsByGroup(long groupId)
			throws Exception {
		Session s = getSession();
		String hql="select app "
				+"from Appointment app "
				+"where app.groupId=?";
		return (List<Appointment>)s.createQuery(hql).setParameter(0, groupId).list();
	}
	
	@SuppressWarnings("unchecked")
	public List<Appointment> findAppointmentByGroupId(List<Long> groupId) throws Exception {
		try {
			Session s=getSession();
			String hql="select a from Appointment a WHERE "
					+ "a.sharedType='G' "
					+ "and a.groupId in (:groupIds)";
			List<Appointment> appointments = (List<Appointment>)s.createQuery(hql)
					.setParameterList("groupIds", groupId).list();

			return appointments;

		} catch (Exception e) {
			throw e;
		}
	}

	public List<Appointment> findAppByGroupIdMemberId(List<Long> groupId, long memberId, long accountId) throws Exception {
		try {
			Session s=getSession();
			String hql=
					"select a from Appointment a, AppointmentAccountRef aar WHERE "
							+ "a.sharedType='M' "
							+ "and a.groupId in (:groupIds) "
							+ "and a.id = aar.appointmentId "
							+ "and aar.accountId = :memberId "
							+ "and exists (select a1 from AppointmentAccountRef a1 WHERE "
							+ "a1.appointmentId = aar.appointmentId "
							+ "and a1.accountId= :accountId)";
			@SuppressWarnings("unchecked")
			List<Appointment> appointments = (List<Appointment>)s.createQuery(hql)
			.setParameterList("groupIds", groupId)
			.setParameter("memberId", memberId)
			.setParameter("accountId", accountId)
			.list();

			return appointments;

		} catch (Exception e) {
			throw e;
		}
	}
	
	public List<Appointment> findAppByGroupIdAccountId(List<Long> groupId, long accountId) throws Exception {
		try {
			Session s=getSession();
			String hql=
					"select a from Appointment a, AppointmentAccountRef aar WHERE "
							+ "a.sharedType='M' "
							+ "and a.groupId in (:groupIds) "
							+ "and a.id = aar.appointmentId "
							+ "and aar.accountId = :accountId";
			@SuppressWarnings("unchecked")
			List<Appointment> appointments = (List<Appointment>)s.createQuery(hql)
			.setParameterList("groupIds", groupId)
			.setParameter("accountId", accountId)
			.list();

			return appointments;

		} catch (Exception e) {
			throw e;
		}
	}

	public List<Appointment> findAppByDate(List<Appointment> appointments, 
			Date startDate, Date endDate) throws Exception {
		try {
			Session s=getSession();
			List<Long> appIds = new ArrayList<Long>();
			for (Appointment a: appointments) {
				appIds.add(a.getId());
			}

			String hql= "select a from Appointment a WHERE "
					+ "a.id in (:appointments) "
					+ "and a.startDate BETWEEN :startDate AND :endDate";

			@SuppressWarnings("unchecked")
			List<Appointment> appoints = (List<Appointment>)s.createQuery(hql)
			.setParameterList("appointments", appIds)
			.setParameter("startDate", startDate)
			.setParameter("endDate", endDate)
			.list();

			return appoints;

		} catch (Exception e) {
			throw e;
		}

	}

//	public List<Appointment> filterByUserName(long accountId) throws Exception {
//		
//		return null;
//	}
}
