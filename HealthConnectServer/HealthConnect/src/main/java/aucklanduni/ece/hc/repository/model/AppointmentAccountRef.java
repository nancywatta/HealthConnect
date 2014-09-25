package aucklanduni.ece.hc.repository.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.hibernate.envers.Audited;
/**
 * 
* @ClassName: AppointmentAccountRef 
* @Description: This is an Entity class relating to actural database table
* This is to contain the relationship between Appointment and Account 
* @author Zhao Yuan
* @date 2014年9月15日 下午9:00:12 
*
 */
@Entity
@Audited
@Table(name = "APP_ACC_REF")
public class AppointmentAccountRef implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID", unique = true, nullable = false, precision = 20, scale = 0)
	private long id;

	@Column(name = "appointment_id", nullable = false, precision = 20, scale = 0)
	private long appointmentId;
	
	@Column(name = "account_id", nullable = false, precision = 20, scale = 0)
	private long accountId;
	
	@Column(name = "expiration_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date expirationDate;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getAppointmentId() {
		return appointmentId;
	}

	public void setAppointmentId(long appointmentId) {
		this.appointmentId = appointmentId;
	}

	public long getAccountId() {
		return accountId;
	}

	public void setAccountId(long accountId) {
		this.accountId = accountId;
	}

	public Date getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}
	

}
