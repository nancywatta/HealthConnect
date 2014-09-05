package aucklanduni.ece.hc.repository.dao;

import aucklanduni.ece.hc.repository.model.Appointment;

public interface AppointmentDao extends BaseDao<Appointment> {

	public Appointment findByName(String appointmentName);


}
