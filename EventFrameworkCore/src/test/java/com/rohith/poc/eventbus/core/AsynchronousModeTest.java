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
 * This is the test case for the async single queue model
 * 
 * Here  there will be a single queue for all the inputs and a monitor thread will take elements from
 * 
 * the queue and feed the worker threads
 * 
 * @author Rohith Jaya Dev
 *
 */
public class AsynchronousModeTest {

	static EventFrameWorkConfig config = null;
	
	private static EventBus eventBus;
	
	@BeforeClass
	public static void init(){
		
		config =	new EventFrameWorkConfig();
		
		createConfig(config);
	}
	@Test
	public void test(){
		
		try {
			eventBus = EventFrameworkFactory.getEventBus(config);
		
			DummyEventSubscriber eventSubscriber = new DummyEventSubscriber (SubscriberType.NORMAL, "SGX-MD");
		
			eventBus.addSubscriber(DummyEventSubscriber.class, eventSubscriber);
			
			try {
			
				Thread.sleep(100);
		
			} catch (InterruptedException e) {
				
				e.printStackTrace();
			}
			
			Thread t = new Thread(new Runnable() {
				
				@Override
				public void run() {
					for(int i=0;i<=10;i++){
							eventBus.publish(new StringEvent(i));
						}
					
					System.out.println("This Thread exiting");
				}
			});
			
			t.start();
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				
				e.printStackTrace();
			}
			
	
			
		System.out.println("Main Exiting");
		
		} catch (EventFrameworkException e) {
			
			e.printStackTrace();
		}
		
		
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
		config.setMessageTypeHandled(messageTypeHandled );
		config.setMode(EventFrameworkMode.ASYNCSINGLEQUEUE);
		SubscriberRegistryConfig subscriberRegistryConfig = new SubscriberRegistryConfig();
		subscriberRegistryConfig.setSubscriberRegistryQueueSize(15);
		subscriberRegistryConfig.setSubscriberRegistryBlocking(true);
		subscriberRegistryConfig.setNonBlockingWaitTime(0);
		config.setSubscriberRegistryConfig(subscriberRegistryConfig );
		
		
		EventFramworkConcurrenyConfig concurrenyConfig = new EventFramworkConcurrenyConfig();
	
		concurrenyConfig.setNumWorkerThreads(4);
		concurrenyConfig.setInputQueueSize(100);
		
		config.setConcurrenyConfig(concurrenyConfig);
	}
	
}
