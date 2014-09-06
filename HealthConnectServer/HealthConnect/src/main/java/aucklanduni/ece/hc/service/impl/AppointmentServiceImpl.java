package aucklanduni.ece.hc.service.impl;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import aucklanduni.ece.hc.repository.dao.AppointmentDao;
import aucklanduni.ece.hc.repository.dao.impl.AppointmentDaoImpl;
import aucklanduni.ece.hc.repository.dao.impl.GroupDaoImpl;
import aucklanduni.ece.hc.repository.model.Appointment;
import aucklanduni.ece.hc.repository.model.Database;
import aucklanduni.ece.hc.repository.model.Group;
import aucklanduni.ece.hc.service.AppointmentService;

@Service
public class AppointmentServiceImpl  extends BaseServiceImpl<Appointment> implements AppointmentService{
	@Autowired
	private AppointmentDao appointmentDao;
	/*public Appointment findByName(String appointmentName) {
		return appointmentDao.findByName(appointmentName);
	}
	public Map<String, ArrayList<Group>> GetGroups(long accountId)throws Exception {
    Map<String, ArrayList<Group>> groups = null;
	*/
	public Map<String, ArrayList<Appointment>> showAllAppointment(long accountId) throws Exception{
		Map<String, ArrayList<Appointment>> appointments = null;
	 try{
		 Database database = new Database();
		 Connection connection = database.Get_Connection();
		 AppointmentDaoImpl appointmentDao = new AppointmentDaoImpl();
		 appointments = appointmentDao.showAllAppointment(connection, accountId);
	 }
	 catch (Exception e){
		 throw e;
	 }
	 return appointments;
	 
	}
	
}
