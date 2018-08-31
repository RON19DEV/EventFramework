package com.rohith.poc.eventbus.api.config;


/**
 * 
 * Concurrency Configuration of the Event Bus
 * 
 * @author Rohith Jayadev
 *
 */
public class EventFramworkConcurrenyConfig {

	/**
	 * Default Input Queue Size
	 */
	public static final int DEFAULT_INPUT_QUEUE_SIZE=100;
	
	public static final int DEFAULT_NUM_THREADS =3;
	
	/**
	 * <p>Number of worker threads for the asynchronous mode</p>
	 * 
	 * <p>This the actual count of the event dispatcher thread. Apart from this there will be one
	 * 
	 *    monitoring thread which monitors the input queue(s) </p>
	 * 
	 */
	private int numWorkerThreads;
	
	
	
	/**
	 * Wait Strategy for the Monitoring Thread.
	 * 
	 * <p> This applies for only SingleLayeredAsynMode</p>
	 * 
	 * @see WaitStrategy
	 * 
	 * 
	 */
	private WaitStrategy monitorThreadStrategy;
	
	
	/**
	 * This property represents the size of the input queue in ASYNC Mode
	 * 
	 *  <p> In ASYNC SINGLE QUEUE Mode one input queue with provided size will be created . If not provided a queue of size 100 will be created</p>
	 *  
	 *  <p> IN ASYNC MULTI QUEUE Mode, input queues of provided in EventMetaData will be created for each message type achieving
	 *  	
	 *  	granularity and better performance. If not provided a Default Queue of Size Created </p>
	 *  
	 *  Note : Configure the queue size based on the mode.
	 *  
	 *  @see EventFrameworkMode
	 * 
	 */
	private int inputQueueSize;

	public int getNumWorkerThreads() {
		return numWorkerThreads;
	}

	public void setNumWorkerThreads(int numWorkerThreads) {
		this.numWorkerThreads = numWorkerThreads;
	}



	public WaitStrategy getMonitorThreadStrategy() {
		return monitorThreadStrategy;
	}

	public void setMonitorThreadStrategy(WaitStrategy monitorThreadStrategy) {
		this.monitorThreadStrategy = monitorThreadStrategy;
	}

	public int getInputQueueSize() {
		
		return inputQueueSize;
	}

	public void setInputQueueSize(int inputQueueSize) {
		
		if(inputQueueSize<=0){
			this.inputQueueSize = DEFAULT_INPUT_QUEUE_SIZE;
			return;
		}
		this.inputQueueSize = inputQueueSize;
	}
	
}
