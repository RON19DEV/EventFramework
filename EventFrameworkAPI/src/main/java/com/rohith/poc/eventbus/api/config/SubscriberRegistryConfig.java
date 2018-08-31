package com.rohith.poc.eventbus.api.config;

/**
 * 
 * The Subscriber Registry Config
 * 
 * Its recommended to use the non blocking mode as the registry queue monitor thread will handle the registration of Subscriber
 * 
 * <p>See<b> com.rohith.poc.eventbus.core.bus.AbstractEventBus.RegistryThread </b></p>
 * 
 * @author Rohith Jaya Dev
 *
 */
public class SubscriberRegistryConfig {

	public static final int DEFAULT_QUEUE_SIZE =10;
	
	public static final int DEFAULT_NON_BLOCKING_TIMEOUT =5000;
	
	/**
	 * <p> The size of the registry queue </p>
	 * 
	 * 	 */
	private int subscriberRegistryQueueSize;
	
	/**
	 * 
	 *<p> Boolean Flag which indicates whether subscriber registry should block the 
	 * 
	 * attempting thread or not </p>
	 * 
	 * Default Value is false
	 */
	private boolean isSubscriberRegistryBlocking =true;
	
	/**
	 * Amount of time a registering thread should wait in case of a non blocking mode
	 * 
	 */
	private int nonBlockingWaitTime;
	
	

	public int getSubscriberRegistryQueueSize() {
		return subscriberRegistryQueueSize;
	}

	public void setSubscriberRegistryQueueSize(int subscriberRegistryQueueSize) {
		this.subscriberRegistryQueueSize = subscriberRegistryQueueSize;
	}

	public boolean isSubscriberRegistryBlocking() {
		return isSubscriberRegistryBlocking;
	}

	public void setSubscriberRegistryBlocking(boolean isSubscriberRegistryBlocking) {
		this.isSubscriberRegistryBlocking = isSubscriberRegistryBlocking;
	}

	public int getNonBlockingWaitTime() {
		return nonBlockingWaitTime;
	}

	public void setNonBlockingWaitTime(int nonBlockingWaitTime) {
		this.nonBlockingWaitTime = nonBlockingWaitTime;
	}
	
	
	
}
