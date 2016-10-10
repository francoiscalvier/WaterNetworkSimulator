package Model;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Special water device acting as a water consumer and a water source.
 * @author fcalvier
 *
 */
public class Reservoir extends WaterDevice {
	
	/**
	 * Sensor 
	 */
	private LevelSensor incomingFlow;
	/**
	 * Sensor 
	 */
	private LevelSensor incomingPressure;
	/**
	 * Sensor list
	 */
	private HashMap<LevelSensor, WaterStream> outgoingFlow;
	/**
	 * Sensor 
	 */
	private StateSensor presenceSensor;
	/**
	 * Sensor 
	 */
	private StateSensor electricState;
	/**
	 * Sensor 
	 */
	private StateSensor fanState;
	/**
	 * Sensor 
	 */
	private LevelSensor waterThermometer;
	/**
	 * Sensor 
	 */
	private LevelSensor airThermometer;
	/**
	 * Sensor 
	 */
	private LevelSensor hygrometer;
	/**
	 * Sensor 
	 */
	private LevelSensor photometer;
	/**
	 * Sensor 
	 */
	private LevelSensor turbidimeter;
	/**
	 * Sensor list (1 per tank)
	 */
	private ArrayList<LevelSensor> waterLevel;
	/**
	 * Sensor list (1 per tank)
	 */
	private ArrayList<StateSensor> waterOverflow;
	/**
	 * Valve list (1 per tank)
	 */
	private ArrayList<Valve> valves;

	/**
	 * water stream released by the tank 
	 */
	private WaterStream outStream;

	/**
	 * total water capacity of the Reservoir
	 */
	private double capacity= 100000;
	
	/**
	 * volume of water in the Reservoir
	 */
	private double effectiveVolume = 10000;

	/**
	 * default constructor. The resulting Reservoir has 1 tank
	 */
	public Reservoir() {
		incomingFlow = new LevelSensor();
		incomingPressure = new LevelSensor();
		outgoingFlow = new HashMap<LevelSensor, WaterStream>();
		presenceSensor = new StateSensor();
		electricState = new StateSensor();
		fanState = new StateSensor();
		waterThermometer = new LevelSensor();
		airThermometer = new LevelSensor();
		hygrometer = new LevelSensor();
		photometer = new LevelSensor();
		turbidimeter = new LevelSensor();
		waterLevel = new ArrayList<LevelSensor>();
		waterLevel.add(new LevelSensor());
		waterOverflow = new ArrayList<StateSensor>();
		waterOverflow.add(new StateSensor());
		valves = new ArrayList<Valve>();
		valves.add(new Valve());

	}

	/** 
	 * Constructor specifying the number of tanks.
	 * @param nbTanks the number of tanks in the Reservoir
	 */
	public Reservoir(int nbTanks) {
		incomingFlow = new LevelSensor();
		incomingPressure = new LevelSensor();
		outgoingFlow = new HashMap<LevelSensor, WaterStream>();
		presenceSensor = new StateSensor();
		electricState = new StateSensor();
		fanState = new StateSensor();
		waterThermometer = new LevelSensor();
		airThermometer = new LevelSensor();
		hygrometer = new LevelSensor();
		photometer = new LevelSensor();
		turbidimeter = new LevelSensor();
		waterLevel = new ArrayList<LevelSensor>();
		waterOverflow = new ArrayList<StateSensor>();
		valves = new ArrayList<Valve>();
		for (int i = 0; i < nbTanks; i++) {
			waterLevel.add(new LevelSensor());
			waterOverflow.add(new StateSensor());
			valves.add(new Valve());
		}
	}

