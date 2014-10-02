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
* @ClassName: ApnUser 
* @Description: This is an Entity class relating to actural database table
* ApnUser is to contain Push Notification Users.
* Note that right now this Push Notification Server hasnt been used in this app
* this table is teporaly useless. But As design it will contain mobile phone's info
* so as to push messages to the clients. 
* @author Zhao Yuan
* @date 2014年9月15日 下午8:59:50 
*
*CREATE TABLE `APN_USER` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_date` datetime DEFAULT NULL,
  `email` varchar(64) DEFAULT NULL,
  `name` varchar(64) DEFAULT NULL,
  `password` varchar(64) DEFAULT NULL,
  `updated_date` datetime DEFAULT NULL,
  `username` varchar(64) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8
;
 */
@Entity
@Table(name = "APN_USER")
public class ApnUser  implements Serializable {

	private static final long serialVersionUID = 4473541468725878542L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID", unique = true, nullable = false, precision = 20, scale = 0)
	private long id;
	
	/**
	 * create date is to contain the datetime when this record was firstly created.
	 */
	@Column(name = "created_date")
	@Temporal(TemporalType.DATE)
	private Date createDate;
	
	/**
	 * email means users email address to distinguish among users.
	 */
	@Column(name = "email")
	private String email;
	
	/**
	 * name is push notification users real name
	 */
	@Column(name = "name")
	private String name;
	
	/**
	 * passward is for users to update their information or to get permittion
	 * when connectting to push server.
	 */
	@Column(name = "password")
	private String password;
	
	/**
	 * record datetime for every update action 
	 */
	@Column(name = "updated_date")
	@Temporal(TemporalType.DATE)
	private Date updatedDate;
	
	/**
	 * This is the nickname/showing name of this user in application
	 */
	@Column(name = "username", unique = true, nullable = false)
	private String username;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}
	
	
}
