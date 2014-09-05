package aucklanduni.ece.hc.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import aucklanduni.ece.hc.repository.dao.AppointmentDao;
import aucklanduni.ece.hc.repository.model.Appointment;
import aucklanduni.ece.hc.service.AppointmentService;

@Service
public class AppointmentServiceImpl  extends BaseServiceImpl<Appointment> implements AppointmentService{
	@Autowired
	private AppointmentDao appointmentDao;
	public Appointment findByName(String appointmentName) {
		return appointmentDao.findByName(appointmentName);
	}
	
	
}
