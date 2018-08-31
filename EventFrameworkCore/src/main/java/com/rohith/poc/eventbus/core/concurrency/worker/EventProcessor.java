package com.rohith.poc.eventbus.core.concurrency.worker;

import java.util.concurrent.atomic.AtomicBoolean;

import com.rohith.poc.eventbus.api.exception.EventFrameworkException;
/**
 * Worker Thread helps in polling events and publishing then asynchrnously
 * 
 * Used in ASYNC SINGLE QUEUE MODE
 * 
 * @author Rohith Jaya Dev
 */
public class EventProcessor extends Thread {


	private EventProcessorInput input;

	private EventProcessorInput currentValue;

	private AtomicBoolean isSet;

	public EventProcessor(String eventProcessorName) {
		super(eventProcessorName);
		this.isSet = new AtomicBoolean(false);
	}

	public boolean submitTask(EventProcessorInput input) throws EventFrameworkException {

		boolean hasSetBefore = isSet.get();
		if (!hasSetBefore) {
			this.input = input;
			isSet.set(true);
			return true;
		}
		return false;
	}

	@Override
	public void run() {

		while (true) {

			if (isInterrupted()) {
				System.out.println("Worker Thread has been interupted by the pool hence exiting ");
				break;
			}
			if (!isSet.get()) {
				continue;
			}
			this.currentValue = this.input;
			boolean setting = this.isSet.compareAndSet(true, false);
			if (!setting) {
				System.out.println("Something went wrong");
				continue;
			}
			try {
				process();
			} catch (EventFrameworkException e) {
				processError(e);
			}
		}

		System.out.println("Worker Exited");
		
	}

	private void process() throws EventFrameworkException {

		System.out.println("Worker :" + getName() + " processed " + this.currentValue.getEvent().toString());
		
		this.currentValue.getChannel().publishEvent(this.currentValue.getEvent());

	}

	private void processError(EventFrameworkException e) {

		System.out.println("Error");
	}

}
