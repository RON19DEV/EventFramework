package com.rohith.poc.eventbus.core.concurrency.worker;

import java.util.Queue;

import com.rohith.poc.eventbus.api.event.Event;
import com.rohith.poc.eventbus.api.exception.EventFrameworkException;
import com.rohith.poc.eventbus.core.concurrency.pool.ConcurrencyPool;

/**
 * Worker Thread helps in polling events and publishing then asynchrnously
 * 
 * @author Rohith Jaya Dev
 *
 */
public class EnhanceEventProcessor extends Thread {

	
	private Queue<ConcurrencyPool> buffer;

	public EnhanceEventProcessor(String processorName, Queue<ConcurrencyPool> ringBuffer) {
		super(processorName);
		this.buffer = ringBuffer;
	}

	@Override
	public void run() {

		while (true) {
			if (isInterrupted()) {
				System.out.println("Enhanced Worker " + getName() + " Interuppted");
				break;
			}
			ConcurrencyPool poll = this.buffer.poll();
			if (null != poll) {
				this.buffer.add(poll);
				Event event = poll.getNextEvent();
				if (null != event) {
					try {
						System.out.println("Worker Thread " + Thread.currentThread().getName() + " retrieved" + event.toString());
						poll.getChannel().publishEvent(event);
					} catch (EventFrameworkException e) {
						System.out.println("Exception while publishing event");
					}
				}
			}
		}
		System.out.println("Worked Thread Exited");
	}
}
