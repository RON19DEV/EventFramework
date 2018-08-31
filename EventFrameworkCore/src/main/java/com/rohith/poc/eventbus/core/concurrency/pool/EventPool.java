package com.rohith.poc.eventbus.core.concurrency.pool;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.rohith.poc.eventbus.api.channel.EventChannel;
import com.rohith.poc.eventbus.api.event.Event;

/**
 * A normal implementation of the Event Pool
 * 
 * @author Rohith Jaya Dev
 *
 */
public class EventPool implements ConcurrencyPool{
		
	private BlockingQueue<Event> poolQueue;

	private Lock poolLock;
	
	private EventChannel eventChannel;
	
	private AtomicBoolean isActive;

	
	public EventPool(int poolSize, EventChannel eventChannel) {

		this.poolQueue = new ArrayBlockingQueue<Event>(poolSize, true);

		this.poolLock = new ReentrantLock(true);

		this.eventChannel = eventChannel;
		
		this.isActive = new AtomicBoolean(true);
	}
	
	/**
	 * Returns a null if the thread couldn't get a next value;
	 * 
	 * This Immediate return is designed not to make a worker thread wait on this
	 * 
	 * queue
	 * 
	 * @return
	 */
	@Override
	public Event getNextEvent() {

		Event currentEvent = null;

		if(!isActive.get()){
			
			Thread.currentThread().interrupt();
			
			return currentEvent;
		}
		
		boolean isAcquired = this.poolLock.tryLock();

		if (isAcquired) {
			try {
				currentEvent = poolQueue.poll(1000, TimeUnit.MILLISECONDS);
				} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				System.out.println("Worker Thread interrupted while waiting on the Poll Method in Queue");
			} finally {
				this.poolLock.unlock();
			}

		}
		return currentEvent;
	}

	@Override
	public void addEvent(Event event) {
		
		try {
			this.poolQueue.put(event);

		} catch (InterruptedException e) {

			System.out.println("Publisher Thread interrupted while trying to add event to pool queue");
		}
	}

	@Override
	public EventChannel getChannel(){
		
		return this.eventChannel;
	}
	
	@Override
	public void shutDownEventPool(){
		
		boolean compareAndSet = this.isActive.compareAndSet(true, false);
		if(compareAndSet){
			this.poolQueue.clear();
		}
		
	}
}
