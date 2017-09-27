package com.binaryfoundain.poc.model;

import java.beans.Transient;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Aircraft {
	
	public Aircraft(){}
	
	public Aircraft(String id, String type, String size){
		this.id = id;
		this.type = type;
		this.size = size;
		this.enteredTime = new Date();
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.enteredTime = new Date(); // time the AC entered in to system
		this.id = id;
	}

	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getSize() {
		return size;
	}
	public void setSize(String size) {
		this.size = size;
	}
	
	@Transient
	public long getEntered(){
		return enteredTime.getTime();
	}
	
	@JsonProperty("id")
	private String id;
	
	@JsonProperty("type")
	private String type;
	
	@JsonProperty("size")
	private String size;
	
	private Date enteredTime;
}
