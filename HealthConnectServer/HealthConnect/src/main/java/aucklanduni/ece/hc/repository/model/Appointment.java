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

import com.fasterxml.jackson.annotation.JsonFormat;
/**
 * 
* @ClassName: Appointment 
* @Description: This is an Entity class relating to actural database table
* This is to contain information of all Appintment
* @author Zhao Yuan
* @date 2014年9月15日 下午9:00:02 
* 
* CREATE TABLE `APPOINTMENT` (
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
*
 */
@Entity
@Table(name = "APPOINTMENT")
public class Appointment implements Serializable {


	private static final long serialVersionUID = -7958131297262423223L;

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
	 * appointment time 
	 */
	@Column(name = "time", nullable = false)
	private Date time;
	
	/**
	 * location of this appointment
	 */
	@Column(name = "location")
	private String location;
	
	/**
	 * short description of this appointment
	 */
	@Column(name = "desciption")
	private String desciption;

	/**
	 * the status of this appointment. Right now this field has not been used in app
	 * But it allows to save different status like "W":waiting for other to accept this appointment
	 * "C":cancel by others and so on.
	 */
	@Column(name = "status")
	private String status;
	
	/**
	 * It means whether this appointment is shared.
	 * Note that by default, a appointment is shared with all members in a group
	 * But patients can edit to allow only a few member to be shared with.
	 * Value : "T" "F"
	 */
	@Column(name = "isShared")
	private String isShared;

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

	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")  
	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getDesciption() {
		return desciption;
	}

	public void setDesciption(String desciption) {
		this.desciption = desciption;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getIsShared() {
		return isShared;
	}

	public void setIsShared(String isShared) {
		this.isShared = isShared;
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

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}
}
