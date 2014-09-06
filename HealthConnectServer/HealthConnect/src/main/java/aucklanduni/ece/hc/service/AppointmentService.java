package aucklanduni.ece.hc.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

import aucklanduni.ece.hc.repository.model.Appointment;

@Transactional
public interface AppointmentService  extends BaseService<Appointment> {


	public Map<String, ArrayList<Appointment>> showAllAppointment(long accountId) throws Exception;


}
