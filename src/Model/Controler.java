package Model;
/**
 * Specific water device which actively modify the pressure and/or the flow of the incoming water 
 * stream 
 * Not tested
 * @author fcalvier
 *
 */
public class Controler extends WaterDevice{
	/**
	 * sensor
	 */
	private LevelSensor flow;
	/**
	 * sensor
	 */
	private LevelSensor incomingPressure;
	/**
	 * sensor
	 */
	private LevelSensor outgoingPressure;
	
	/**
	 * associated outgoing stream
	 */
	private WaterStream outStream;
	
	
	/**
	 * default constructor 
	 */
	public Controler() {
		flow = new LevelSensor();
		incomingPressure = new LevelSensor();
		outgoingPressure = new LevelSensor();
	}
	
	/**
	 * contructor for named Controler. 
	 * @param name value of the name attribute
	 */
	public Controler(String name) {
		super(name);
		flow = new LevelSensor();
		incomingPressure = new LevelSensor();
		outgoingPressure = new LevelSensor();
	}
	
	/**
	 * once added, the controler start consuming water
	 */
	@Override
	public void addToNetwork(WaterDevice source) {
		
		flow.addToNetwork();
		incomingPressure.addToNetwork();
		outgoingPressure.addToNetwork();
		super.addToNetwork(source);
		drink(getMaxFlow());
	}
	
	/**
	 * the water consumption of the controler is stopped before its removal. 
	 */
	@Override
	public void removeFromNetwork() {
		stopDrink();
		super.removeFromNetwork();
		flow.removeFromNetwork();
		incomingPressure.removeFromNetwork();
		outgoingPressure.removeFromNetwork();
		
	}
	
	
	@Override
	public void updatedStream(){
		super.updatedStream();
		flow.setLevel(getStream().getIncomingFlow());
		incomingPressure.setLevel(getStream().getIncomingPressure());
		outgoingPressure.setLevel(getOutStream().getIncomingPressure());
		
	}
	
	@Override
	public void addOutgoingDevice(WaterDevice device){
		outgoingDevices.add(device);
		device.setUsed(true);
		synchronized (lock) {
			getOutStream().addOutgoing(device.getStream());
		}
		updateDiameter();
	}
	
	/**
	 * access to the outgoing stream. Create this stream If no one is associated
	 * @return
	 */
	public WaterStream getOutStream(){
		if (outStream == null){
			outStream = new WaterStream(this, 0,MAXPRESSURE);
		}
		return outStream;
	}
	
	/**
	 * update the outgoing stream pressure 
	 */
	@Override
	public boolean canDrink() {
		if(super.canDrink()){
			outStream.setIncomingPressure(MAXPRESSURE);
			return true;
		}
		else{
			outStream.setIncomingPressure(0);
			return false;
		}
}
}
