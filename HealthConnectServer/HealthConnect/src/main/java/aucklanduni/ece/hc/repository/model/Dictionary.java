package aucklanduni.ece.hc.repository.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
/**
 * 
* @ClassName: Dictionary 
* @Description: This is an Entity class relating to actural database table
* Dictionary refers to static system parameters
* Its a data dictionary.
* @author Zhao Yuan
* @date 2014年9月15日 下午9:02:34 
*
*CREATE TABLE `DICTIONARY` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `type` varchar(20) NOT NULL,
  `value` varchar(20) DEFAULT NULL,
  `name` varchar(64) NOT NULL,
  `description` varchar(256) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8
;
 */
@Entity
@Table(name = "DICTIONARY")
public class Dictionary  implements Serializable{

	private static final long serialVersionUID = 5222783567462206743L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID", unique = true, nullable = false, precision = 20, scale = 0)
	private long id;
	
	/**
	 * data type 
	 */
	@Column(name = "type", nullable = false)
	private String type;
	
	/**
	 * data value for each name
	 */
	@Column(name = "value")
	private String value;

	/**
	 * data name
	 */
	@Column(name = "name",nullable = false)
	private String name;

	/**
	 * short desc for each record
	 */
	@Column(name = "description")
	private String description;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}
}
