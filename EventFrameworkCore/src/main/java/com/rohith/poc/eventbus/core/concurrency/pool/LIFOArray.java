package com.rohith.poc.eventbus.core.concurrency.pool;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 
 * A Special Data Structure that helps in Last In First Out in a Multi Threaded Environment
 * 
 * @author Rohith Jaya Dev
 *
 * @param <T>
 */
public class LIFOArray<T> {

	private static final int FIXED_BUFFER_SIZE = 10;

	private Object[] buffer;

	private AtomicInteger nextRead;

	private AtomicInteger nextWrite;

	private AtomicBoolean isReadAllowed;

	private Lock readLock;

	private ReentrantLock writeLock;
	
	private AtomicInteger previousRead;

	public LIFOArray() {

		this.buffer = new Object[FIXED_BUFFER_SIZE];
		this.nextRead = new AtomicInteger(-1);
		this.nextWrite = new AtomicInteger(-1);
		this.previousRead = new AtomicInteger(-1);
		this.isReadAllowed = new AtomicBoolean(false);
		this.readLock = new ReentrantLock(true);
		this.writeLock = new ReentrantLock(true);
	}

	/**
	 * Read is lock free
	 * 
	 * If some other thread is reading the current value then he can return with
	 * out waiting
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public T read() {

		if (!isReadAllowed.get() || nextRead.get() == -1) {
			return null;
		}

		boolean isLocked = this.readLock.tryLock();

		if (isLocked) {
			int nextRead = this.nextRead.get();
			int prevRead = this.previousRead.get();
			Object nextValue = null;

			if(nextRead!=prevRead){
				nextValue=	this.buffer[nextRead];
				previousRead.set(this.nextRead.get());
				
			}
			if (this.nextRead.get() == nextRead) {
				isReadAllowed.compareAndSet(true, false);
			}
			this.readLock.unlock();
			return (T) nextValue;
		}
		return null;
	}

	/**
	 * Method helps in writing element to the lifo array
	 * 
	 * Updations are Blocking as a lock in involved
	 * 
	 * @param event
	 */
	public void write(T event) {

		int nextPos;
	
		try {
			this.writeLock.lockInterruptibly();

		} catch (InterruptedException e) {
			System.out.println("Locked Thread is Interrupted");
			Thread.currentThread().interrupt();
			return;
		}

		
			if (nextWrite.get() == -1 || nextWrite.get() >= FIXED_BUFFER_SIZE) {
				nextPos = 0;
				nextWrite.set(0);
			} else {
				if (nextWrite.get() == FIXED_BUFFER_SIZE - 1) {
					nextPos = 0;
					nextWrite.set(0);
				} else {
					nextPos = nextWrite.incrementAndGet();
				}
			}
			System.out.println("Writing" + event.toString());
			this.buffer[nextPos] = event;
			this.nextRead.set(nextPos);
			isReadAllowed.set(true);
			this.writeLock.unlock();
		

	}

}
