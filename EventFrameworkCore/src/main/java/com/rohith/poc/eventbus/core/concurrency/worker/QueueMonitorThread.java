package com.rohith.poc.eventbus.core.concurrency.worker;

import java.util.Map;
import java.util.concurrent.BlockingQueue;

import com.rohith.poc.eventbus.api.channel.EventChannel;
import com.rohith.poc.eventbus.api.event.Event;
import com.rohith.poc.eventbus.api.exception.EventFrameworkException;

/**
 * QUEUE Monitor thread used in ASYNC SINGLE QUEUE MODE
 * 
 * <p> Delegates Work to the Worker Threads </p>
 * 
 * @author Rohith Jaya Dev
 */

public class QueueMonitorThread extends Thread {

	private Map<String, EventChannel> channelMap = null;

	private BlockingQueue<Event> inputQueue;

	private EventProcessor[] processors;

	public QueueMonitorThread(EventProcessor[] processors, Map<String, EventChannel> channelMap,
			BlockingQueue<Event> inputQueue) throws EventFrameworkException {

		super("Queue Monitor Thread");

		if (null == processors || processors.length == 0) {

			throw new EventFrameworkException("Process List is Empty");
		}

		this.processors = processors;

		if (null == inputQueue) {

			throw new EventFrameworkException("Input Queue Can't be null");
		}

		this.inputQueue = inputQueue;

		this.channelMap = channelMap;

	}

	@Override
	public void run() {

		int lastDispatchedThread = -1;

		while (true) {

			if (isInterrupted()) {

				System.out.println("Monitoring Queue is Interrupted hence Exiting operation.");

				break;
			}

			if (isInterrupted()) {

				System.out.println("Exited Here.");

				break;
			}

			Event take = this.inputQueue.poll();

			if (null == take) {
				continue;
			}

			EventChannel currentChannel = channelMap.get(take.getEventType());

			EventProcessorInput input = new EventProcessorInput(currentChannel, take);

			boolean hasDispatched = false;

			while (!hasDispatched) {

				if (isInterrupted()) {
					System.out.println("Monitoring Queue is Interrupted inside has dispached loop.");
					break;
				}

				int currentDisPatch = lastDispatchedThread + 1;
				if (currentDisPatch >= this.processors.length) {
					currentDisPatch = 0;
				}
				for (int i = currentDisPatch; i < this.processors.length; i++) {
					EventProcessor eventProcessor = this.processors[i];
					try {
						if (eventProcessor.submitTask(input)) {
							hasDispatched = true;
							lastDispatchedThread = currentDisPatch;
							System.out.println("Event : " + take.getEventType() + "  " + take.toString()
									+ " dispatched to" + eventProcessor.getName());
							break;
						}
					} catch (EventFrameworkException e) {
						System.out.println(
								"Exception occured while submitting task to the Event Processor. " + e.getMessage());
					}
				}

				if (isInterrupted()) {
					System.out.println("Monitoring Queue is Interrupted inside has dispached loop.");
					break;
				}

				if (hasDispatched) {
					continue;
				}

				for (int i = 0; i < currentDisPatch; i++) {

					EventProcessor eventProcessor = this.processors[i];
					try {
						if (eventProcessor.submitTask(input)) {
							hasDispatched = true;
							lastDispatchedThread = currentDisPatch;
							System.out.println("Event : " + take.getEventType() + "  " + take.toString()
									+ " dispatched to" + eventProcessor.getName());
							break;
						}
					} catch (EventFrameworkException e) {
						System.out.println(
								"Exception occured while submitting task to the Event Processor. " + e.getMessage());
					}
				}
			}

		}

		System.out.println("Monitor Exited");
	}

}
