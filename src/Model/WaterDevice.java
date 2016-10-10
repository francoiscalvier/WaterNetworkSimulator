package Model;

import java.util.ArrayList;

import View.WaterComponent;


/**
 * a device containing water
 * 
 * @author fcalvier
 *
 */
public abstract class WaterDevice extends Device implements Runnable{

	/**
	 * number of instances of this class. Used for id
	 */
	private static int nbWaterDevices =0;

	/**
	 * list of devices supplied in water by this one
	 */
	protected ArrayList<WaterDevice> outgoingDevices;
	/**
	 * list of devices supplying in water this one
	 */
	protected ArrayList<WaterDevice> incomingDevices;
	/**
	 * water stream passing through this device
	 */
	private WaterStream stream;
	/**
	 * boolean expressing whether or not  the device is trying to extract water from its supply 
	 */
	protected Boolean wantToDrink;
	/**
	 * quantity of water this device is trying to extract. Expressed in cubic meter per hour
	 */
	private double consumption;

	/**
	 * a waterDevice is used when it is ready to receive water. 
	 */
	private boolean isUsed;
	/**
	 * id of the device
	 */
	private final int id;
	/**
	 * optional name of the device
	 */
	public String name;
	/**
	 * value of the max possible static pressure in the network 
	 */
	public double maxPressure;

	// arbitrary choosen values
	/**
	 * altitude difference between the edges of the water device. Can be positive or negative. Expressed in meters
	 * min value is -10 x MAXPRESSURE (otherwise, outgoing pressure is null)  
	 */
	protected int drop =0;
	/**
	 * diameter of the main pipe of the device. Expressed in millimeters
	 */
	private double diameter = 32;

	/**
	 * the section is a function of the diameter. Their values are updated together. both must be modified only 
	 * through the diameter setting method
	 */
	private double section = Math.PI*Math.pow(diameter/2,2);

	/**
	 * max diameter of a pipe
	 */
	final static int MAXDIAMETER = 1300;
	/**
	 * max pressure authorized in a network. Expressed in bars
	 */
	final static int MAXPRESSURE = 650;//8;
	/**
	 * min pressure . Expressed in bars
	 */
	final static int MINPRESSURE = 3;
	/**
	 * max water speed in the devices. Expressed in meter per seconds
	 */
	final static int MAXWATERSPEED = 2;
	/**
	 * min water speed in the supplied devices. Expressed in meter per seconds
	 */
	final static double MINWATERSPEED = 0.5;

	/**
	 * usual water speed in the supplied devices. Expressed in meter per seconds
	 */
	final static double NORMALWATERSPEED = 1.5;


	/**
	 * default constructor. The name attribute is set to id value
	 */
	public WaterDevice(){
		super();
		id = nbWaterDevices++;
		this.name = ""+id;
		outgoingDevices = new ArrayList<WaterDevice>();
		incomingDevices = new ArrayList<WaterDevice>();
		wantToDrink = false;

	}

	/**
	 * constructor to specify the name of the device
	 * @param name the value of the name attribute
	 */
	public WaterDevice(String name){
		this.name = name;
		id = nbWaterDevices++;
		outgoingDevices = new ArrayList<WaterDevice>();
		incomingDevices = new ArrayList<WaterDevice>();
		wantToDrink = false;

	}

	@Override
	public String toString() {
		String res = getClass().getSimpleName()+" "+id;
		if(!name.isEmpty()){
			res+="\n"+name;
		}
		res+="\n"+getDiameter();
		return res;
	}

	public double getDiameter() {
		return diameter;
	}

	
	/**
	 * set the diameter attribute and compute the section attribute
	 * @param d the value of the diameter attribute
	 */
	public void setDiameter(double d) {
		this.diameter = d;
		section = Math.PI *Math.pow(d/2,2);

	}

	/**
	 * @return the water stream associated to this device. If n stream is associated, then
	 * it is created
	 */
	public WaterStream getStream() {
		if(stream == null){
			stream = new WaterStream(this);
		}
		return stream;
	}

	public void setStream(WaterStream stream) {
		this.stream = stream;
	}


	/**
	 * add a device to the set of outgoing devices and connect the respecting streams. If necessary, the 
	 * diameter of the current device is increased
	 * @param device the device to add
	 */
	public void addOutgoingDevice(WaterDevice device){

		outgoingDevices.add(device);
		device.setUsed(true);
		getStream().addOutgoing(device.getStream());
		updateDiameter();

	}

