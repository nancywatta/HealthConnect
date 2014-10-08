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

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
/**
 * 
* @ClassName: Appointment 
* @Description: This is an Entity class relating to actural database table
* This is to contain information of all Appintment
* @author Zhao Yuan
* @date 2014年9月15日 下午9:00:02 
*
*CREATE TABLE `APPOINTMENT` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(64) DEFAULT NULL,
  `location` varchar(64) DEFAULT NULL,
  `start_time` time NOT NULL,
  `end_time` time NOT NULL,
  `start_date` datetime NOT NULL,
  `end_date` datetime DEFAULT NULL,
  `execute_time` bigint(20) DEFAULT NULL,
  `description` varchar(256) DEFAULT NULL,
  `appointment_type` varchar(64) DEFAULT NULL,
  `status` varchar(4) DEFAULT NULL,
  `shared_type` varchar(4) DEFAULT NULL,
  `group_id` bigint(20) DEFAULT NULL,
  `created_date` datetime DEFAULT NULL,
  `updated_date` datetime DEFAULT NULL,
  `expiration_date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `app_group_fk` (`group_id`),
  CONSTRAINT `app_group_fk` FOREIGN KEY (`group_id`) REFERENCES `group_info` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8
; 
 */
@Entity
@Audited
@Table(name = "APPOINTMENT")
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@JsonDeserialize()
@JsonIgnoreProperties(ignoreUnknown = true)
public class Appointment implements Serializable {

	private static final long serialVersionUID = -7329335127924971404L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID", unique = true, nullable = false, precision = 20, scale = 0)
	private long id;
	
	/**
	 * appointment name 
	 */
	@Column(name = "name")
	private String name;

	/**
	 * start time for this appointment
	 */
	@Column(name = "start_time", nullable = false)
	@Temporal(TemporalType.TIME)
	private Date startTime;
	
	/**
	 * end time for this appointment
	 */
	@Column(name = "end_time", nullable = false)
	@Temporal(TemporalType.TIME)
	private Date endTime;
	
	/**
	 * start date of this appointment
	 */
	@Column(name = "start_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date startDate;
	
	/**
	 * end date of this appointment
	 */
	@Column(name = "end_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date endDate;

	/**
	 * execute time during start date and end date when the appointment will be executed.
	 */
	@Column(name = "execute_time")
	private long executeTime;
	
	/**
	 * location of this appointment
	 */
	@Column(name = "location")
	private String location;
	
	/**
	 * short description of this appointment
	 */
	@Column(name = "description")
	private String description;
	
	/**
	 * This allows users to have different types of appointment
	 * so as to manage or filter effectively
	 */
	@Column(name = "appointment_type")
	private String appointmentType;

	/**
	 * the status of this appointment. Right now this field has not been used in app
	 * But it allows to save different status like "W":waiting for other to accept this appointment
	 * "C":cancel by others and so on.
	 */
	@Column(name = "status")
	private String status;
	
	/**
	 * It means the range where this appointment is shared.
	 * Note that by default, a appointment is shared with all members in a group
	 * But patients can edit to allow only a few member to be shared with.
	 * Value : "G":group; "M":members
	 * When its G, it means all members in group can view this appointment
	 * When its M, it means only members in App_acc_ref table are allowed 
	 * to view  the appointment.
	 */
	@Column(name = "shared_type")
	private String sharedType;
	
	/**
	 * mean which group this appointment belongs to 
	 */
	@Column(name = "group_id", nullable = false, precision = 20, scale = 0)
	private long groupId;

	/**
	 * The datetime when a record is first created
	 */
	@Column(name = "created_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createDate;
	
	/**
	 * The datetime when a record is updated
	 */
	@Column(name = "updated_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date updatedDate;
	
	/**
	 * The datetime when a record is expired
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@JsonFormat(pattern="HH:mm:ss", timezone = "GMT+12")
	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	@JsonFormat(pattern="HH:mm:ss", timezone = "GMT+12")
	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	
	public long getExecuteTime(){
		return executeTime;
	}

	public void setExecuteTime(long executeTime) {
		this.executeTime = executeTime;
	}

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss", timezone = "GMT+13")
	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss", timezone = "GMT+13")
	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String desciption) {
		this.description = desciption;
	}

	public String getAppointmentType() {
		return appointmentType;
	}

	public void setAppointmentType(String appointmentType) {
		this.appointmentType = appointmentType;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getSharedType() {
		return sharedType;
	}

	public void setSharedType(String sharedType) {
		this.sharedType = sharedType;
	}

	public long getGroupId() {
		return groupId;
	}

	public void setGroupId(long groupId) {
		this.groupId = groupId;
	}

	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone = "GMT+13")  
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone = "GMT+13")  
	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone = "GMT+13")
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
