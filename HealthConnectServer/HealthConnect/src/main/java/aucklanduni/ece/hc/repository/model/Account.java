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
import javax.persistence.Transient;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.hibernate.envers.Audited;
/**
 * 
* @ClassName: Account 
* @Description: This is an Entity class relating to actural database table 
* Account object is to contain information of application users
* @author Zhao Yuan
* @date 2014年9月15日 下午8:58:46 
*
** CREATE TABLE `ACCOUNT` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `email` varchar(64) NOT NULL,
  `username` varchar(64) DEFAULT NULL,
  `password` varchar(64) DEFAULT NULL,
  `created_date` datetime DEFAULT NULL,
  `updated_date` datetime DEFAULT NULL,
  `last_login_date` datetime DEFAULT NULL,
  `expiration_date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8
;
 */
@Entity
@Audited
@Table(name = "ACCOUNT")
public class Account implements Serializable {

	private static final long serialVersionUID = -251853810742604601L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID", unique = true, nullable = false, precision = 20, scale = 0)
	private long id;
	
	/**
	 * Email is the unique value represents user's email address. 
	 * It should not be null.
	 */
	@Column(name = "email", unique = true, nullable = false)
	private String email;

	/**
	 * Username represents user's nickname/shown name in this application.
	 * If its null, UI should show its email address. 
	 */
	@Column(name = "username")
	private String username;

	/**
	 * Password is for users to use in case when they want to change 
	 * personal information. 
	 */
	@Column(name = "password")
	private String password;

	/**
	 * CreateDate is to record the datetime when the record first being created.
	 */
	@Column(name = "created_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createDate;

	/**
	 * UpdateDate is to record every time when this record being updated.
	 */
	@Column(name = "updated_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date updatedDate;

	/**
	 * last login date is to save last time when user loged in.
	 * This value should be updated after user logout
	 * or UI's session expires.
	 */
	@Column(name = "last_login_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastLoginDate;
	
	/**
	 * This is the datetime field used to save the datetime when a user delete his account.
	 * The application wont delete a record, instead just expire it.
	 */
	@Column(name = "expiration_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date expirationDate;

	/**
	 * It represents the role of this account.
	 * This Transient field wont be saved in db.
	 * TODO this field should be deleted once we have viewobjects done
	 * coz right now its only for the sake of showing role in android layout.
	 */
	@Transient
	private Dictionary role;
	
	/**
	 * It represents the memberDetails of this account.
	 * This Transient field wont be saved in db.
	 * TODO this field should be deleted once we have viewobjects done
	 * coz right now its only for the sake of showing role in android layout.
	 */
	@Transient
	private Member memberDetails;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
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

	public Date getLastLoginDate() {
		return lastLoginDate;
	}

	public void setLastLoginDate(Date lastLoginDate) {
		this.lastLoginDate = lastLoginDate;
	}
	
	public void setRole(Dictionary role) {
		this.role = role;
	}
	
	public Dictionary getRole() {
		return role;
	}
	
	public Date getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}

	public Member getMemberDetails() {
		return memberDetails;
	}

	public void setMemberDetails(Member memberDetails) {
		this.memberDetails = memberDetails;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}
	
}
