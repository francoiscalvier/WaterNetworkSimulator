package Controler;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Set;
import Model.Network;
import Model.Sensor;

/**
 * This class manage the sensor activity of the whole network in a single thread
 * @author fcalvier
 *
 */
public class SensorPool implements  Runnable {
	

	public SensorPool(){
		new Thread(this).start();
	}

	@Override
	public void run() {
		Set<Sensor> list;
		try {
			PrintStream ps = new PrintStream(new File("SensorOutput"));
		
		while (true){
			list = Network.getSensorList().keySet();
			for(Sensor s : list){
				if(s.isActivated()){
					
					
					if(s.getFrequency() >0){
						long waitTime = System.currentTimeMillis();
						//TODO for LevelSensor, measure should be done once per second
						if(waitTime - Network.getSensorList().get(s)>=s.getFrequency()*1000){
							s.measure();
							Network.getSensorList().put(s, waitTime);
							ps.print(waitTime+ "\t"); 
							s.sendData(ps);
							
						}


					}


				}
			}
		}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