	/**
	 * ensure that the diameter of the current device is sufficient for the needed flow. If not, 
	 * the diameter is increased and the method is recursively called on parents devices
	 */
	public void updateDiameter(){
		double outgoingMaxFlow =0;
		for (WaterDevice wd : outgoingDevices){
			outgoingMaxFlow+= wd.getMaxFlow();
		}
		if (getMaxFlow() < outgoingMaxFlow){
			setDiameter(getMinDiameter(outgoingMaxFlow));
			for (WaterDevice wd : incomingDevices){
				wd.updateDiameter();
			}
		}
	}

	public double getSection(){
		return section;
	}

	/**
	 * add the device to the network. Ensure that the incoming pressure is correct. If not, a 
	 * Controler is added before to regulate the pressure. 
	 */
	
	public void addToNetwork(WaterDevice source) {
		maxPressure = source.maxPressure + drop/10. ;
		//- Physic.chargeLoss(outgoingDevices.size(), diameter, Physic.flow(2, MAXDIAMETER));
		boolean added = false;
		/*
		  control of the incoming pressure and controler addition. 
		
		if(maxPressure>MAXPRESSURE){
			Controler c = new Controler();
			c.addToNetwork(source);
			addToNetwork(c);
			added = true;
			((WaterComponent)associatedView).verticalSplit(c);
		}
		if (maxPressure<MINPRESSURE){
			
			Booster b = new Booster();
			b.addToNetwork(source);
			addToNetwork(b);
			added = true;
			((WaterComponent)associatedView).verticalSplit(b);
		}*/
		if(!added){
			incomingDevices.add(source);
			source.addOutgoingDevice(this);
			Network.add(this);
			updatedNetwork();
			updatedStream();
		}
	}

	/**
	 * remove the device from the network and connect outgoing devices on incoming devices
	 */
	@Override
	public void removeFromNetwork() {

		for(WaterDevice device :incomingDevices ){
			device.outgoingDevices.remove(this);
		}
		incomingDevices = new ArrayList<WaterDevice>();
		for (WaterDevice device : outgoingDevices){
			device.incomingDevices = null;
		}
		getStream().remove();
		Network.remove(this);

	}

	/**
	 * check if the water device is linked to a water source and if the water actually come into the
	 * water device
	 * @return true if the water device is actually linked to a water source. False otherwise
	 */
	public boolean canDrink(){
		return getStream().getIncomingPressure()>0;

	}

