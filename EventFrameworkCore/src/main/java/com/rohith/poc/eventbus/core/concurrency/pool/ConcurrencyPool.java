package com.rohith.poc.eventbus.core.concurrency.pool;

import java.util.concurrent.ArrayBlockingQueue;

import com.rohith.poc.eventbus.api.channel.EventChannel;
import com.rohith.poc.eventbus.api.event.Event;
/**
 * 
 * Represents a Event Pool used by the Framework Thread Pool
 * 
 * <p> LIFO Mode uses the LIFO Pool backed by a special LIFO Array Data Structure {@link LIFOArray}</p>
 * 
 * <p> ASYNC MULTI QUEUED MODE uses a Normal Event Pool backed by a ArrayBlockingQueue {@linkplain ArrayBlockingQueue}</p>
 * 
 * 
 * @author Rohith Jaya Dev
 *
 */
public interface ConcurrencyPool {

	/**
	 * API for getting the next event from the buffer 
	 * @return
	 */
	public Event getNextEvent();
	
	/**
	 * API for adding an event to the buffer
	 * 
	 * @param event
	 */
	public void addEvent(Event event);
	
	/**
	 * API for the corresponding Channel
	 * 
	 * @return
	 */
	public EventChannel getChannel();
	
	/**
	 * API for pool shutdown
	 * 
	 */
	public void shutDownEventPool();
}
