package com.rohith.poc.eventbus.api.exception;

/**
 * Event Frame Work Exception that extends {@link Exception}
 * 
 * @author Rohith Jaya Dev
 *
 */
public class EventFrameworkException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	public EventFrameworkException(String message){
		
		super(message);
	}
	
	public EventFrameworkException(String message, Throwable cause){
		
		super(message,cause);
	}

	public EventFrameworkException(Throwable cause){
		
		super(cause);
	}
}
