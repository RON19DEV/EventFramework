package com.rohith.poc.eventbus.core;

import java.util.HashSet;
import java.util.Set;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.rohith.poc.eventbus.api.EventBus;
import com.rohith.poc.eventbus.api.config.EventFrameWorkConfig;
import com.rohith.poc.eventbus.api.config.EventFrameworkMode;
import com.rohith.poc.eventbus.api.config.EventFramworkConcurrenyConfig;
import com.rohith.poc.eventbus.api.config.EventMetaData;
import com.rohith.poc.eventbus.api.config.SubscriberRegistryConfig;
import com.rohith.poc.eventbus.api.exception.EventFrameworkException;
import com.rohith.poc.eventbus.core.factory.EventFrameworkFactory;



public class EventFramworkFactoryTest {

	static EventFrameWorkConfig config = null;

	private static EventBus eventBus;

	@BeforeClass
	public static void init() {

		config = new EventFrameWorkConfig();

		createConfig(config);
	}
	
	@Test
	public void NullConfigTest(){
		
		try {
			eventBus = EventFrameworkFactory.getEventBus(null);
		} catch (EventFrameworkException e) {
			
			Assert.assertEquals("null config", e.getMessage());
		}
		
	}
	
	
	@Test
	public void NullConcurrencyConfigTest(){
		
		EventFrameWorkConfig config = createNullConfig();
		
		try {
			eventBus = EventFrameworkFactory.getEventBus(config);
		} catch (EventFrameworkException e) {
			
			Assert.assertEquals("Concurrency Config is null", e.getMessage());
		}
		
	}
	

	@Test
	public void NullSubscriptionConfigTest(){
		
		try {
			eventBus = EventFrameworkFactory.getEventBus(config);
		} catch (EventFrameworkException e) {
			
			Assert.assertEquals("Subscription Registry can't be null", e.getMessage());
		}

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
		//config.setSubscriberRegistryConfig(subscriberRegistryConfig);

		EventFramworkConcurrenyConfig concurrenyConfig = new EventFramworkConcurrenyConfig();
		concurrenyConfig.setNumWorkerThreads(4);
		concurrenyConfig.setInputQueueSize(100);
		config.setConcurrenyConfig(concurrenyConfig);
	}

	private EventFrameWorkConfig createNullConfig() {
		
		EventFrameWorkConfig config = new EventFrameWorkConfig();
		
		
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
		//config.setConcurrenyConfig(concurrenyConfig);
		
		return config;
	}

}
