package aucklanduni.ece.hc.util;


import java.net.SocketException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import aucklanduni.ece.hc.repository.model.Account;
import aucklanduni.ece.hc.repository.model.Appointment;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.Date;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.parameter.Cn;
import net.fortuna.ical4j.model.property.Attendee;
import net.fortuna.ical4j.model.property.CalScale;
import net.fortuna.ical4j.model.property.ProdId;
import net.fortuna.ical4j.model.property.Uid;
import net.fortuna.ical4j.model.property.Version;
import net.fortuna.ical4j.util.UidGenerator;
/**
 * 
* @ClassName: ICalendarTool 
* @Description: This is a iCalendar converter aiming to convert appointment,
* event and task to ICalendar format so as to be shown in multiple applications.
* @author Zhao Yuan
* @date 2014年9月14日 下午3:53:52 
*
 */
public class ICalendarTool {
	/**
	 * use this method to finally output iCal obj
	 * @see main method
	 */
	public static Calendar getICal4J(Appointment app,List<Account> accountList) throws SocketException{
		Calendar iCal = createICalendar();
		iCal = createAppointment(iCal, app.getTime(),app.getTime(),app.getName(),accountList);
		
		return iCal;
	}
	/**
	 * create empty ical
	 */
	public static Calendar createICalendar(){
		Calendar calendar = new Calendar();
		calendar.getProperties().add(new ProdId("-//Ben Fortuna//iCal4j 1.0//EN"));
		calendar.getProperties().add(Version.VERSION_2_0);
		calendar.getProperties().add(CalScale.GREGORIAN);
		
		return calendar;
	}
	/**
	 * create an one day event for ical
	 */
	public static Calendar createEvent(Calendar icalendar
			, java.util.Date eventDate
			, String eventName
			,String eventId) throws SocketException{
		// initialise as an all-day event..
		VEvent vEvent = new VEvent(new Date(eventDate), eventName);

		// Generate a UID for the event..
		UidGenerator ug = new UidGenerator(eventId);
		vEvent.getProperties().add(ug.generateUid());

		icalendar.getComponents().add(vEvent);
		
		return icalendar;
	}
	/**
	 * create a appointment with start time and endtime for ical
	 */
	public static Calendar createAppointment(Calendar iCalendar
			,java.util.Date startDate
			,java.util.Date endDate
			,String eventName
			,List<Account> accountList) throws SocketException{
		// Create the event
		VEvent appointment = new VEvent(new Date(startDate), new Date(endDate), eventName);

		// generate unique identifier..
		UidGenerator ug = new UidGenerator("uidGen");
		Uid uid = ug.generateUid();
		appointment.getProperties().add(uid);

		if(accountList!=null && accountList.size()>0){
			appointment = addAttendee(appointment,accountList);
		}

		// Add the event and print
		iCalendar.getComponents().add(appointment);
		
		return iCalendar;
	}
	/**
	 * add attendee for an appointment(from accountList)
	 */
	public static VEvent addAttendee(VEvent appointment,List<Account> accountList){
		//	add attendees..
//		for(Account account:accountList){
		for(int i=0;i<accountList.size();i++){
			Account account = (Account)accountList.get(i);
			Attendee attendee = new Attendee(URI.create(account.getEmail()));
//			attendee.getParameters().add(Role.REQ_PARTICIPANT);
			attendee.getParameters().add(new Cn(account.getUsername()));
			appointment.getProperties().add(attendee);
		}

		return appointment;
	}
	

	public static void main(String[] args) throws SocketException{
//		System.out.println(createICalendar());
//		System.out.println(createEvent(createICalendar(),new Date(),"namename","11111"));
//		System.out.println(createAppointment(createICalendar(),new Date(),new Date(),"appointmentname"));
//	
		Appointment app = new Appointment();
		app.setId(555);
		app.setName("nananana");
		app.setTime(new Date());
		
		List<Account> accountList = new ArrayList<Account>();
		Account acc1 = new Account();
		acc1.setEmail("zhangsan@email");
		acc1.setUsername("zhangsan");
		accountList.add(acc1);
		
		Account acc2 = new Account();
		acc2.setEmail("lisi@email");
		acc2.setUsername("lisi");
		accountList.add(acc2);
		
		System.out.println(getICal4J(app,accountList));
	}
}
