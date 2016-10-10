package Controler;

import java.util.HashSet;

import Model.Network;
/**
 * This class is a breakdown generator. It randomly makes a WaterDevice consume water  
 * @author fcalvier
 *
 */
public class Breakdown implements Runnable {
private HashSet<Integer> downWaterDevices = new HashSet<Integer>();
	
	public Breakdown() {
		new Thread(this).start();	
	}
	
	

	@Override
	public void run() {
		int index;
		float consumption;
			try {
				do{
				index = (int) (Math.random()*Network.getWaterDeviceList().size());
				}
				while(downWaterDevices.contains(index));
				downWaterDevices.add(index);
				consumption = (float) (Math.random()*Math.random()*10);
				System.out.println("device "+ index+ "is leaking" + consumption);
				Thread.sleep((long) (Math.random()*10000));
				new Thread(this).start();
				Network.getWaterDeviceList().get(index).drink(consumption);
				//TODO Network.getWaterDeviceList().get(index).possibleActions.add("Repair");
				} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		
		
	}
	
}