	/**
	 * Constructor of a named Reservoir
	 * @param name the value of the name parameter
	 */
	public Reservoir(String name) {
		super(name);
		incomingFlow = new LevelSensor();
		incomingPressure = new LevelSensor();
		outgoingFlow = new HashMap<LevelSensor, WaterStream>();
		presenceSensor = new StateSensor();
		electricState = new StateSensor();
		fanState = new StateSensor();
		waterThermometer = new LevelSensor();
		airThermometer = new LevelSensor();
		hygrometer = new LevelSensor();
		photometer = new LevelSensor();
		turbidimeter = new LevelSensor();
		waterLevel = new ArrayList<LevelSensor>();
		waterLevel.add(new LevelSensor());
		waterOverflow = new ArrayList<StateSensor>();
		waterOverflow.add(new StateSensor());
		valves = new ArrayList<Valve>();
		valves.add(new Valve());
	}

	
	@Override
	public String toString() {
		String res = "incoming flowmeter\n"+incomingFlow.toString()
					+"\nincoming manometer\n"+incomingPressure.toString()
					+"\noutgoing flowmeter\n"+outgoingFlow.toString()
					+"\npresence sensor\n"+presenceSensor.toString()
					+"\nelectric state\n"+electricState.toString()
					+"\nfan state\n"+fanState.toString()
					+"\nwater thermometer\n"+waterThermometer.toString()
					+"\nair thermometer\n"+airThermometer.toString()
					+"\nhygrometer\n"+hygrometer.toString()
					+"\nphotmeter\n"+photometer.toString()
					+"\nturbidimeter\n"+turbidimeter.toString();
		for (Sensor s :waterLevel ){
			res+="\nwater level sensor\n"+s.toString();
		}
		for (Sensor s :waterOverflow ){
			res+="\noverflow sensor\n"+s.toString();
		}
		for (Sensor s : valves){
			res+="\nvalve\n"+s.toString();
		}
		return super.toString()+"\n"+res;
	}
	
	public double getCapacity() {
		return capacity;
	}

	public void setCapacity(double volume) {
		this.capacity = volume;
		setDiameter(100);
		associatedView.updateForm();
	}

	public double getEffectiveVolume() {
		return effectiveVolume;
	}


	@Override
	public void addToNetwork(WaterDevice source) {
		incomingFlow.setLevel(0);
		incomingFlow.addToNetwork();
		incomingPressure.setLevel(0);
		incomingPressure.addToNetwork();
		outgoingFlow = new HashMap<LevelSensor, WaterStream>();

		presenceSensor.setOk(false);
		presenceSensor.addToNetwork();
		electricState.setOk(true);
		electricState.addToNetwork();
		fanState.setOk(false);
		fanState.addToNetwork();
		waterThermometer.addToNetwork();
		airThermometer.setLevel(20);
		airThermometer.addToNetwork();
		hygrometer.setLevel(11);
		hygrometer.addToNetwork();
		photometer.setLevel(0);
		photometer.addToNetwork();
		turbidimeter.setLevel(0);
		turbidimeter.addToNetwork();
		for (LevelSensor sensor : waterLevel) {
			sensor.setLevel(0);
			sensor.addToNetwork();
		}
		for (Valve valve : valves) {
			valve.setOpenRate(1);
			valve.addToNetwork();
		}
		for (StateSensor sensor : waterOverflow) {
			sensor.setOk(false);
			sensor.addToNetwork();
		}
		openValve();
		super.addToNetwork(source);
		drink(getMaxFlow());

	}

	@Override
	public void removeFromNetwork() {
		incomingFlow.removeFromNetwork();
		incomingPressure.removeFromNetwork();
		for (Sensor s : outgoingFlow.keySet()) {
			s.removeFromNetwork();
		}
		presenceSensor.removeFromNetwork();
		electricState.removeFromNetwork();
		fanState.removeFromNetwork();
		waterThermometer.removeFromNetwork();
		airThermometer.removeFromNetwork();
		hygrometer.removeFromNetwork();
		photometer.removeFromNetwork();
		turbidimeter.removeFromNetwork();
		for (LevelSensor sensor : waterLevel) {
			sensor.removeFromNetwork();
		}
		for (Valve valve : valves) {
			valve.removeFromNetwork();
		}
		for (StateSensor sensor : waterOverflow) {
			sensor.removeFromNetwork();
		}
		super.removeFromNetwork();
	}

	@Override
	public void addOutgoingDevice(WaterDevice device) {
		outgoingDevices.add(device);
		device.setUsed(true);
		
		synchronized (lock) {
			getOutStream().addOutgoing(device.getStream());
			
		}
		updateDiameter();
		LevelSensor s = new LevelSensor();
		s.addToNetwork();
		outgoingFlow.put(s, device.getStream());
	}

