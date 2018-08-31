package com.rohith.poc.eventbus.api.config;

import java.util.Set;
/**
 * 
 * 	The Main Configuration File for the project
 * 
 * @author Rohith Jaya Dev 
 *
 */
public class EventFrameWorkConfig {

	/**
	 * Mode of working of the even frame work
	 * 
	 * @see EventFrameworkMode
	 * 
	 */
	private EventFrameworkMode mode;
	
	
	/**
	 * 
	 * A Set of message type the event framework can handle, dynamic message addition
	 * 
	 * capability is not provided for this version
	 * 
	 */
	private Set<EventMetaData> messageTypeHandled;

	/**
	 * 
	 * <p> Subscriber Registry Configuration </p>
	 * 
	 */
	private SubscriberRegistryConfig subscriberRegistryConfig;
	
	/**
	 * Concurrency specific configuration to be used for Asynchronous Modes
	 * 
	 * 
	 */
	private EventFramworkConcurrenyConfig concurrenyConfig;
	
	
	public EventFrameworkMode getMode() {
		return mode;
	}


	public void setMode(EventFrameworkMode mode) {
		this.mode = mode;
	}



	public Set<EventMetaData> getMessageTypeHandled() {
		return messageTypeHandled;
	}


	public void setMessageTypeHandled(Set<EventMetaData> messageTypeHandled) {
		this.messageTypeHandled = messageTypeHandled;
	}


	public SubscriberRegistryConfig getSubscriberRegistryConfig() {
		return subscriberRegistryConfig;
	}


	public void setSubscriberRegistryConfig(SubscriberRegistryConfig subscriberRegistryConfig) {
		this.subscriberRegistryConfig = subscriberRegistryConfig;
	}


	public EventFramworkConcurrenyConfig getConcurrenyConfig() {
		return concurrenyConfig;
	}


	public void setConcurrenyConfig(EventFramworkConcurrenyConfig concurrenyConfig) {
		this.concurrenyConfig = concurrenyConfig;
	}
	
	
	
}
