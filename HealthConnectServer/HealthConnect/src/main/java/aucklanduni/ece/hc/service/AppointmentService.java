package aucklanduni.ece.hc.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

import aucklanduni.ece.hc.repository.model.Appointment;
import aucklanduni.ece.hc.webservice.model.ValidationFailException;

@Transactional
public interface AppointmentService  extends BaseService<Appointment> {


	public Map<String, ArrayList<Appointment>> showAllAppointment(long accountId) throws Exception;
    //View the target appointments according to the account id.

	public void createNewAppointment(long accountId, Date appointmentTime,
			String appointmentName, String appointmentLocation) throws Exception;

	public List<Appointment> findAllByAccountId(long accountId) throws Exception; 
	      
}
