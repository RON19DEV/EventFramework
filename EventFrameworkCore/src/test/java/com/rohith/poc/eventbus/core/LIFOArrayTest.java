package com.rohith.poc.eventbus.core;

import org.junit.BeforeClass;
import org.junit.Test;

import com.rohith.poc.eventbus.core.concurrency.pool.LIFOArray;
/**
 * Test Class for LiFO array data structure
 * 
 * Once an Element is taken that will never be taken again thats why null at the end of the output
 * 
 * @author Rohith Jaya Dev
 *
 */
public class LIFOArrayTest {

	private static LIFOArray<Integer> array;
	
	@BeforeClass
	public static void init(){
		
		array = new LIFOArray<Integer>();
	}
	
	@Test
	public void testArray(){
		
		Thread t = new Thread(new Runnable() {
			
			@Override
			public void run() {
				for(int i=10;i<20;i++){
					array.write(i);
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
				
			}
		});
		

		
		Thread t1 = new Thread(new Runnable() {
			
			@Override
			public void run() {
				for(int i=0;i<10;i++){
					array.write(i);
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
			}
		});
		
		t1.start();
		t.start();
		Thread t2= new Thread(new Runnable() {
			
			@Override
			public void run() {
				for(int i=0;i<20;i++){
					System.out.println("Read" + array.read());
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
			}
		});
	
	t2.start();
	try{
		t2.join();
		t1.join();
		t.join();
	}catch(InterruptedException e){
		e.printStackTrace();
	}
	
	}
	
}
