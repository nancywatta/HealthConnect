package aucklanduni.ece.hc.repository.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

@Entity
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

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}
	

}
