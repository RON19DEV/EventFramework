package com.rohith.poc.eventbus.api.config;

import java.util.Objects;

/**
 * Message MetaData
 * 
 * <p>
 * Represents and Event Level Configuration
 * </p>
 * 
 * <ul>
 * <li>Event Type is a String representation of the event . If this value is
 * null then that message type will not be handled</li>
 * <li>Queue Size is used to represent the message level queue size in the Multi
 * Queue ASYNC DESIGN</li>
 * </ul>
 * @author Rohith Jaya Dev
 *
 */
public class EventMetaData {

	private static final int DEFAULT_QUEUE_SIZE = 100;

	private String eventType;

	private Integer queueSize;

	public EventMetaData() {

		this.queueSize = new Integer(DEFAULT_QUEUE_SIZE);
	}

	public String getEventType() {
		return eventType;
	}

	public void setEventType(String eventType) {
		this.eventType = eventType;
	}

	public int getQueueSize() {
		return queueSize;
	}

	public void setQueueSize(Integer queueSize) {

		if (queueSize <= 0) {

			return;
		}
		this.queueSize = queueSize;
	}

	@Override
	public int hashCode() {

		int prime = 31;

		int initValue = 0;

		initValue = initValue + (this.eventType.hashCode() * prime);

		initValue = initValue + (this.queueSize.hashCode() * prime);

		return initValue;
	}

	@Override
	public boolean equals(Object o) {

		if (this == o) {

			return true;
		}

		if (o == null)
			return false;

		if (getClass() != o.getClass())
			return false;

		EventMetaData other = (EventMetaData) o;

		return Objects.equals(this.eventType, other.eventType) && Objects.equals(this.queueSize, other.queueSize);

	}

}
