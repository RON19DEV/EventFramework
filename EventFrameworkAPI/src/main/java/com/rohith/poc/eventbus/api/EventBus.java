package com.rohith.poc.eventbus.api;

import com.rohith.poc.eventbus.api.event.Event;
import com.rohith.poc.eventbus.api.subscriber.EventSubscriber;
import com.rohith.poc.eventbus.api.subscriber.FilteredEventSubscriber;

/**
 * Central Component of the Framework
 * 
 * <p> Exposes APIs to Publish an Event 
 * 		
 *     Add an Event Subscriber and Filtered Event Subscribers </p> 
 * 
 * 
 * <p> Implementations are com.rohith.poc.eventbus.core.bus.SynchronousEventBus
 * 		                   com.rohith.poc.eventbus.core.bus.AsynchronousEventBus
 * 						   
 * </p>
 * @author Rohith Jaya Dev
 *
 */
public interface EventBus {

	/**
	 * API for publishing an event
	 * 
	 * @param event
	 */
	public void publish(Event event);
	
	/**
	 * API for adding a normal subscriber
	 * 
	 * @param clazz
	 * @param subscriber
	 */
	public void addSubscriber(Class<?> clazz, EventSubscriber<Event> subscriber);
	
	/**
	 * 
	 * API for adding a Filtered Subscriber
	 * 
	 * @param subscriber
	 */
	public void addSubscribersForFilteredEvents(FilteredEventSubscriber<Event> subscriber);
	
	/**
	 * 
	 * Additional API exposed to shutdown the event bus after use. It is to shut down the pool of threads
	 * 
	 */
	public void shutDown();
}
