package com.rohith.poc.eventbus.api.event;

/**
 * 
 * <p>Represents a Wrapper Class that represents any trading system events such as MarketData , Executions so on </p>
 * 
 * 
 * <p> Event Bus accepts a Implementation of Event of Type T</p>
 * 
 * 
 * <p> Get Type must return the string type of the event such as SGX-MD - for SGX Market Data and SO on </p>
 * 
 * <p> If the get Type method returns null then then the event will be ignored by the bus </p>
 * 
 * <p>  Get Actual returns the actual wrapped object </p>
 * @author Rohith Jayadev 
 *
 */
public interface Event {

		public String getEventType();


}
