package com.rohith.poc.eventbus.core.channel;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.rohith.poc.eventbus.api.channel.EventChannel;
import com.rohith.poc.eventbus.api.event.Event;
import com.rohith.poc.eventbus.api.exception.EventFrameworkException;
import com.rohith.poc.eventbus.api.subscriber.EventSubscriber;
import com.rohith.poc.eventbus.api.subscriber.FilteredEventSubscriber;

/**
 * An Abstract Channel that Implements the Event Channel 
 * 
 * <p> Used to perform common functionalities like applying filters and publishing </p>
 * 
 * @author Rohith Jaya Dev
 *
 */
public abstract class AbstractEventChannel implements EventChannel {

	private List<EventSubscriber<Event>> eventSubscribers;

	private List<FilteredEventSubscriber<Event>> filteredEventSubscribers;

	public AbstractEventChannel() {

		this.eventSubscribers = new CopyOnWriteArrayList<EventSubscriber<Event>>();

		this.filteredEventSubscribers = new CopyOnWriteArrayList<FilteredEventSubscriber<Event>>();
	}

	protected List<EventSubscriber<Event>> getEventSubscribers() {

		return this.eventSubscribers;
	}

	protected List<FilteredEventSubscriber<Event>> getFilteredEventSubscribers() {

		return this.filteredEventSubscribers;
	}

	public void addSubscriber(EventSubscriber<Event> subscriber) {

		this.eventSubscribers.add(subscriber);
	}

	public void addSubscribersForFilteredEvents(FilteredEventSubscriber<Event> subscriber) {

		this.filteredEventSubscribers.add(subscriber);
	}

	protected void publish(EventSubscriber<Event> i, Event e) throws EventFrameworkException {

			i.onEvent(e);

	}

	protected boolean applyFilter(FilteredEventSubscriber<Event> i, Event e) throws EventFrameworkException {

			return i.applyFilter(e);

	}
	
	protected void publishToNormalSubscribers(Iterator<EventSubscriber<Event>> iterator , Event e){
		int i=1;
		while (iterator.hasNext()) {
			EventSubscriber<Event> next = iterator.next();
			try {
				publish(next, e);
			} catch (EventFrameworkException e1) {
				next.onError(e1);
			}
		}
	}
	
	protected void publishToFilteredSubscribers(Iterator<FilteredEventSubscriber<Event>> iterator , Event e){
		
		while (iterator.hasNext()) {
			FilteredEventSubscriber<Event> next = iterator.next();
			try {
				applyFilterAndDispact(next, e);
			} catch (EventFrameworkException e1) {
				next.onError(e1);
			}
		}
	}
	
	@SuppressWarnings("rawtypes")
	protected void applyFilterAndDispact(FilteredEventSubscriber<Event> i, Event e) throws EventFrameworkException {

		if (applyFilter(i, e)) {
			publish(i, e);
		}

	}

}
