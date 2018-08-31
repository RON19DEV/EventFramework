package com.rohith.poc.eventbus.core;

import java.util.HashSet;
import java.util.Set;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.rohith.poc.eventbus.api.EventBus;
import com.rohith.poc.eventbus.api.config.EventFrameWorkConfig;
import com.rohith.poc.eventbus.api.config.EventFrameworkMode;
import com.rohith.poc.eventbus.api.config.EventFramworkConcurrenyConfig;
import com.rohith.poc.eventbus.api.config.EventMetaData;
import com.rohith.poc.eventbus.api.config.SubscriberRegistryConfig;
import com.rohith.poc.eventbus.api.exception.EventFrameworkException;
import com.rohith.poc.eventbus.api.subscriber.SubscriberType;
import com.rohith.poc.eventbus.core.bus.AsynchronousEventBus;
import com.rohith.poc.eventbus.core.factory.EventFrameworkFactory;


/**
 * 
 * <b>Asynchronous Multi-Queued Method Test </b>
 * 
 * <p>
 * In this test case we are creating two subscribers who subscribe two different
 * event types SGX-MD and DGCX-MD </p>
 * <ul>
 * <li>An Bus is created using the config and in the config the mode is ASYNC
 * MULTI QUEUED</li>
 * 
 * <li>The Threads inside the Test methods acts as two separate publishers who
 * publish events of the above types.</li>
 * 
 * <li>The event bus workers take the events from a event level queue and pass
 * it to the corresponding subscribers</li>
 * 
 * <li>As you can see in the code a subscriber is being added dynamically</li>
 * 
 * <li>Registration is done by a separate thread in side the Event bus</li>
 * </ul>
 * 
 * 
 * @see AsynchronousEventBus
 * 
 * @author Rohith Jayadev
 *
 */
public class AsynchronousMultiQueuedModeTest {

	static EventFrameWorkConfig config = null;

	static EventBus eventBus;

	@BeforeClass
	public static void init() {

		config = new EventFrameWorkConfig();

		createConfig(config);
	}


	@Test
	public void test() {

		try {
			eventBus = EventFrameworkFactory.getEventBus(config);
		} catch (EventFrameworkException e1) {
		
			e1.printStackTrace();
		}

		DummyEventSubscriber eventSubscriber = new DummyEventSubscriber(SubscriberType.NORMAL, "SGX-MD");
		eventBus.addSubscriber(DummyEventSubscriber.class, eventSubscriber);

		try {

			Thread.sleep(10000);

		} catch (InterruptedException e) {

			e.printStackTrace();
		}

		Thread t = new Thread(new Runnable() {

			@Override
			public void run() {
				for (int i = 0; i <= 10; i++) {

					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {

						e.printStackTrace();
					}
					if (i % 2 == 0) {

						eventBus.publish(new StringEvent(i, "SGX-MD"));
					} else {

						eventBus.publish(new StringEvent(i, "DGCX-MD"));
					}

				}

				System.out.println("This Thread exiting");
			}
		});

		t.start();

		Thread t1 = new Thread(new Runnable() {

			@Override
			public void run() {
				for (int i = 11; i <= 20; i++) {

					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {

						e.printStackTrace();
					}
					if (i % 2 == 0) {

						eventBus.publish(new StringEvent(i, "SGX-MD"));
					} else {

						eventBus.publish(new StringEvent(i, "DGCX-MD"));
					}

				}

				System.out.println("This Thread exiting");
			}
		});

		t1.start();

		try {

			Thread.sleep(100);

			eventSubscriber = new DummyEventSubscriber(SubscriberType.NORMAL, "DGCX-MD");
			eventBus.addSubscriber(DummyEventSubscriber.class, eventSubscriber);

			t.join();
			t1.join();
		} catch (InterruptedException e) {

			e.printStackTrace();
		}
		System.out.println("Main Exiting");
	}

	@AfterClass
	public static void destroy() {

		eventBus.shutDown();

	}

	private static void createConfig(EventFrameWorkConfig config) {
		Set<EventMetaData> messageTypeHandled = new HashSet<EventMetaData>(1);

		EventMetaData metaData = new EventMetaData();
		metaData.setEventType("SGX-MD");
		messageTypeHandled.add(metaData);

		metaData = new EventMetaData();
		metaData.setEventType("DGCX-MD");
		messageTypeHandled.add(metaData);

		config.setMessageTypeHandled(messageTypeHandled);
		config.setMode(EventFrameworkMode.ASYNCMULTIQUEUE);
		SubscriberRegistryConfig subscriberRegistryConfig = new SubscriberRegistryConfig();
		subscriberRegistryConfig.setSubscriberRegistryQueueSize(15);
		subscriberRegistryConfig.setSubscriberRegistryBlocking(true);
		subscriberRegistryConfig.setNonBlockingWaitTime(0);
		config.setSubscriberRegistryConfig(subscriberRegistryConfig);

		EventFramworkConcurrenyConfig concurrenyConfig = new EventFramworkConcurrenyConfig();

		concurrenyConfig.setNumWorkerThreads(4);
		concurrenyConfig.setInputQueueSize(100);

		config.setConcurrenyConfig(concurrenyConfig);
	}
}
