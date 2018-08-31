package com.rohith.poc.eventbus.core;

import com.rohith.poc.eventbus.api.event.Event;

public class StringEvent implements Event {

	private String eventType;

	private int data;
	
	public  StringEvent(int data) {
		this.data=data;
		this.eventType="SGX-MD";
	}
	
	public  StringEvent(int data, String dataType) {
		this.data=data;
		this.eventType=dataType;
	}
	
	@Override
	public String getEventType() {

		return eventType;
	}

	public int getData(){

		return data;
	}

	@Override
	public String toString() {
		return "StringEvent [eventType=" + eventType + ", data=" + data + "]";
	}
	
	

}
