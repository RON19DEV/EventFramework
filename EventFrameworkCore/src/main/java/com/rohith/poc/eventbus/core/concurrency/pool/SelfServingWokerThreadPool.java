package com.rohith.poc.eventbus.core.concurrency.pool;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

import com.rohith.poc.eventbus.api.channel.EventChannel;
import com.rohith.poc.eventbus.api.config.EventFrameworkMode;
import com.rohith.poc.eventbus.api.config.EventFramworkConcurrenyConfig;
import com.rohith.poc.eventbus.api.config.EventMetaData;
import com.rohith.poc.eventbus.api.event.Event;
import com.rohith.poc.eventbus.api.exception.EventFrameworkException;
import com.rohith.poc.eventbus.core.bus.AbstractEventBus;
import com.rohith.poc.eventbus.core.concurrency.worker.EnhanceEventProcessor;
import com.rohith.poc.eventbus.core.concurrency.worker.EventProcessor;
/**
 * 
 * This Thread Pool is used for ASYNC MULTI QUEUE and LIFO Mode
 * 
 * <p> Uses a bunch of worker threads for delegating process </p>
 * 
 * {@link EnhanceEventProcessor}
 * 
 * @author Rohith Jaya Dev
 *
 */
public class SelfServingWokerThreadPool implements EventFrameworkThreadPool {

	private EventFramworkConcurrenyConfig conig;

	private AtomicBoolean isActive;

	private Map<String, ConcurrencyPool> eventPoolMap;

	private Queue<ConcurrencyPool> ringBuffer;

	private EnhanceEventProcessor[] eventProcessors;

	public SelfServingWokerThreadPool(EventFramworkConcurrenyConfig concurrenyConfig, Set<EventMetaData> eventMetadatas,
			AbstractEventBus eventBus, EventFrameworkMode mode) {

		this.conig = concurrenyConfig;

		this.isActive = new AtomicBoolean(false);

		createMessagePools(eventBus.eventChannels(), eventMetadatas, mode);

		createWorkerThreads();

	}

	@Override
	public void start() {

		if (!this.isActive.get()) {

			this.isActive.set(true);

			for (int i = 0; i < this.eventProcessors.length; i++) {
				this.eventProcessors[i].start();
			}

		}

	}

	@Override
	public void stop() {

		boolean compareAndSet = this.isActive.compareAndSet(true, false);

		if (compareAndSet) {

			for (int i = 0; i < this.eventProcessors.length; i++) {
				this.eventProcessors[i].interrupt();
			}

			for (ConcurrencyPool eventPool : this.ringBuffer) {

				eventPool.shutDownEventPool();
			}
		}

	}

	@Override
	public void submitEvent(Event event) throws EventFrameworkException {

		if (!this.isActive.get()) {

			System.out.println(" Thread pool is ShutDown ");
			return;
		}

		ConcurrencyPool eventPool = this.eventPoolMap.get(event.getEventType());

		if (null != eventPool) {

			eventPool.addEvent(event);
		}

	}

	private void createMessagePools(Map<String, EventChannel> eventChannels, Set<EventMetaData> eventMetadatas,
			EventFrameworkMode mode) {

		int bufferSize = eventMetadatas.size();

		this.eventPoolMap = new HashMap<String, ConcurrencyPool>(bufferSize);

		this.ringBuffer = new ConcurrentLinkedQueue<ConcurrencyPool>();

		for (EventMetaData eventMetaData : eventMetadatas) {

			ConcurrencyPool pool = null;

			if (mode == EventFrameworkMode.ASYNCMULTIQUEUE) {
				pool = new EventPool(eventMetaData.getQueueSize(), eventChannels.get(eventMetaData.getEventType()));
			} else {

				pool = new LIFOEventPool(eventChannels.get(eventMetaData.getEventType()));
			}

			this.ringBuffer.add(pool);

			this.eventPoolMap.put(eventMetaData.getEventType(), pool);
		}
	}

	private void createWorkerThreads() {

		int numWorkerThreads = this.conig.getNumWorkerThreads();

		this.eventProcessors = new EnhanceEventProcessor[numWorkerThreads];

		for (int i = 0; i < numWorkerThreads; i++) {

			this.eventProcessors[i] = new EnhanceEventProcessor("Worker Thread : " + (i + 1), this.ringBuffer);
		}

	}

}
