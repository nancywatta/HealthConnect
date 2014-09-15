package aucklanduni.ece.hc.util;


import java.net.SocketException;
import java.net.URI;
import java.util.List;

import aucklanduni.ece.hc.repository.model.Account;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.Date;
import net.fortuna.ical4j.model.DateTime;
import net.fortuna.ical4j.model.TimeZone;
import net.fortuna.ical4j.model.TimeZoneRegistry;
import net.fortuna.ical4j.model.TimeZoneRegistryFactory;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.component.VTimeZone;
import net.fortuna.ical4j.model.parameter.Cn;
import net.fortuna.ical4j.model.parameter.Role;
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

	public static Calendar createICalendar(){
		Calendar calendar = new Calendar();
		calendar.getProperties().add(new ProdId("-//Ben Fortuna//iCal4j 1.0//EN"));
		calendar.getProperties().add(Version.VERSION_2_0);
		calendar.getProperties().add(CalScale.GREGORIAN);
		
		return calendar;
	}
	
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
	
	public static Calendar createAppointment(Calendar iCalendar
			,java.util.Date startDate
			,java.util.Date endDate
			,String eventName) throws SocketException{
		// Create the event
		VEvent appointment = new VEvent(new Date(startDate), new Date(endDate), eventName);

		// generate unique identifier..
		UidGenerator ug = new UidGenerator("uidGen");
		Uid uid = ug.generateUid();
		appointment.getProperties().add(uid);

//		appointment = addAttendee(appointment);

		// Add the event and print
		iCalendar.getComponents().add(appointment);
		
		return iCalendar;
	}
	
	public static VEvent addAttendee(VEvent appointment,List<Account> accountList){
		//	add attendees..
		for(Account account:accountList){
			Attendee attendee = new Attendee(URI.create(account.getEmail()));
//			attendee.getParameters().add(Role.REQ_PARTICIPANT);
			attendee.getParameters().add(new Cn(account.getUsername()));
			appointment.getProperties().add(attendee);
		}

		return appointment;
	}
	public static void main(String[] args) throws SocketException{
		System.out.println(createICalendar());
		System.out.println(createEvent(createICalendar(),new Date(),"namename","11111"));
		System.out.println(createAppointment(createICalendar(),new Date(),new Date(),"appointmentname"));
	}
}
