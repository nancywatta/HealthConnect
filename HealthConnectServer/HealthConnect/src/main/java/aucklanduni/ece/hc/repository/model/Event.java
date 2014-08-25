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
@Entity
@Table(name = "Event")
public class Event  implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID", unique = true, nullable = false, precision = 20, scale = 0)
	private long id;
	
	@Column(name = "event_type", nullable = false)
	private String eventType;
	
	@Column(name = "event_content")
	private String eventContent;
	
	@Column(name = "send_to")
	private String sendTo;
	
	@Column(name = "send_type")
	private String sendType;

	@Column(name = "send_time_scheduled")
	@Temporal(TemporalType.DATE)
	private Date sendTimeScheduled;

	@Column(name = "send_time_actural")
	@Temporal(TemporalType.DATE)
	private Date sendTimeActural;

	@Column(name = "send_result_type")
	private String sendResultType;

	@Column(name = "created_date")
	@Temporal(TemporalType.DATE)
	private Date createDate;
	
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
