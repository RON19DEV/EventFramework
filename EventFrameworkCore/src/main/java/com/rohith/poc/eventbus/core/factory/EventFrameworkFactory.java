package com.rohith.poc.eventbus.core.factory;

import java.util.Iterator;

import com.rohith.poc.eventbus.api.EventBus;
import com.rohith.poc.eventbus.api.config.EventFrameWorkConfig;
import com.rohith.poc.eventbus.api.config.EventFrameworkMode;
import com.rohith.poc.eventbus.api.config.EventFramworkConcurrenyConfig;
import com.rohith.poc.eventbus.api.config.EventMetaData;
import com.rohith.poc.eventbus.api.config.SubscriberRegistryConfig;
import com.rohith.poc.eventbus.api.exception.EventFrameworkException;
import com.rohith.poc.eventbus.core.bus.AsynchronousEventBus;
import com.rohith.poc.eventbus.core.bus.SynchronousEventBus;

public class EventFrameworkFactory {

	/**
	 * Helps in Creating a new Instance of the Event Bus based on the config
	 * 
	 * @param config
	 * @return
	 * @throws EventFrameworkException
	 */
	public static EventBus getEventBus(EventFrameWorkConfig config) throws EventFrameworkException {

		if (null == config) {

			throw new EventFrameworkException("null config");
		}

		if (null == config.getMode()) {

			throw new EventFrameworkException("Mode cannot be null");

		}

		validateRegistryConfig(config);

		validateConcurrencyConfig(config);

		validateEventMetaData(config);

		switch (config.getMode()) {

		case SYNC:
			return new SynchronousEventBus(config);
		case ASYNCMULTIQUEUE:
		case ASYNCSINGLEQUEUE:
		case LIFO:
			return new AsynchronousEventBus(config);
		default:
			return new SynchronousEventBus(config);
		}
	}

	private static void validateRegistryConfig(EventFrameWorkConfig config) throws EventFrameworkException {
		if (null == config.getSubscriberRegistryConfig()) {
			throw new EventFrameworkException("Subscription Registry can't be null");
		}

		SubscriberRegistryConfig sub = config.getSubscriberRegistryConfig();
		if (sub.isSubscriberRegistryBlocking() && sub.getNonBlockingWaitTime() <= 0) {

			sub.setNonBlockingWaitTime(SubscriberRegistryConfig.DEFAULT_NON_BLOCKING_TIMEOUT);
		}

		if (sub.getSubscriberRegistryQueueSize() <= 0) {

			sub.setSubscriberRegistryQueueSize(SubscriberRegistryConfig.DEFAULT_QUEUE_SIZE);
		}

		config.setSubscriberRegistryConfig(sub);
	}

	private static void validateConcurrencyConfig(EventFrameWorkConfig config) throws EventFrameworkException {
		if (config.getMode() != EventFrameworkMode.SYNC) {

			if (null == config.getConcurrenyConfig()) {

				throw new EventFrameworkException("Concurrency Config is null");
			}

			EventFramworkConcurrenyConfig concurrenyConfig = config.getConcurrenyConfig();

			if (concurrenyConfig.getNumWorkerThreads() <= 0) {

				concurrenyConfig.setNumWorkerThreads(EventFramworkConcurrenyConfig.DEFAULT_NUM_THREADS);
			}
			config.setConcurrenyConfig(concurrenyConfig);
		}
	}

	private static void validateEventMetaData(EventFrameWorkConfig config) throws EventFrameworkException {

		if (null == config.getMessageTypeHandled() || config.getMessageTypeHandled().isEmpty()) {

			throw new EventFrameworkException("Empty Message Set Found");
		}
		Iterator<EventMetaData> i = config.getMessageTypeHandled().iterator();

		while (i.hasNext()) {

			EventMetaData current = i.next();

			if (null == current.getEventType()) {

				i.remove();
			}
		}
	}

}
