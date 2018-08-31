package com.rohith.poc.eventbus.core.bus;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Predicate;

import com.rohith.poc.eventbus.api.EventBus;
import com.rohith.poc.eventbus.api.channel.EventChannel;
import com.rohith.poc.eventbus.api.config.EventFrameWorkConfig;
import com.rohith.poc.eventbus.api.config.EventMetaData;
import com.rohith.poc.eventbus.api.event.Event;
import com.rohith.poc.eventbus.api.exception.EventFrameworkException;
import com.rohith.poc.eventbus.api.subscriber.EventSubscriber;
import com.rohith.poc.eventbus.api.subscriber.FilteredEventSubscriber;
import com.rohith.poc.eventbus.api.subscriber.SubscriberType;
import com.rohith.poc.eventbus.core.channel.SynchronousEventChannel;
/**
 * 
 * Abstract Event Bus Which performs the Common Operations for both Synchronous and Asynchronous Event Bus
 * 
 * <p> It Uses a Registry Thread internally to help in registering subscribers </p>
 * 
 * <p> Exposed some extra APIs to be used by concrete implementations </p>
 * 
 * 			
 * @see SynchronousEventBus
 * 
 * @see AsynchronousEventBus			   
 * 
 * @author Rohith Jaya Dev
 *
 */
public abstract class AbstractEventBus implements EventBus {

	private static final int REGISTRY_THREAD_WAIT_TIME = 100;
	
	private EventFrameWorkConfig config;

	private BlockingQueue<EventSubscriber<Event>> subscriberRegistry;

	private AtomicBoolean isActive = null;

	private Map<String, EventChannel> channels = null;

	private Thread registryThread;

	public AbstractEventBus(EventFrameWorkConfig config) {

		this.config = config;

		this.isActive = new AtomicBoolean(false);

		this.subscriberRegistry = new LinkedBlockingQueue<EventSubscriber<Event>>(
				this.config.getSubscriberRegistryConfig().getSubscriberRegistryQueueSize());

		createMessageChannels();

		startRegisteryThread();
	}

	@Override
	public void addSubscriber(Class<?> clazz, EventSubscriber<Event> subscriber) {

		if (this.config.getSubscriberRegistryConfig().isSubscriberRegistryBlocking()) {

			try {
				this.subscriberRegistry.put(subscriber);
			} catch (InterruptedException e) {
				if (null != subscriber) {
					subscriber.onError(
							new EventFrameworkException("The Registering thread is interupted while waiting", e));
				}
			}

		} else {
			try {
				this.subscriberRegistry.offer(subscriber,
						this.config.getSubscriberRegistryConfig().getNonBlockingWaitTime(), TimeUnit.MILLISECONDS);
			} catch (InterruptedException e) {
				if (null != subscriber) {
					subscriber.onError(new EventFrameworkException(
							"The Registering thread is interupted while on timed waiting", e));
				}
			}
		}

	}
	
	@Override
	public void addSubscribersForFilteredEvents(FilteredEventSubscriber<Event> subscriber) {
	
		if(subscriber.getType()==SubscriberType.NORMAL){
			return;
		}
		addSubscriber(null, subscriber);
	}

	protected AtomicBoolean isActive(){
		
		return this.isActive;
	}
	
	protected EventChannel getChannel(String messageType){
		
		return this.channels.get(messageType);
	}
	
	public Map<String, EventChannel> eventChannels(){
		
		return this.channels;
	}
	
	private void createMessageChannels() {

		this.channels = new HashMap<String, EventChannel>(this.config.getMessageTypeHandled().size());

		this.config.getMessageTypeHandled().stream().forEach(i -> creatChannel(i));

	}

	private void creatChannel(EventMetaData i) {

		switch (config.getMode()) {

		case SYNC:
			channels.put(i.getEventType(), new SynchronousEventChannel(i));
			break;
		case ASYNCSINGLEQUEUE:
			channels.put(i.getEventType(), new SynchronousEventChannel(i));
			break;
		case ASYNCMULTIQUEUE:
			channels.put(i.getEventType(), new SynchronousEventChannel(i));
			break;
		case LIFO:
			channels.put(i.getEventType(), new SynchronousEventChannel(i));
			break;
		}

	}

	private void startRegisteryThread() {

		this.registryThread = new RegistryThread(this);

		this.registryThread.start();

		this.isActive = new AtomicBoolean(true);

	}

	@SuppressWarnings("unused")
	private static class RegistryThread extends Thread {

		private AbstractEventBus bus;

		public RegistryThread(AbstractEventBus bus) {

			this.bus = bus;
		}

		@Override
		public void run() {

			while (true) {
				
				System.out.println("Registry Thread waiting for registry");
			
				if (isInterrupted()) {
						break;
				}
				if (bus.isActive.get()) {
					
					try {
						EventSubscriber<Event> take = bus.subscriberRegistry.take();
						if (validateEventSubscriber(take)) {
							System.out.println("Registering Subscriber");
							EventChannel eventChannel = this.bus.channels.get(take.subscribedMessageType());
							switch (take.getType()) {
							case FILTERED:
								eventChannel.addSubscribersForFilteredEvents((FilteredEventSubscriber<Event>) take);
								break;
							case NORMAL:
								eventChannel.addSubscriber(take);
								break;
							}
						}
						Thread.sleep(REGISTRY_THREAD_WAIT_TIME);
					} catch (InterruptedException e) {
						System.out.println("Registry Thread intereuppted while for registry");
						break;
					}
				}

			}

			System.out.println(" Registry Thread Exited ");

		}

		private boolean validateEventSubscriber(EventSubscriber<Event> take) {

			if (null == take || null == take.getType() || null == take.subscribedMessageType()) {

				if (null != take) {

					take.onError(new EventFrameworkException(
							"Subsciber doesn't bind to the contract hence registry failed"));
				}

				return false;
			}

			Optional<EventMetaData> findFirst = this.bus.config.getMessageTypeHandled().stream()
					.filter(new Predicate<EventMetaData>() {

						@Override
						public boolean test(EventMetaData t) {

							return take.subscribedMessageType().equals(t.getEventType());
						}
					}).findFirst();

			if (!findFirst.isPresent()) {

				take.onError(new EventFrameworkException(
						"Event Bus is not configured to serve this message type, hence registry failed"));

				return false;
			}

			return true;
		}

	}

	protected boolean validateMessage(Event event) {
		return isActive().get() && null != event && null!= event.getEventType() && null != getChannel(event.getEventType());
	}
	@Override
	public void shutDown(){
		this.isActive.set(false);
		this.registryThread.interrupt();
	}
}
