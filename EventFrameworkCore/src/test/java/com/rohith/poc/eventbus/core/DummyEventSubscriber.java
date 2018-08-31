package com.rohith.poc.eventbus.core;

import com.rohith.poc.eventbus.api.event.Event;
import com.rohith.poc.eventbus.api.exception.EventFrameworkException;
import com.rohith.poc.eventbus.api.subscriber.AbstractEventSubscriber;
import com.rohith.poc.eventbus.api.subscriber.SubscriberType;

public class DummyEventSubscriber extends AbstractEventSubscriber {

	private int count;

	public DummyEventSubscriber(SubscriberType type, String messageType) {
		super(messageType);
		
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void onEvent(Event event) throws EventFrameworkException {
		
	
		count++;
		
		System.out.println("Subscriber  " + subscribedMessageType() + " Recieved only" + event.toString());
		
		
	}

	@Override
	public void onError(EventFrameworkException eventFrameworkException) {


		System.out.println("Error" + eventFrameworkException.getMessage());
		
	}
	
	public int count(){
		
		return count;
	}

}
