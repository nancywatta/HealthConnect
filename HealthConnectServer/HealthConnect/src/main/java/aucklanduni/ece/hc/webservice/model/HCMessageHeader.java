package aucklanduni.ece.hc.webservice.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import aucklanduni.ece.hc.util.Constants;
/**
 * 
* @ClassName: HCMessageHeader 
* @Description: This is the header for HCMessage
* Note that timestamp usually is to control the validation of the information
* @author Zhao Yuan
* @date 2014年9月15日 下午9:25:51 
*
 */
public class HCMessageHeader  implements Serializable{

	private static final long serialVersionUID = 428374590978195708L;

	private final String version = Constants.REST_API_VERSION;
	private long timestamp;
	
	public HCMessageHeader(){
		this.timestamp = System.currentTimeMillis();
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public String getVersion() {
		return version;
	}
	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}
	
}
