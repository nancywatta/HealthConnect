package aucklanduni.ece.hc.repository.dao;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Map;

import aucklanduni.ece.hc.repository.dao.impl.BaseDaoImpl;
import aucklanduni.ece.hc.repository.model.Appointment;

public interface AppointmentDao extends BaseDao<Appointment> {

	//public Appointment findByName(String appointmentName);
	public Map<String, ArrayList<Appointment>> showAllAppointment(Connection connection,
			long accountId) throws Exception;
}
