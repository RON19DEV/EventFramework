package com.rohith.poc.eventbus.api.subscriber;

import com.rohith.poc.eventbus.api.event.Event;
/**
 * Filtered Event Subscriber is the extension of the Event Subscriber Interface 
 * 
 * <p> Exposes an API applyFilter which should be implemented by the application </p>
 * 
 * <p> If the Event passes the filter criteria then it will be passed on to the Subscriber </p>
 * 
 * <p> Useful in filtering Symbol data based on contract code and price range and so on </p>
 * 
 * @author Rohith Jaya Dev
 *
 * @param <T>
 */
public interface FilteredEventSubscriber<T extends Event> extends EventSubscriber<Event> {

		public boolean applyFilter(T event);
	
}
