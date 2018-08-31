package com.rohith.poc.eventbus.api.subscriber;

import com.rohith.poc.eventbus.api.event.Event;
import com.rohith.poc.eventbus.api.exception.EventFrameworkException;

/**
 * 
 * <p>
 * Call Back Provided by the Event Framework
 * </p>
 * 
 * <p>
 * Event will be published by the Event Framework to the <b> onEvent() </b>
 * Method
 * </p>
 * 
 * <p>
 * Errors will be published by the Event Framework to the <b> onError() </b>
 * Method
 * </p>
 * 
 * <p>
 * Any Implementation must provide the Event Type String that it subscribes to
 * and the type or else it will be ignored at registry
 * </p>
 * 
 * @see SubscriberType
 * 
 * @author Rohith Jayadev
 *
 * @param <T>
 */
public interface EventSubscriber<T extends Event> {

	public SubscriberType getType();

	public String subscribedMessageType();

	public void onEvent(T event) throws EventFrameworkException;

	public void onError(EventFrameworkException eventFrameworkException);

}
