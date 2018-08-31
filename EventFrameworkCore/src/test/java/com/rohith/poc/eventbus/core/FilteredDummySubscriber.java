package com.rohith.poc.eventbus.core;

import com.rohith.poc.eventbus.api.event.Event;
import com.rohith.poc.eventbus.api.exception.EventFrameworkException;
import com.rohith.poc.eventbus.api.subscriber.AbstractFilteredEventSubscriber;
import com.rohith.poc.eventbus.api.subscriber.SubscriberType;

public class FilteredDummySubscriber extends AbstractFilteredEventSubscriber {

	private int count;
	public FilteredDummySubscriber(SubscriberType type, String messageType) {
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

	/**
	 * Default Implementation of the filtered event always return true 
	 * 
	 */
	@Override
	public boolean applyFilter(Event event) {
		StringEvent string = (StringEvent) event;
		
		if(string.getData()>50){
			return true;
		}
		return false;
	}

	public int count(){
		
		return this.count;
	}

	
}
