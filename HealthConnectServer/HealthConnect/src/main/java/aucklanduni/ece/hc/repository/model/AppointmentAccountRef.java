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
*CREATE TABLE `APP_ACC_REF` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `appointment_id` bigint(20) NOT NULL,
  `account_id` bigint(20) NOT NULL,
  `group_id` bigint(20) DEFAULT NULL,
  `expiration_date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `appaccref_acc_fk` (`account_id`),
  KEY `appaccref_app_fk` (`appointment_id`),
  KEY `appaccref_group_fk` (`group_id`),
  CONSTRAINT `appaccref_app_fk` FOREIGN KEY (`appointment_id`) REFERENCES `appointment` (`id`),
  CONSTRAINT `appaccref_acc_fk` FOREIGN KEY (`account_id`) REFERENCES `account` (`id`),
  CONSTRAINT `appaccref_group_fk` FOREIGN KEY (`group_id`) REFERENCES `group_info` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8
;
 */
@Entity
@Audited
@Table(name = "APP_ACC_REF")
public class AppointmentAccountRef implements Serializable {

	private static final long serialVersionUID = -4456560451348816336L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID", unique = true, nullable = false, precision = 20, scale = 0)
	private long id;

	/**
	 * represents the appointment id (PK of that table) 
	 */
	@Column(name = "appointment_id", nullable = false, precision = 20, scale = 0)
	private long appointmentId;
	
	/**
	 * represents the account id (PK of that table) 
	 */
	@Column(name = "account_id", nullable = false, precision = 20, scale = 0)
	private long accountId;
	
	/**
	 * represents the group id (PK of that table) 
	 */
	@Column(name = "group_id",  precision = 20, scale = 0)
	private long groupId;
	
	/**
	 * represents the record is no longer valid
	 * Right now it isnt used
	 * TODO should be deleted from application
	 * as we have other ways to control whether there is or isnt a relationship 
	 * between appointment and an user.
	 * And for now there is no need to look back on the previous shared members
	 * of am appointment.
	 */
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

	public long getGroupId() {
		return groupId;
	}

	public void setGroupId(long groupId) {
		this.groupId = groupId;
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
