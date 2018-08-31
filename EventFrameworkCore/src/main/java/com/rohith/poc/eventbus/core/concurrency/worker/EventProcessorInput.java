package com.rohith.poc.eventbus.core.concurrency.worker;

import com.rohith.poc.eventbus.api.channel.EventChannel;
import com.rohith.poc.eventbus.api.event.Event;

public class EventProcessorInput {

	private EventChannel channel;
	
	private Event  event;
	
	public EventProcessorInput(EventChannel channel , Event event){
		
		this.channel = channel;
		
		this.event=event;
	}

	public EventChannel getChannel() {
		return channel;
	}

	public Event getEvent() {
		return event;
	}
	
	
	
}
