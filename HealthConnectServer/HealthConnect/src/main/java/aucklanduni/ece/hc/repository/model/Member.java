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
* @ClassName: Member 
* @Description: This is an Entity class relating to actural database table
* Memeber is the reference table connecting Group and Account
* @author Zhao Yuan
* @date 2014年9月15日 下午9:06:44 
*
*CREATE TABLE `MEMBER` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `account_id` bigint(20) NOT NULL,
  `group_id` bigint(20) NOT NULL,
  `role_id` bigint(20) NOT NULL,
  `created_date` datetime DEFAULT NULL,
  `expiration_date` datetime DEFAULT NULL,
  `isActive` varchar(4) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `mem_acc_fk` (`account_id`),
  KEY `mem_group_fk` (`group_id`),
  CONSTRAINT `mem_group_fk` FOREIGN KEY (`group_id`) REFERENCES `group_info` (`id`),
  CONSTRAINT `mem_acc_fk` FOREIGN KEY (`account_id`) REFERENCES `account` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8
;
 */
@Entity
@Audited
@Table(name = "MEMBER")
public class Member  implements Serializable {

	private static final long serialVersionUID = -3683308074499371005L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID", unique = true, nullable = false, precision = 20, scale = 0)
	private long id;
	
	/**
	 * represents the account id of this memeber 
	 */
	@Column(name = "account_id", nullable = false, precision = 20, scale = 0)
	private long accountId;

	/**
	 * represents the group id of this memeber 
	 */
	@Column(name = "group_id", nullable = false, precision = 20, scale = 0)
	private long groupId;

	/**
	 * represents the role id of this memeber 
	 */
	@Column(name = "role_id", nullable = false, precision = 20, scale = 0)
	private long roleId;

	/**
	 * create date of this record
	 */
	@Column(name = "created_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createDate;
	
	/**
	 * expired date of this record
	 */
	@Column(name = "expiration_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date expirationDate;
	
	/**
	 * whether this member is active or not
	 */
	@Column(name = "isActive", nullable = false)
	private String isActive;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
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

	public long getRoleId() {
		return roleId;
	}

	public void setRoleId(long roleId) {
		this.roleId = roleId;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	
	public Date getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}
	
	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}
}
