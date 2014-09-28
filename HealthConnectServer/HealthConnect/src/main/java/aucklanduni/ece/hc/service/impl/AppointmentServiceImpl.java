package aucklanduni.ece.hc.service.impl;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import aucklanduni.ece.hc.repository.dao.AccountDao;
import aucklanduni.ece.hc.repository.dao.AppointmentDao;
import aucklanduni.ece.hc.repository.dao.impl.AppointmentDaoImpl;
import aucklanduni.ece.hc.repository.dao.impl.GroupDaoImpl;
import aucklanduni.ece.hc.repository.model.Account;
import aucklanduni.ece.hc.repository.model.Appointment;
import aucklanduni.ece.hc.repository.model.Database;
import aucklanduni.ece.hc.repository.model.Group;
import aucklanduni.ece.hc.repository.model.Member;
import aucklanduni.ece.hc.service.AppointmentService;
import aucklanduni.ece.hc.webservice.model.ValidationFailException;

@Service
public class AppointmentServiceImpl  extends BaseServiceImpl<Appointment> implements AppointmentService{
	@Autowired
	private AppointmentDao appointmentDao;
	@Autowired
	private AccountDao accountDao;
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
	//Chengchen 12/09/14
	public void createNewAppointment(long accountId, Date appointmentTime,
			String appointmentName, String appointmentLocation) throws Exception {
		
		try{
			//save appointment in APPOINTMENT table
			Appointment newAppointment = new Appointment();
			newAppointment.setName(appointmentName);
			newAppointment.setTime(appointmentTime);
			newAppointment.setLocation(appointmentLocation);
			Account account = accountDao.findById(accountId);
			createAppointment(newAppointment,account,appointmentName,appointmentTime,appointmentLocation);
			
		    
				
		}catch (ValidationFailException ve) {
			throw ve;
		}catch (Exception e) {
			throw e;
		}
		}
	
	
	
	
	
	private void createAppointment(Appointment appointment, Account account,String appointmentName, Date appointmentTime,
			String appointmentLocation) throws Exception {
		// TODO Auto-generated method stub
		appointmentDao.add(appointment);
		
		
	}
	public List<Appointment> findAllByAccountId(long accountId) throws Exception{
		return appointmentDao.findAllByAccountId(accountId);
	}
	
	//public List<Appointment> findAllByGroupShared(long accountId)throws Exception{
		//return appointmentDao.findAllByGroupShared(accountId);
	//}
	public List<Appointment> findAllByGroupId(long accountId, long groupId) throws Exception{
		return appointmentDao.findAllByGroupId(accountId,groupId);
	}
	
	//Yalu
	public List<Appointment> filterByUsername(String username) throws Exception {
		
		try {
			Database database= new Database();
			Connection connection = database.Get_Connection();

			long accountId = accountDao.getAccIdByUserName(connection, username);
			
			return appointmentDao.findAllByAccountId(accountId);
		}
		catch (Exception e) {
			throw e;
		}
		
	}
	
	public List<Appointment> filterByDate(long accountId, Date startDate, Date endDate) throws Exception{
		
		try {
			Database database= new Database();
			Connection connection = database.Get_Connection();
			return appointmentDao.filterByDate(connection,accountId, startDate, endDate);
		}
		catch (Exception e) {
			throw e;
		}
	}
}
	

