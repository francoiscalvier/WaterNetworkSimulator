package Model;

import java.util.ArrayList;
/**
 * Engine providing emergency electric supply to one or several devices of the network. Manage some 
 * fuel tanks and the place it is stored.
 * @author fcalvier
 *
 */
public class ElectricGenerator extends Device{
	/**
	 * fuel consumption (in cubic meter per hour) of the engine
	 */
	private float consumption;
	/**
	 * Sensor stating the if the engine is running. 
	 */
	StateSensor useSensor; 
	/**
	 * list of sensors relative to the tanks (one per tank)
	 */
	ArrayList<LevelSensor> fuelLevelSensor;
	/**
	 * Sensor relative to the engine
	 */
	LevelSensor oilThermometer; 
	/**
	 * Sensor relative to the place
	 */
	LevelSensor sonometer; 
	/**
	 * Sensor relative to the place
	 */
	LevelSensor airQualitySensor;
	
	/**
	 * default constructor. The resulting generator has one tank 
	 */
	public ElectricGenerator (){
		useSensor = new StateSensor();
		fuelLevelSensor = new ArrayList<LevelSensor>();
		fuelLevelSensor.add(new LevelSensor());
		oilThermometer = new LevelSensor();
		sonometer = new LevelSensor();
		airQualitySensor = new LevelSensor();
			
	}

	/**
	 * contructor specifying the number of tanks of the generator
	 * @param nbTanks
	 */
	public ElectricGenerator(int nbTanks){
		useSensor = new StateSensor();
		fuelLevelSensor = new ArrayList<LevelSensor>();
		for (int i =0; i< nbTanks; i++){
			fuelLevelSensor.add(new LevelSensor());
		}
		oilThermometer = new LevelSensor();
		sonometer = new LevelSensor();
		airQualitySensor = new LevelSensor();
		
			
	}
	
	@Override
	public void addToNetwork() {
		useSensor.setOk(false);
		useSensor.addToNetwork();
		for (LevelSensor sensor : fuelLevelSensor){
			sensor.setLevel(0);
			sensor.addToNetwork();
		}
		oilThermometer.setLevel(20);
		oilThermometer.addToNetwork();
		sonometer.setLevel(0);
		sonometer.addToNetwork();
		airQualitySensor.setLevel(100);
		airQualitySensor.addToNetwork();
		super.addToNetwork();
	}
	
	@Override
	public void removeFromNetwork() {
		useSensor.removeFromNetwork();
		for (LevelSensor sensor : fuelLevelSensor){
			sensor.removeFromNetwork();
		}
		oilThermometer.removeFromNetwork();
		sonometer.removeFromNetwork();
		airQualitySensor.removeFromNetwork();
		super.removeFromNetwork();
	}
	
	private boolean canRun(){
		boolean emptyTank = true;
		for(LevelSensor s : fuelLevelSensor){
			if (emptyTank && s.getLevel() != 0){
				emptyTank =false;
			}
		}
		return !emptyTank;
	}
	
	/**
	 * while the engine has fuel, it consume fuel and degrade the air quality
	 */
	public void start(){
		
		useSensor.setOk(canRun());
		while(useSensor.isOk() && canRun()){
			for(LevelSensor s : fuelLevelSensor){
				s.decrease (consumption/fuelLevelSensor.size());
			}
			airQualitySensor.decrease(1);
		}
		stop();
	}
	
	
	public void stop(){
		useSensor.setOk(false);
	}
	
	
	/**
	 * method to fill the fuel tanks. The fuel volume is shared by all the available tanks
	 * @param fuelQuantity the fuel volume to add
	 */
	public void fill (float fuelQuantity){
		//TODO should test for overflow
		for(LevelSensor s : fuelLevelSensor){
			s.increase (fuelQuantity/fuelLevelSensor.size());
		}
	}
	
	

}
