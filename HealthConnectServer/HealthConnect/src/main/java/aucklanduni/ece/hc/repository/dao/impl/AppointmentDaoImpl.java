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
				"from Appointment app, Account acc, AppointmentAccountRef aaf "+
				"where acc.id=? and acc.id=aaf.accountId and app.id=aaf.appointmentId";
		List<Appointment> appointments=(List<Appointment>)s.createQuery(hql).setParameter(0, accountId).list();
		return appointments;
	}
}