	@Override
	public  void updatedStream() {
		
		super.updatedStream();
		incomingFlow.setLevel(getStream().getIncomingFlow());
		incomingPressure.setLevel(getStream().getIncomingPressure());

		for (LevelSensor s : outgoingFlow.keySet()) {
			s.setLevel(outgoingFlow.get(s).getIncomingFlow());
		}

		waterThermometer.setLevel(getStream().getTemperature());
	}

	private synchronized void closeValve() {
		valves.get(0).setOpenRate(0);
		setUsed(false);
		getStream().updatePressure();


	}
	
	private synchronized void openValve() {
		valves.get(0).setOpenRate(1);
		setUsed(true);
		getStream().updatePressure();

	}

	@Override
	public void doSelectedAction(String selectedAction) {
		if (possibleActions.contains(selectedAction)) {
			if (selectedAction.contentEquals("Close Valve")) {
				closeValve();
				possibleActions.remove("Close Valve");
				possibleActions.add("Open Valve");
			} else if (selectedAction.contentEquals("Open Valve")) {
				openValve();
				possibleActions.remove("Open Valve");
				possibleActions.add("Close Valve");
			}

		}
	}
	
	@Override
	public double getRealSection() {
		return getSection() * valves.get(0).getOpenRate();

	}

	/**
	 * return the outgoing stream. If the outgoing stream does not exist, it is first created. 
	 * @return
	 */
	public WaterStream getOutStream() {
		if (outStream == null) {
			outStream = new WaterStream(this, 0, MAXPRESSURE);
			getStream().addOutgoing(outStream);
			
		}
		return outStream;
	}


	/**
	 * object for multithreading 
	 */
	Object fullTank = new Object();
	
	
	/**
	 * the reservoir fill its tanks when the outgoing flow is lower than the incoming flow.
	 *  The pressure in outgoing stream takes into account the water column of the tanks.  
	 */
	@Override
	public void run() {

		
		// wantToDrink is always true
		while (wantToDrink){
			
			if ((effectiveVolume < capacity||getOutStream().getOutgoingFlow()>0) && canDrink()) {

				
				if(effectiveVolume < capacity){

					getStream().setOutgoingFlow(getMaxFlow());
					//filling thread
					new Thread(){
							@Override
							public void run(){
								double consumption;
								long lastTime = System.currentTimeMillis();
								long time;
								double additionalVolume;
								while(effectiveVolume < capacity && canDrink()){
									time = System.currentTimeMillis() - lastTime;
	
									if(time>500){
										lastTime += time; 
										consumption= getMaxFlow() - getOutStream().getOutgoingFlow();
										 additionalVolume = consumption*time/3600000;
										//case of overflow
										if((effectiveVolume+additionalVolume) >capacity){
											waterOverflow.get(0).setOk(true);
											setEffectiveVolume (capacity);
											
										}
										else {
											if ((effectiveVolume+additionalVolume)<0){
												setEffectiveVolume (0);
											}
											else{
												setEffectiveVolume (effectiveVolume+additionalVolume);
											}
										}
										waterLevel.get(0).setLevel(effectiveVolume);
										
										
									}	
									synchronized (lock) {
										try {
											
											lock.wait(1000);
											
										} catch (InterruptedException e) {
											e.printStackTrace();
										}
										
									}
								}
								synchronized (fullTank) {
									fullTank.notify();
									
								}
								
							}
					}.start();
					while(effectiveVolume < capacity && canDrink()){
						synchronized (fullTank) {
							try {
								
								fullTank.wait();
								
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					}
				}
				//reservoir is fulfilled
				else{
					getStream().setOutgoingFlow(getOutStream().getOutgoingFlow());
					while(outStream.getOutgoingFlow()==getStream().getOutgoingFlow()&& canDrink())
					synchronized (lock) {
						try {
							lock.wait();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			}
			//reservoir fulfilled and no outgoing water
			else{
				getStream().setOutgoingFlow(0);
			}
		}
				
		
		
		
	}

	/**
	 * the effectiveVolume should be at most the reservoir capacity and at least 0. 
	 * @param effectiveVolume
	 */
	public void setEffectiveVolume(double effectiveVolume) {
		this.effectiveVolume = effectiveVolume;
		//getOutStream().setIncomingPressure(Math.max(super.getStream().getOutgoingPressure(outStream),
				//		effectiveVolume / 10 * Math.PI * Math.pow(2.2 , 2)));

		associatedView.updateForm();
	}




}
