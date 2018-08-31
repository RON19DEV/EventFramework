package com.rohith.poc.eventbus.core.channel;

import com.rohith.poc.eventbus.api.channel.EventChannel;
import com.rohith.poc.eventbus.api.config.EventMetaData;
import com.rohith.poc.eventbus.api.event.Event;

/**
 * <p>Synchronous Implementation of the Event Bus where each subscriber by the
 * publishing thread is called in a synchronous way.</p>
 * 
 * @see AbstractEventChannel
 * 
 * @see EventChannel
 * 
 * 
 * @author Rohith Jayadev
 *
 */
public class SynchronousEventChannel extends AbstractEventChannel {

	public SynchronousEventChannel(EventMetaData eventMetaData) {
		super();
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void publishEvent(Event e) {
		publishToNormalSubscribers(getEventSubscribers().iterator(), e);
		publishToFilteredSubscribers(getFilteredEventSubscribers().iterator(), e);
	}

}
