package aucklanduni.ece.hc.webservice.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import aucklanduni.ece.hc.util.Constants;

public class HCMessage implements Serializable{

	private static final long serialVersionUID = 2621571331747201741L;

	private String status;
	private HCMessageHeader header;
	private Object response;
	private String error;
	
	public HCMessage(){
		this.header = new HCMessageHeader();
	}
	public HCMessage setSuccess(){
		this.status = ""+Constants.REST_MESSAGE_STATUS_SUCCESS_CODE;
		return this;
	}
	public HCMessage setSuccess(Object response){
		this.status = ""+Constants.REST_MESSAGE_STATUS_SUCCESS_CODE;
		this.response = response;
		return this;
	}
	public HCMessage setFail(String error){
		return this.setFail(Constants.REST_MESSAGE_STATUS_FAIL_DEFAULT, error);
	}
	public HCMessage setFail(String status, String error){
		this.status = status;
		this.error = error;
		return this;
	}
	
	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}


	public HCMessageHeader getHeader() {
		return header;
	}


	public void setHeader(HCMessageHeader header) {
		this.header = header;
	}


	public Object getResponse() {
		return response;
	}


	public void setResponse(Object response) {
		this.response = response;
	}


	public String getError() {
		return error;
	}


	public void setError(String error) {
		this.error = error;
	}


	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}
	public static void main(String[] args){
		System.out.println(new HCMessage());
	}
}
