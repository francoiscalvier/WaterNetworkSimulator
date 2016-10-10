package Model;

import java.util.ArrayList;
/**
 * 
 * @author fcalvier
 *
 */
public class FireHydrant extends WaterDevice {
	
	/**
	 * valve used for device shutdown (maintenance case) 
	 */
	Valve incomingValve;
	/**
	 * valves used by firemen
	 */
	ArrayList<Valve> outgoingValves;
	/**
	 * water consumed from firehydrants is not billed but should be counted
	 */
	LevelSensor flowmeter;
	/**
	 * the law ask a particular minimal pressure at firehydrant place 
	 */
	LevelSensor manometer;
	/**
	 * Sensor stating the regular use of the device (firemen key). If a outgoing valve is open and
	 *  the opening sensor is closed then the usage is not regular. 
	 */
	StateSensor openingSensor;
	/**
	 * Sensor stating the global state of the device
	 */
	StateSensor stateSensor;
	
	/**
	 * default constructor for a fireHydrant with 1 outgoing valve
	 */
	public FireHydrant(){
		outgoingDevices =null;
		incomingValve = new Valve();
		outgoingValves = new ArrayList<Valve>();
		outgoingValves.add(new Valve());
		flowmeter = new LevelSensor();
		manometer = new LevelSensor();
		openingSensor = new StateSensor();
		stateSensor = new StateSensor();
			
	}
	/**
	 * Constructor specifyaing the number of outgoing valves
	 * @param nbValves number of outgoing valves
	 */
	public FireHydrant(int nbValves){
		outgoingDevices =null;
		incomingValve = new Valve();
		outgoingValves = new ArrayList<Valve>();
		for(int i=0;i<nbValves;i++){
			outgoingValves.add(new Valve());
		}
		flowmeter = new LevelSensor();
		manometer = new LevelSensor();
		openingSensor = new StateSensor();
		stateSensor = new StateSensor();
			
	}
	
	/**
	 * The incoming valve is open and the outgoing valves are closed
	 */
	@Override
	public void addToNetwork(WaterDevice source) {
		incomingValve.setOpenRate(1);
		incomingValve.addToNetwork();
		for(Valve v : outgoingValves){
			v.setOpenRate(0);
			v.addToNetwork();
		}
		
		flowmeter.addToNetwork();
		manometer.addToNetwork();
		openingSensor.setOk(false);
		openingSensor.addToNetwork();
		stateSensor.setOk(true);
		stateSensor.addToNetwork();
		super.addToNetwork( source);
	}
	@Override
	public void removeFromNetwork() {
		incomingValve.removeFromNetwork();
		for(Valve v : outgoingValves){
			v.removeFromNetwork();
		}
		
		flowmeter.removeFromNetwork();
		manometer.removeFromNetwork();
		openingSensor.removeFromNetwork();
		stateSensor.removeFromNetwork();
		super.removeFromNetwork();
	}
	
	@Override
	public double getSection() {
		return super.getSection() * incomingValve.getOpenRate();
	}
	
	
	@Override
	public void updatedStream() {
		super.updatedStream();
		flowmeter.setIncrement(getStream().getOutgoingFlow());
		manometer.setLevel(getStream().getIncomingPressure());
	}
	
	/**
	 * open one outgoing valve and start to consume water
	 */
	public void firemenWithrawal(){
		openingSensor.setOk(true);
		outgoingValves.get(0).setOpenRate(1);
		if (canDrink()){
			drink(10);
		}
		else{
			firemenEndWithrawal();
		}
	}
	
	/**
	 * close the open valve and stop water consuming
	 */
	public void firemenEndWithrawal(){
		stopDrink();
		openingSensor.setOk(false);
		outgoingValves.get(0).setOpenRate(0);
		
	}
	
	/**
	 * set the state sensor to the broken state and start water consuming
	 */
	public void damage(){
		stateSensor.setOk(false);
		drink();
	}
	
	/**
	 * stop water consuming and set the state sensor to the safe state
	 */
	public void repair(){
		stopDrink();
		stateSensor.setOk(true);
	}
}
