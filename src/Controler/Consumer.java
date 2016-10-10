package Controler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Hashtable;

import Model.Network;
import Model.WaterDevice;

/**
 * This class is a consumption generator. Every second, it randomly makes a PrivateSector consume water
 * @author fcalvier
 *
 */
public class Consumer implements Runnable{

	private Hashtable<WaterDevice, Long[]> drinkingClients = new Hashtable<WaterDevice, Long[]>();

	public Consumer() {
		new Thread(this).start();	
	}


	@Override
	public void run() {
		test2();
	}

	/**
	 * This method is the real consumption generator.  Every second, it randomly makes a PrivateSector 
	 * consume water for a random duration
	 */
	@Deprecated
	private void test(){
		WaterDevice wd;
		long duration;

		try {
			PrintStream ps = new PrintStream(new File("ConsumptionGeneration"));
			
			do{
				wd =   Network.getClientList().get((int) (Math.random()*Network.getClientList().size()));
			}
			while(drinkingClients.containsKey(wd));
			duration = (long) (Math.random()*10000 + 1000);
			Long[] time = {duration,System.currentTimeMillis()};
			drinkingClients.put(wd,time);
			ps.println(System.currentTimeMillis()+ "\t client "+ wd+ " during " +duration + "ms");
			Thread.sleep(100);
			new Thread(this).start();
			wd.drink(duration);
			Thread.sleep(1000);
			drinkingClients.remove(wd);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}



	}

	/**
	 * Simplified consumption generator. Makes each PrivateSector consume water for a random duration
	 */
	public  void test1() {
		long duration;
		try {
			PrintStream ps = new PrintStream(new File("ConsumptionGeneration"));

			for(WaterDevice wd : Network.getClientList()){
				duration = (long) (Math.random()*10000 + 1000);
				ps.println(System.currentTimeMillis() + " client "+ wd.toString()+ " during " +duration + "ms");
				wd.drink(duration);
				
				
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	/**
	 * Second implementation of the real consumption generator.  Every second, it randomly makes a PrivateSector 
	 * consume water for a random duration. Less Threads are involved.  
	 */
	public synchronized void test2() {
		

		try {
			PrintStream ps = new PrintStream(new File("ConsumptionGeneration"));
			while(true){
				wait(500);
				
				
				new Thread (){
					@Override
					public void run(){
						WaterDevice wd;
						long duration;
						wd =Network.getClientList().get((int) (Math.random()*Network.getClientList().size()));
						duration = (long) (Math.random()*10000 + 1000);
						boolean added = false;
						Long[] time = {duration,System.currentTimeMillis()};
						synchronized (drinkingClients) {
							if(!drinkingClients.containsKey(wd)){
								 
								drinkingClients.put(wd, time);
								added = true;
								
							}
						}
						if(added){
							ps.println("client "+ wd+ " during " +duration + "ms");
							wd.drink(duration);
							
							synchronized (drinkingClients) {
								try {
									drinkingClients.wait(1000);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
								drinkingClients.remove(wd);
							}
						}
						
					}
				}.start();
				
					
				

				

			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}


	}
}