	/**
	 * default drink method caller which make the device consume water. The default consumption is 
	 * the max flow of the device according to its diameter and the middle water speed.
	 * 
	 */
	public void drink(){

		drink(Physic.flow(1.5, diameter/1000));
	}

	
	/**
	 * the drink method make the device consume water with a specified flow for a specified duration.
	 * @param consumption the consumption flow (in cubic meters per hour)
	 * @param duration the consumption duration (in milliseconds)
	 */
	public void drink(double consumption, long duration){
		if(!wantToDrink){
			drink(consumption);

			try {
				Thread.sleep(duration);
				stopDrink(consumption);
				t.join();

			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

	
	/**
	 * the drink method make the device consume water for a specified duration. The consumption flow is
	 * preset (consumption attribute)
	 * @param duration the consumption duration (in milliseconds)
	 */
	public  void drink(long duration){
		if(!wantToDrink){

			drink();
			try {
				Thread.sleep(duration);
				stopDrink();
				t.join();

			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}


	}

	/**
	 * the drink method make the device consume water with a specified flow. The device will consume 
	 * water until the call of the stopDrink method
	 * @param consumption the consumption flow (in cubic meters per hour)
	 */
	public void drink(double consumption){

		if(!wantToDrink){
			this.consumption = consumption;	
			wantToDrink = true;
			t =new Thread(this);
			t.start();
		}
		else{
			synchronized (lock) {
				//this.consumption += consumption;
				lock.notifyAll();

			}
		}
	}


	/**
	 * stop the water consumption of the device.
	 */
	public void stopDrink(){
		wantToDrink =false;
		synchronized (lock) {
			lock.notifyAll();

		}
	}

	/**
	 * reduce the water consumption of the device
	 * @param consumption the consumption reduction (in cubic meters per hour)
	 */
	public void stopDrink(double consumption){
		this.consumption -= consumption;	
		if(this.consumption==0){
			stopDrink();

		}


	}

	
	/**
	 * reduce the water consumption of the device after a given amount of time
	 * @param consumption the consumption reduction (in cubic meters per hour)
	 * @param duration the amount of time (in milliseconds) before the consumption reduction
	 */
	
	public void stopDrink(double consumption , long duration){
		try {
			Thread.sleep(duration);
			stopDrink(consumption);
			t.join();

		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}




/**
 * stop the water consumption of the device after a given amount of time.
 * @param duration the amount of time (in milliseconds) before the consumption halt
 */
	public void stopDrink(long duration){
		try {
			Thread.sleep(duration);
			stopDrink();
			t.join();

		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	/**
	* the running action of a water device is to consume water. A water device consume water as 
	* long as needed (wantToDrink) and as long as possible (canDrink()).
	*/
	@Override
	public  void run() {

		while(wantToDrink){
			try {

				if (canDrink()){
					getStream().modifyOutgoingFlow( consumption);
					while(wantToDrink && canDrink()){

						synchronized (lock) {
							lock.wait();
						}


					}
					getStream().modifyOutgoingFlow( - consumption);

				}


			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}


	}


	/**
	 * Method called each time the associated stream evolve. Used to notify the related view
	 */
	public void updatedStream(){
		if(((WaterComponent)getAssociatedView())!=null){
			((WaterComponent)getAssociatedView()).updateColor();
		}

		synchronized (lock) {
			lock.notify();

		}
	}

	/**
	 * lock object for multi threading
	 */
	Object lock = new Object();


	/**
	 * Method called each time the associated stream evolve. Used to notify the related view
	 */
	public void updatedNetwork(){
		if(((WaterComponent)getAssociatedView())!=null){
			((WaterComponent)getAssociatedView()).updateForm();
			((WaterComponent)getAssociatedView()).updateColor();

		}
	}


	/**
	 * Effective section of the Device (i.e. the section taking into account the open rate)
	 * @return the section of the device
	 */
	public double getRealSection() {
		return getSection();

	}


	/**
	 * Method that replace a WaterDevice from the incomingDevices list by another WaterDevice.
	 * IncomingDevices list of this WaterDevice and both outgoingDevices lists of the oldWD and newWD
	 * are updated
	 * If oldWD id not in the incomingDevices, nothing is done
	 * @param oldWD the waterDevice to replace
	 * @param newWD the new WaterDevice to put in place of oldWD
	 */
	public void replaceIncomingDevice(WaterDevice oldWD, WaterDevice newWD){
		int index = incomingDevices.indexOf(oldWD);
		if(index >-1){
			incomingDevices.add(index, newWD);
			incomingDevices.remove(oldWD);
			oldWD.outgoingDevices.remove(this);
			newWD.outgoingDevices.add(this);
			getStream().replaceIncomingStream(oldWD.getStream(),newWD.getStream());
		}


	}

	/**
	 * Method that replace a WaterDevice from the outgoingDevices list by another WaterDevice.
	 * OutgoingDevices list of this WaterDevice and both incomingDevices lists of the oldWD and newWD
	 * are updated
	 * If oldWD id not in the outgoingDevices, nothing is done
	 * @param oldWD the waterDevice to replace
	 * @param newWD the new WaterDevice to put in place of oldWD
	 */
	public void replaceOutgoingDevice(WaterDevice oldWD, WaterDevice newWD){
		int index = outgoingDevices.indexOf(oldWD);
		if(index >-1){
			outgoingDevices.remove(oldWD);
			outgoingDevices.add(index, newWD);
			oldWD.incomingDevices.remove(this);
			newWD.incomingDevices.add(this);
			getStream().replaceOutgoinStream(oldWD.getStream(),newWD.getStream());
		}


	}
	
	/**
	 * access to the outgoingDevices attribute used by the GUI
	 * @return
	 */
	public ArrayList<WaterDevice> getOutgoingDevices() {
		return outgoingDevices;
	}

	/**
	 * flow through a WaterDevice with a water speed of 2 m/s
	 * @return flow in cubic meter per hour
	 */
	public double getMaxFlow(){
		return  Physic.flow(MAXWATERSPEED, diameter/1000);
	}

	
	/**
	 * minimal diameter of a water device for a given flow 
	 * @param flow flow in cubic meter per hour
	 * @return the required diameter (in meters)
	 */
	public double getMinDiameter(double flow ){
		return  Physic.getDiameter(MAXWATERSPEED, flow);
	}

	public boolean isUsed() {
		return isUsed;
	}

	public void setUsed(boolean isUsed) {
		this.isUsed = isUsed;
	}

}
