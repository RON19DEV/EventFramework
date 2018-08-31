package com.rohith.poc.eventbus.core.concurrency.pool;

import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

import com.rohith.poc.eventbus.api.channel.EventChannel;
import com.rohith.poc.eventbus.api.config.EventFramworkConcurrenyConfig;
import com.rohith.poc.eventbus.api.event.Event;
import com.rohith.poc.eventbus.api.exception.EventFrameworkException;
import com.rohith.poc.eventbus.core.bus.AbstractEventBus;
import com.rohith.poc.eventbus.core.concurrency.worker.EventProcessor;
import com.rohith.poc.eventbus.core.concurrency.worker.QueueMonitorThread;

/**
 * 
 * 
 * Monitored Worker Thread Pool is used in ASYNC SUNGLE QUEUE DESIGN
 * 
 * <p> Uses a Monitor Thread and Worker Threads Concept </p>
 * 
 * 
 * {@link QueueMonitorThread} , {@link EventProcessor}
 * 
 *  
 * 
 * @author Rohith Jaya Dev
 *
 */
public class MonitoredWorkerThreadPool implements EventFrameworkThreadPool {

	private EventFramworkConcurrenyConfig config;

	private QueueMonitorThread monitorThread;

	private EventProcessor[] eventProcessors;

	private BlockingQueue<Event> inputQueue;
	
	private AtomicBoolean isActive;

	public MonitoredWorkerThreadPool(EventFramworkConcurrenyConfig config, AbstractEventBus eventBus)
			throws EventFrameworkException {
		this.config = config;
		this.isActive= new AtomicBoolean(false);
		this.inputQueue = new ArrayBlockingQueue<Event>(config.getInputQueueSize());
		createWorkerThreads();
		createMonitorThread(eventBus.eventChannels());
	}

	@Override
	public void start() {

		for (int i = 0; i < this.eventProcessors.length; i++) {
			this.eventProcessors[i].start();
		}

		this.monitorThread.start();
		
		this.isActive.compareAndSet(false, true);
	}
	
	@Override
	public void submitEvent(Event event) throws EventFrameworkException {
	
			try {
				if(isActive.get()){
					
					this.inputQueue.put(event);
				}
			
			
			} catch (InterruptedException e) {
				
				throw new EventFrameworkException("Publishing thread interrupted while waiting on input Queue");
			}
	}

	@Override
	public void stop() {

		this.isActive.compareAndSet(true, false);
		
		this.inputQueue.clear();
		
		this.monitorThread.interrupt();

		for (int i = 0; i < this.eventProcessors.length; i++) {
				this.eventProcessors[i].interrupt();
		}

	
	}

	private void createMonitorThread(Map<String, EventChannel> map) throws EventFrameworkException {

		this.monitorThread = new QueueMonitorThread(this.eventProcessors, map, this.inputQueue);
	}

	private void createWorkerThreads() {

		this.eventProcessors = new EventProcessor[this.config.getNumWorkerThreads()];

		for (int i = 0; i < this.eventProcessors.length; i++) {

			this.eventProcessors[i] = new EventProcessor("Worker Thread : " + (i + 1));
		}

	}



}
