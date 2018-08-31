package com.rohith.poc.eventbus.core;

import java.util.HashSet;
import java.util.Set;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.rohith.poc.eventbus.api.EventBus;
import com.rohith.poc.eventbus.api.config.EventFrameWorkConfig;
import com.rohith.poc.eventbus.api.config.EventFrameworkMode;
import com.rohith.poc.eventbus.api.config.EventMetaData;
import com.rohith.poc.eventbus.api.config.SubscriberRegistryConfig;
import com.rohith.poc.eventbus.api.exception.EventFrameworkException;
import com.rohith.poc.eventbus.api.subscriber.SubscriberType;
import com.rohith.poc.eventbus.core.factory.EventFrameworkFactory;

/**
 * This is the Synchronous Mode with No Queue for publishing messages , the publisher directly calls the subscriber call back
 * 
 * @author Rohith Jaya Dev Sudhakaran
 *
 */
public class SynchronousModeTest {

	static EventFrameWorkConfig config = null;

	private static EventBus eventBus;

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
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		DummyEventSubscriber eventSubscriber = new DummyEventSubscriber(SubscriberType.NORMAL, "SGX-MD");
		
		eventBus.addSubscriber(DummyEventSubscriber.class, eventSubscriber);

		try {
			Thread.sleep(100);
			
		} catch (InterruptedException e) {

			e.printStackTrace();
		}
		
		Thread t = new Thread(new Runnable() {

			@Override
			public void run() {

				for (int i = 0; i < 1000; i++) {
					eventBus.publish(new StringEvent(i, "SGX-MD"));
				}

			}
		});

		t.start();

		try {
			Thread.sleep(1000);
			t.join();
			Assert.assertEquals( 1000, eventSubscriber.count());
		} catch (InterruptedException e) {

			e.printStackTrace();
		}

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
		config.setMessageTypeHandled(messageTypeHandled);
		config.setMode(EventFrameworkMode.SYNC);
		SubscriberRegistryConfig subscriberRegistryConfig = new SubscriberRegistryConfig();
		subscriberRegistryConfig.setSubscriberRegistryQueueSize(15);
		subscriberRegistryConfig.setSubscriberRegistryBlocking(true);
		subscriberRegistryConfig.setNonBlockingWaitTime(0);
		config.setSubscriberRegistryConfig(subscriberRegistryConfig);

	}

}
