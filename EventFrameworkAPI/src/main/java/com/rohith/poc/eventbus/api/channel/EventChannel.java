package com.rohith.poc.eventbus.api.channel;

import com.rohith.poc.eventbus.api.event.Event;
import com.rohith.poc.eventbus.api.exception.EventFrameworkException;
import com.rohith.poc.eventbus.api.subscriber.EventSubscriber;
import com.rohith.poc.eventbus.api.subscriber.FilteredEventSubscriber;
/**
 * 
 * Event Channels are Virtual Channels which represents a tunnel to subscribers 
 * 
 * <p> It is created per message type served by the event bus </p>
 * 
 * <p> Only Implementation provided is the Synchronous Channel </p>
 * 
 * @author Rohith Jaya Dev
 *
 */
public interface EventChannel {

	/**
	 * Helps in publishing event
	 * 
	 * @param e
	 * @throws EventFrameworkException
	 */
	public void publishEvent(@SuppressWarnings("rawtypes") Event e) throws EventFrameworkException;
	/**
	 * Adding a subscriber
	 * 
	 * @param subscriber
	 */
	public void addSubscriber(EventSubscriber<Event> subscriber);
	
	/**
	 * Addding a Filtered Subscriber
	 * 
	 * @param subscriber
	 */
	public void addSubscribersForFilteredEvents(FilteredEventSubscriber<Event> subscriber);
	
	
	
}
