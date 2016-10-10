package Model;
/**
 *  Specific subclass of Controler which increase the pressure thanks to an electric pump
 *  Not tested
 * @author fcalvier
 *
 */
public class PumpingGroup extends Controler implements Runnable{

	/**
	 * Sensor stating the activity of the pumping group
	 */
	StateSensor pumpState;
	/**
	 * Sensor stating the electrical providing of the pumping group
	 */
	StateSensor electricState;
	/**
	 * Sensor
	 */
	LevelSensor pumpRotationSpeed;
	/**
	 * Sensor
	 */
	LevelSensor electricConsumption;
	/**
	 * Sensor relative to the place of the pump
	 */
	LevelSensor sonometer;
	/**
	 * Sensor relative to the place of the pump. the pumping action warm the pump and thus the water
	 * going through the pump
	 */
	LevelSensor waterThermometer;
	/**
	 * Sensor relative to the place of the pump
	 */
	LevelSensor airThermometer;
	/**
	 * Sensor
	 */
	LevelSensor pumpThermometer;
	
	
	public PumpingGroup(){
		super();
		pumpState = new StateSensor();
		electricState = new StateSensor();
		pumpRotationSpeed = new LevelSensor();
		electricConsumption = new LevelSensor();
		sonometer = new LevelSensor();
		waterThermometer = new LevelSensor();
		airThermometer = new LevelSensor();
		pumpThermometer = new LevelSensor();
		
	}
	@Override
	public void addToNetwork() {
		super.addToNetwork();
		pumpState.setOk(false);
		pumpState.addToNetwork();
		electricState.setOk(true);
		electricState.addToNetwork();
		pumpRotationSpeed.setLevel(0);
		pumpRotationSpeed.addToNetwork();
		electricConsumption.setLevel(0);
		electricConsumption.addToNetwork();
		sonometer.setLevel(0);
		sonometer.addToNetwork();
		waterThermometer.setLevel(getStream().getTemperature());
		waterThermometer.addToNetwork();
		airThermometer.setLevel(20);
		airThermometer.addToNetwork();
		pumpThermometer.setLevel(20);
		pumpThermometer.addToNetwork();
	}
	@Override
	public void removeFromNetwork() {
		super.removeFromNetwork();
		pumpState.removeFromNetwork();
		electricState.removeFromNetwork();
		pumpRotationSpeed.removeFromNetwork();
		electricConsumption.removeFromNetwork();
		sonometer.removeFromNetwork();
		waterThermometer.removeFromNetwork();
		airThermometer.removeFromNetwork();
		pumpThermometer.removeFromNetwork();
	}
	
	/**
	 * 
	 */
	public void activate(){
		t = new Thread(this);
		t.start();
	}
	
	public void deactivate(){
		pumpState.setOk(false);
		sonometer.setLevel(0);
	}
	@Override
	public void run() {
		while(!canPump()){
			Thread.yield();
			if(canPump()){
				pumpState.setOk(true);
				sonometer.setLevel(80);
				while(canPump()){
					pump();
					Thread.yield();
				}
				pumpState.setOk(false);
				sonometer.setLevel(0);
			}
		}
	}
	
	
	/**
	 * 
	 */
	protected void pump() {
		
		pumpState.setOk(canPump());
		electricConsumption.increase(1);
		pumpThermometer.increase(1);
		airThermometer.increase(1);
		modifyStreamPressure();
		
		
		
	}
	
	protected boolean canPump(){
		return electricState.isOk();
	}
	
	protected void modifyStreamPressure(){
		getOutStream().setIncomingPressure(getStream().getIncomingPressure() + 1);
	}
}
