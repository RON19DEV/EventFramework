package com.rohith.poc.eventbus.api.config;

/**
 * 
 * Wait Strategy for the threads 
 * 
 * <p> NOTE:  Note Used in this version </p>
 * 
 * @author Rohith Jaya Dev
 *
 */
public enum WaitStrategy {

		WAITANDSLEEP("WAITANDSLEEP"),SPIN("SPIN");
		
		private String waitStrategy;
		
		private WaitStrategy(String waitStrategy){
			
			this.waitStrategy = waitStrategy;
		}
	
	
	
		public String waitStrategy(){
			
			return this.waitStrategy;
		}
}
