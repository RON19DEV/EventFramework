package com.rohith.poc.eventbus.api.config;

/**
 * 
 * <p>
 * The ENUM that represent the mode of working of the event bus
 * </p>
 * 
 * <ul>
 * <li>SYNC - SYNCHRONOUS MODE  Single Thread Bus . (Publisher Thread Calls
 * the CallBack</li>
 * <li>ASYNCSINGLEQUEUE  Multi Threaded Mode . (Multiple Worker Threads and
 * 	   One Monitor Thread and Single Queue for all Event Types</li>
 * <li>ASYNCMULTIQUEUE  Multi Threaded Mode . (Multiple Workers Threads and Multiple Queues as per event types</li>
 * <li> LIFO - Multi Threaded LAST IN FIRST OUT MODE (Latest Event Only)  </li>
 * 
 * </ul>
 * 
 * 
 * @author Rohith Jayadev
 *
 */
public enum EventFrameworkMode {

	SYNC, ASYNCSINGLEQUEUE, ASYNCMULTIQUEUE, LIFO;
}
