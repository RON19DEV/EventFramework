package com.rohith.poc.eventbus.core.bus;

import com.rohith.poc.eventbus.api.config.EventFrameWorkConfig;
import com.rohith.poc.eventbus.api.event.Event;
import com.rohith.poc.eventbus.api.exception.EventFrameworkException;
import com.rohith.poc.eventbus.core.concurrency.pool.EventFrameworkThreadPool;
import com.rohith.poc.eventbus.core.concurrency.pool.MonitoredWorkerThreadPool;
import com.rohith.poc.eventbus.core.concurrency.pool.SelfServingWokerThreadPool;
/**
 * 
 * Represents the Asynchronous Version of the Event Bus 
 * 
 * <p> extends the Abstract Event Bus </p>
 * 
 * <p> Thread Pool Used varies as per the ASYNC Modes </p>
 * 
 * @see EventFrameworkThreadPool
 * 
 * @see AbstractEventBus
 *  * 
 * @author Rohith Jaya Dev 
 *
 */
public class AsynchronousEventBus extends AbstractEventBus {

	private EventFrameworkThreadPool threadPool;

	public AsynchronousEventBus(EventFrameWorkConfig config) throws EventFrameworkException {
		super(config);
		createAndStartThreadPool(config);
	}

	@Override
	public void publish(Event event) {

		if (!validateMessage(event)) {

			return;
		}

		try {
			this.threadPool.submitEvent(event);

		} catch (EventFrameworkException e) {

			System.out.println("Couldn't publish this event " + event.toString() + " due to " + e.getMessage());

		}
	}

	@Override
	public void shutDown() {
		super.shutDown();
		this.threadPool.stop();
	}

	private void createAndStartThreadPool(EventFrameWorkConfig config) throws EventFrameworkException {

		switch (config.getMode()) {
		case ASYNCMULTIQUEUE:
		case LIFO:
			this.threadPool = new SelfServingWokerThreadPool(config.getConcurrenyConfig(),
					config.getMessageTypeHandled(), this, config.getMode());
			break;
		case ASYNCSINGLEQUEUE:
			this.threadPool = new MonitoredWorkerThreadPool(config.getConcurrenyConfig(), this);
			break;
		default:
			this.threadPool = new SelfServingWokerThreadPool(config.getConcurrenyConfig(),
					config.getMessageTypeHandled(), this, config.getMode());
			break;
		}

		this.threadPool.start();
	}

}
