package com.rohith.poc.eventbus.api.subscriber;

import com.rohith.poc.eventbus.api.event.Event;

/**
 * 
 * Abstract Event Subscriber implements common APIs like getType and message type
 * 
 * <p> Any Application wishing to implement the call back should extend this class </p>
 * 
 * @author Accolite
 *
 */
public abstract class AbstractEventSubscriber implements EventSubscriber<Event> {

	private SubscriberType type;
	
	private String messageType;

	public AbstractEventSubscriber(String messageType) {

		this.type = SubscriberType.NORMAL;
		
		this.messageType = messageType;
	}
	public SubscriberType getType() {

		return this.type;
	}
	
	public String subscribedMessageType(){
		
		
		return this.messageType;
	}

}
