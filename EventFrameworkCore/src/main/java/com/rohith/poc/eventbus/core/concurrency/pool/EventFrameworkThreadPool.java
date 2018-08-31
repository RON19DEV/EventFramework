package com.rohith.poc.eventbus.core.concurrency.pool;

import com.rohith.poc.eventbus.api.event.Event;
import com.rohith.poc.eventbus.api.exception.EventFrameworkException;

/**
 * Base Interface for the Event Framework Thread Pool
 * 
 * @author Rohtih Jaya Dev
 *
 */
public interface EventFrameworkThreadPool {
	
	/**
	 * API for starting the thread pool
	 * 
	 */
	public void start();
	
	/**
	 * API for stoping the thread pool
	 * 
	 */
	public void stop();
	
	/**
	 * API for submitting a task to the thread pool
	 * 
	 * @param event
	 * @throws EventFrameworkException
	 */
	public void submitEvent(Event event) throws EventFrameworkException;
	
}
