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
/**
 * 
* @ClassName: Appointment 
* @Description: This is an Entity class relating to actural database table
* This is to contain information of all Appintment
* @author Zhao Yuan
* @date 2014年9月15日 下午9:00:02 
*
 */
@Entity
@Audited
@Table(name = "APPOINTMENT")
public class Appointment implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID", unique = true, nullable = false, precision = 20, scale = 0)
	private long id;
	
	@Column(name = "name")
	private String name;

	@Column(name = "start_time", nullable = false)
	@Temporal(TemporalType.TIME)
	private Date startTime;
	
	@Column(name = "end_time", nullable = false)
	@Temporal(TemporalType.TIME)
	private Date endTime;
	
	@Column(name = "start_date", nullable = false)
	@Temporal(TemporalType.DATE)
	private Date startDate;
	
	@Column(name = "end_date")
	@Temporal(TemporalType.DATE)
	private Date endDate;

	@Column(name = "execute_time")
	private long executeTime;
	
	@Column(name = "location")
	private String location;
	
	@Column(name = "description")
	private String description;
	
	@Column(name = "appointment_type")
	private String appointmentType;

	@Column(name = "status")
	private String status;
	
	@Column(name = "shared_type")
	private String sharedType;
	
	@Column(name = "group_id", nullable = false, precision = 20, scale = 0)
	private long groupId;

	@Column(name = "created_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createDate;
	
	@Column(name = "updated_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date updatedDate;
	
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

	@JsonFormat(pattern="HH:mm:ss")
	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	@JsonFormat(pattern="HH:mm:ss")
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

	@JsonFormat(pattern="yyyy-MM-dd")
	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	@JsonFormat(pattern="yyyy-MM-dd")
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

	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")  
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")  
	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
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
