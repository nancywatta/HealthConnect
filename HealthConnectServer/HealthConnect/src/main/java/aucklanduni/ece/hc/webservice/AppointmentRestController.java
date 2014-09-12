package aucklanduni.ece.hc.webservice;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import aucklanduni.ece.hc.repository.model.Account;
import aucklanduni.ece.hc.repository.model.Appointment;
import aucklanduni.ece.hc.repository.model.Dictionary;
import aucklanduni.ece.hc.service.AccountService;
import aucklanduni.ece.hc.service.AppointmentService;
import aucklanduni.ece.hc.service.DictionaryService;
import aucklanduni.ece.hc.webservice.model.HCMessage;
import aucklanduni.ece.hc.webservice.model.ValidationFailException;

@RestController
@RequestMapping("/service/appointment/")
public class AppointmentRestController {
	@Autowired
	private AppointmentService appointmentService;
	@Autowired
	private DictionaryService roleService;
	@Autowired
	private AccountService accountService;
	

	@RequestMapping(value="/createAppointment",method = RequestMethod.POST
			,headers="Accept=application/json"
			)
	public HCMessage createAppointment(HttpServletRequest request, HttpServletResponse response
			,@RequestParam(value="accountId") long accountId
			,@RequestParam("appointmentTime") Date appointmentTime
			,@RequestParam("appointmentName") String appointmentName
			,@RequestParam("appointmentLocation") String appointmentLocation
			) {
		HCMessage message = new  HCMessage();
		try {
			/*Dictionary role = null;
			role = roleService.findById(roleId);

			// Only Patient or Nurse can create a Group
			if(! (role.getValue().compareTo("P")==0 
					|| role.getValue().compareTo("N")==0)) {
				throw new ValidationFailException("Only Patient or Nurse can create a Group");
			}*/ //do not need to use roleId here.

			Account account = null;
			// check if given accountId exists
			account = accountService.findById(accountId);
			if(account == null) {
				throw new ValidationFailException("Account does not exist");
			}

			// check if group name already exists
			List<Appointment> appointments = new ArrayList<Appointment>();
			appointments = appointmentService.findByHql("from Appointment WHERE appointmentname='" + appointmentName + "'");
			if(appointments.size() > 0) {
				throw new ValidationFailException("Appointment Name Already Exists");
			}

			appointmentService.createNewAppointment(accountId, appointmentTime, appointmentName, appointmentLocation);

		}catch(ValidationFailException ve) {
			message.setFail("404", ve.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			message.setFail("400", e.getMessage());
		}

		return message;
	}
}
