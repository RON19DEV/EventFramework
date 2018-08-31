package com.rohith.poc.eventbus.core.bus;

import com.rohith.poc.eventbus.api.channel.EventChannel;
import com.rohith.poc.eventbus.api.config.EventFrameWorkConfig;
import com.rohith.poc.eventbus.api.event.Event;
import com.rohith.poc.eventbus.api.exception.EventFrameworkException;

/**
 * Synchronous Version of the Event Channel where the publisher calls the call back in the subscriber
 * 
 * 
 * @see AbstractEventBus
 * 
 * @author Rohith Jaya Dev
 *
 */
public class SynchronousEventBus extends AbstractEventBus {

	public SynchronousEventBus(EventFrameWorkConfig config) {
		super(config);

	}

	@Override
	public void publish(Event event) {
		
		if(validateMessage(event)){
				EventChannel channel  = getChannel(event.getEventType());				
				try {
			//		System.out.println("Pubisher publishing event " +  event.toString());
					channel.publishEvent(event);
				} catch (EventFrameworkException e) {
					System.out.println("Exception Occured while publishing event" + e.getMessage());
				}
				
		}else{
			System.out.println("Invalid Message Type or Channel Not Found");
		}
		
	}

	@Override
	public void shutDown() {
		super.shutDown();
	}

}
