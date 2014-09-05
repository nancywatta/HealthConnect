package aucklanduni.ece.hc.repository.dao.impl;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import aucklanduni.ece.hc.repository.dao.AppointmentDao;
import aucklanduni.ece.hc.repository.model.Appointment;
@Repository
public class AppointmentDaoImpl  extends BaseDaoImpl<Appointment> implements AppointmentDao{

	private SessionFactory sessionFactory;

	public Appointment findByName(String appointmentName) {
		String hql="from Appointment as a where a.name=?";
		Appointment appointment=(Appointment) sessionFactory.getCurrentSession().createQuery(hql);
		return appointment;
	}


}
