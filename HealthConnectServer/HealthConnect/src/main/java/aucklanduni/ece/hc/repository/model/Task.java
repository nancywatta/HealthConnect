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
/**
 * 
* @ClassName: Task 
* @Description: This is an Entity class relating to actural database table
* Task means TO_DO things 
* TO_DO list contains not only appointment but also taking pills, doing excercise
* having special diet and so on.
* TODO should be reconsidered coz right now its a bit duplicated with appointment
* also should know more detailed logic about patients daily routine 
* apart from appointment management
* @author Zhao Yuan
* @date 2014年9月15日 下午9:07:17 
*
*CREATE TABLE `TASK` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `account_id` bigint(20) NOT NULL,
  `name` varchar(64) NOT NULL,
  `start_date` datetime DEFAULT NULL,
  `end_date` datetime DEFAULT NULL,
  `execute_time` datetime DEFAULT NULL,
  `isValid` varchar(4) DEFAULT NULL,
  `isShared` varchar(4) DEFAULT NULL,
  `desciption` varchar(256) DEFAULT NULL,
  `created_date` datetime DEFAULT NULL,
  `updated_date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `task_acc_fk` (`account_id`),
  CONSTRAINT `task_acc_fk` FOREIGN KEY (`account_id`) REFERENCES `account` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8
;
 */
@Entity
@Table(name = "TASK")
public class Task implements Serializable {

	private static final long serialVersionUID = -1888663488419982146L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID", unique = true, nullable = false, precision = 20, scale = 0)
	private long id;
	
	/**
	 * name of a task
	 */
	@Column(name = "name", nullable = false)
	private String name;

	/**
	 * start date of a task
	 */
	@Column(name = "start_date")
	@Temporal(TemporalType.DATE)
	private Date startDate;
	
	/**
	 * end date of a task
	 */
	@Column(name = "end_date")
	@Temporal(TemporalType.DATE)
	private Date endDate;

	/**
	 * execute time of a task during start date and end date
	 */
	@Column(name = "execute_time")
	@Temporal(TemporalType.DATE)
	private Date executeTime;
	
	/**
	 * whether a task is valid
	 */
	@Column(name = "isValid")
	private String isValid;
	
	/**
	 * whether a task is opened to group
	 */
	@Column(name = "isShared")
	private String isShared;
	
	/**
	 * short description of a task
	 */
	@Column(name = "desciption")
	private String desciption;

	/**
	 * create date of a task
	 */
	@Column(name = "created_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createDate;
	
	/**
	 * updated date of a task
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

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Date getExecuteTime() {
		return executeTime;
	}

	public void setExecuteTime(Date executeTime) {
		this.executeTime = executeTime;
	}

	public String getIsValid() {
		return isValid;
	}

	public void setIsValid(String isValid) {
		this.isValid = isValid;
	}

	public String getIsShared() {
		return isShared;
	}

	public void setIsShared(String isShared) {
		this.isShared = isShared;
	}

	public String getDesciption() {
		return desciption;
	}

	public void setDesciption(String desciption) {
		this.desciption = desciption;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

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
