package com.rohith.poc.eventbus.core.concurrency.pool;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

import com.rohith.poc.eventbus.api.channel.EventChannel;
import com.rohith.poc.eventbus.api.event.Event;
/**
 * LIFO implementation of the Concurrency Pool
 * 
 * @author Rohith Jaya Dev
 *
 */
public class LIFOEventPool implements ConcurrencyPool {

	private LIFOArray<Event> ringBuffer;

	private BlockingQueue<Event> poolQueue;

	private EventChannel eventChannel;

	private AtomicBoolean isActive;

	public LIFOEventPool(EventChannel eventChannel) {

		this.eventChannel = eventChannel;

		this.ringBuffer = new LIFOArray<Event>();

		this.isActive = new AtomicBoolean(true);
	}

	@Override
	public Event getNextEvent() {

		if (!this.isActive.get()) {

			return null;
		}
		return ringBuffer.read();
	}

	@Override
	public void addEvent(Event event) {

		if (!this.isActive.get()) {

			return;
		}

		this.ringBuffer.write(event);
	}

	@Override
	public EventChannel getChannel() {
		return this.eventChannel;
	}

	@Override
	public void shutDownEventPool() {

		boolean compareAndSet = this.isActive.compareAndSet(true, false);
		if (compareAndSet) {

			if (null != this.poolQueue)
				this.poolQueue.clear();
		}

	}

}
