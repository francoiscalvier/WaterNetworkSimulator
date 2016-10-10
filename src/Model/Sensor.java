package Model;

import java.io.PrintStream;
/**
 * class sensor device which can be activated or deactivated and then send data at a given frequency
 * @author fcalvier
 *
 */
public class Sensor extends Device{

	/**
	 * data sending frequency 
	 */
	private int frequency;
	/**
	 * activation status
	 */
	private boolean isActivated;
	/**
	 * a counter used for the sensor id
	 */
	private static int nbSensor = 0;
	/**
	 * the id of the sensor based on the value of preceding counter
	 */
	private String id; 
	public Sensor() {
		id = "sensor"+(nbSensor++);
		
		//TODO initialise communication
		
	}
	
	/**
	 * constructor initialising the frequency
	 * @param frequency the initial frequency
	 */
	public Sensor(int frequency) {
		id = "sensor"+(nbSensor++);
		//TODO initialise communication
		setFrequency(frequency);
		
	}
	
	public int getFrequency() {
		return frequency;
	}
	public void setFrequency(int frequency) {
		this.frequency = frequency;
	}
	
	
	/**
	 * Cause the diffusion of the last measured data of the sensor. The current protocol is the use of
	 * a print stream.
	 * @param printer the open printStream where the data are sent
	 */
	public void sendData(PrintStream printer){
		//TODO send data to the monitoring system
		printer.println(id +" \t"+ getData());
	}
	
	protected String getData() {
		return "";
	}
	
	/**
	 * when added to the network, a sensor is activated
	 */
	@Override
	public void addToNetwork() {
		activate();
		Network.add(this);
		
	}
	
	/**
	 * when removed from the network, a sensor is deactivated
	 */
	@Override
	public void removeFromNetwork() {
		deactivate();
		Network.remove(this);
		
	}
	@Override
	public String toString() {
		
		return id +"\n is activated " +isActivated+"\n frequency "+ getFrequency() + "\n measure " + getData();
	}
	
	public void activate(){
		isActivated = true;
	}
	public void deactivate(){
		isActivated = false;
	}
	
	public boolean isActivated(){
		return isActivated;
	}
	
	/**
	 * capture the data value
	 */
	public void measure(){
		
	}
	
}
