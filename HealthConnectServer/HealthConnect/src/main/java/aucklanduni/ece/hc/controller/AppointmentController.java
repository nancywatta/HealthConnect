package aucklanduni.ece.hc.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;

import aucklanduni.ece.hc.repository.model.Account;
import aucklanduni.ece.hc.repository.model.Appointment;
import aucklanduni.ece.hc.repository.model.AppointmentAccountRef;
import aucklanduni.ece.hc.repository.model.Group;
import aucklanduni.ece.hc.service.AppointmentService;


@Controller
@RequestMapping("/Appointment")
public class AppointmentController {
	@Autowired
	private AppointmentService appointmentService;
	
	@RequestMapping("/createAppointment")
	@ResponseBody
	public String createAppointment(HttpServletRequest request, HttpServletResponse response,
			@RequestParam("accountId") long accountId,
			@RequestParam("appointmentTime") String appointmentTime,
			@RequestParam("appointmentName") String appointmentName,
			@RequestParam("appointmentLocation") String appointmentLocation) {
		
		Account account=new Account();
		Appointment appointment=new Appointment();
		AppointmentAccountRef aaf=new AppointmentAccountRef();
		account.setId(accountId);
		appointment.setTime(appointmentTime);
		appointment.setName(appointmentName);
		appointment.setLocation(appointmentLocation);
		//update the reference table
		aaf.setAccountId(accountId);
		//aaf.setAppointmentId(appointmentService.findByName(appointmentName).getId());
		//for this to work, manually add one row to accout table
		//insert into accout(id,email,username) values(1,"wke918","wuke")
		try {
			appointmentService.add(appointment);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	
	@RequestMapping("/viewAppointment")
	@ResponseBody
	public String viewAppointment(HttpServletRequest request, HttpServletResponse response,
			@RequestParam("accountId") long accountId){
		System.out.println(">>>>>>>>>>>>>>>>>viewAppointment"+accountId);
		String appointments=null;
		try{
			Map<String, ArrayList<Appointment>> appointmentList=appointmentService.showAllAppointment(accountId);
			Gson gson = new Gson();
			appointments = gson.toJson(appointmentList);
			System.out.println(appointments);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return appointments;
	} 
	
}
