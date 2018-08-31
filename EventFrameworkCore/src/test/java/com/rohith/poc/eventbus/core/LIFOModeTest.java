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
import com.rohith.poc.eventbus.core.factory.EventFrameworkFactory;
/**
 * 
 * This Test Class is used to test the latest event first mode 
 * 
 * <p> It uses the Aysnhronous Event Bus  which uses the Slef Serving thread pool </p>
 * 
 * <p> But the difference from the Multi Queued Aysnchronous Mode is that it uses LIFO Event Pool while
 * 	 the async Multi Queued Mode uses Normal Event Pool </p>
 * 
 * <p>Note : LOGS are asynchrnous due to multithreaded environment but if you look at the logs carefully it
 * 
 * 			takes only the latest event written the readed access the pool </p>
 * 
 * @author Rohith Jaya Dev
 *
 */
public class LIFOModeTest {

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

		eventSubscriber = new DummyEventSubscriber(SubscriberType.NORMAL, "SGX-MD");

		eventBus.addSubscriber(DummyEventSubscriber.class, eventSubscriber);

		try {

			Thread.sleep(100);

		} catch (InterruptedException e) {

			e.printStackTrace();
		}

		Thread t = new Thread(new Runnable() {

			@Override
			public void run() {
				for (int i = 0; i <= 10; i++) {

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
			Thread.sleep(1000);
			t.join();
			t1.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		System.out.println("Main Exiting");
	}
	
	@AfterClass
	public static void destroy(){
		
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
		config.setMode(EventFrameworkMode.LIFO);
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
