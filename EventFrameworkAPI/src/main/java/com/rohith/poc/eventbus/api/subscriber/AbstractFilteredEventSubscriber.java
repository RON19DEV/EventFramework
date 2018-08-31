package com.rohith.poc.eventbus.api.subscriber;

import com.rohith.poc.eventbus.api.event.Event;
/**
 * 
 * Abstract Filtered Event Subscriber Class should be extended by any application who 
 * 
 * wishes to provide a Filtered Subscriber 
 * 
 * <p> Concrete Implementations must implement onEvent(), onError() and applyFilterMethods();
 * 
 * <p> applyFilteredMethod on this class always returns true it is important to implement it
 * 
 * @author Rohith Jaya Dev
 *
 */
public abstract class AbstractFilteredEventSubscriber implements FilteredEventSubscriber<Event>  {

	private SubscriberType type;

	private String messageType;
	
	public AbstractFilteredEventSubscriber(String messageType) {
		
		this.type = SubscriberType.FILTERED;
		
		this.messageType =messageType;
	}
	
	public String subscribedMessageType(){
		
		
		return this.messageType;
	}

	public SubscriberType getType(){
		
		return this.type;
	}

	/**
	 * Default Implementation of the filtered event always return true 
	 * 
	 */
	public boolean applyFilter(Event event) {
		
		return true;
	}

}
