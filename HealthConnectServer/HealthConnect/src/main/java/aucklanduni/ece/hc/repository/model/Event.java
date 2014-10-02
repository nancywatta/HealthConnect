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
* @ClassName: Event 
* @Description: This is an Entity class relating to actural database table
* Event means activities happened in this application
* This table is actually designed to be used by notification server i.e. email server
* There will be a cronjob or scheduled task in this application to trigger notification server
* or to scan this table and send whatever message that need to be sent to email server
* and write back the actual sending result.
* It has nothing to do with the business logic.
* @author Zhao Yuan
* @date 2014年9月15日 下午9:03:32 
*
*CREATE TABLE `EVENT` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `event_type` varchar(4) NOT NULL,
  `event_content` varchar(256) DEFAULT NULL,
  `send_to` varchar(256) DEFAULT NULL,
  `send_type` varchar(4) NOT NULL,
  `send_time_scheduled` datetime DEFAULT NULL,
  `send_time_actural` datetime DEFAULT NULL,
  `send_result_type` varchar(4)  DEFAULT NULL,
  `created_date` datetime DEFAULT NULL,
  `updated_date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8
;
 */
@Entity
@Table(name = "Event")
public class Event  implements Serializable {

	private static final long serialVersionUID = 7533019585900119026L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID", unique = true, nullable = false, precision = 20, scale = 0)
	private long id;
	
	/**
	 * event type : appintment event; medical event; daily exercise event and so on.
	 */
	@Column(name = "event_type", nullable = false)
	private String eventType;
	
	/**
	 * the content sent to user to read.
	 * It can contain html charactors.
	 */
	@Column(name = "event_content")
	private String eventContent;
	
	/**
	 * address used by notification server to know where to send
	 */
	@Column(name = "send_to")
	private String sendTo;
	
	/**
	 * send type: email; message; notification
	 */
	@Column(name = "send_type")
	private String sendType;

	/**
	 *  next send time in a cron job espression
	 */
	@Column(name = "send_time_scheduled")
	@Temporal(TemporalType.DATE)
	private Date sendTimeScheduled;

	/**
	 *  actual send time in a cron job espression
	 *  will be wrote after getting the feedback from email server.
	 */
	@Column(name = "send_time_actural")
	@Temporal(TemporalType.DATE)
	private Date sendTimeActural;

	/**
	 *  actual send result
	 *  could be failed or success
	 */
	@Column(name = "send_result_type")
	private String sendResultType;

	/**
	 *  record the datetime when its created
	 */
	@Column(name = "created_date")
	@Temporal(TemporalType.DATE)
	private Date createDate;
	
	/**
	 *  record the datetime when its updated
	 */
	@Column(name = "updated_date")
	@Temporal(TemporalType.DATE)
	private Date updatedDate;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getEventType() {
		return eventType;
	}

	public void setEventType(String eventType) {
		this.eventType = eventType;
	}

	public String getEventContent() {
		return eventContent;
	}

	public void setEventContent(String eventContent) {
		this.eventContent = eventContent;
	}

	public String getSendTo() {
		return sendTo;
	}

	public void setSendTo(String sendTo) {
		this.sendTo = sendTo;
	}

	public String getSendType() {
		return sendType;
	}

	public void setSendType(String sendType) {
		this.sendType = sendType;
	}

	public Date getSendTimeScheduled() {
		return sendTimeScheduled;
	}

	public void setSendTimeScheduled(Date sendTimeScheduled) {
		this.sendTimeScheduled = sendTimeScheduled;
	}

	public Date getSendTimeActural() {
		return sendTimeActural;
	}

	public void setSendTimeActural(Date sendTimeActural) {
		this.sendTimeActural = sendTimeActural;
	}

	public String getSendResultType() {
		return sendResultType;
	}

	public void setSendResultType(String sendResultType) {
		this.sendResultType = sendResultType;
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
