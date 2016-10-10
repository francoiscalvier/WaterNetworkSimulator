package Controler;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Hashtable;


import Model.Network;
import Model.WaterDevice;

/**
 * this class is a generator of events due to the management of the network (cleaning, scheduled
 * replacement, etc)
 * @author fcalvier
 *
 */
public class ServiceManagement implements Runnable {

	@Override
	public void run() {
		ArrayList<WaterDevice> list;
		Hashtable<WaterDevice, Long[]> stopedDevices = new Hashtable<WaterDevice, Long[]>();

		long duration;
		
		try {
			PrintStream ps = new PrintStream(new File("ServiceClosure"));
		
		while (true){
			list = Network.getWaterDeviceList();
			int index = (int) (Math.random()*Network.getClientList().size());
			duration = (long) (Math.random()*100000 + 1000);
			
			for(WaterDevice wd : list){
				if(stopedDevices.containsKey(wd)){
					Long[] time = stopedDevices.get(wd);
					if(System.currentTimeMillis()- time[1]> time[0]){
						wd.doSelectedAction("Open Valve");
					}
						
				}	
				else if(wd.getPossibleActions().contains("Close Valve")){
					
					Long[] time = {duration, System.currentTimeMillis()};
					wd.doSelectedAction("Close Valve");
					stopedDevices.put(wd, time);
					ps.println("device "+ index+ " interupted during " +duration + "ms");
				}
					
			
			}
		}
		
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}
	
}
