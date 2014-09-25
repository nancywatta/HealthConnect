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

	@Column(name = "time", nullable = false)
	private Date time;
	
	@Column(name = "location")
	private String location;
	
	@Column(name = "desciption")
	private String desciption;

	@Column(name = "status")
	private String status;
	
	@Column(name = "isShared")
	private String isShared;
	
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
