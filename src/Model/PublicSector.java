package Model;

import java.util.ArrayList;

import View.Pipe;
/**
 * A public sector has additional sensors. The pressure of the stream and the noise due to the water
 * are measured.
 * @author fcalvier
 *
 */
public class PublicSector extends Sector {

	/**
	 * sensor
	 */
	LevelSensor pressure;
	/** 
	 * sensor 
	 */
	LevelSensor sonometer;
	
	/**
	 * height difference between the ends of the sector
	 */
	int drop;
	
	
	/**
	 * default constructor for an unnamed sector
	 */
	public PublicSector() {
		super();
		pressure = new LevelSensor();
		sonometer = new LevelSensor();
		
	}
	
	/**
	 * constructor specifying the name of the sector
	 * @param name the value of the name attribute
	 */
	public PublicSector(String name) {
		super(name);
		pressure = new LevelSensor();
		sonometer = new LevelSensor();
	}

	@Override
	public void addToNetwork() {
		super.addToNetwork();
		pressure.addToNetwork();
		sonometer.setLevel(0);
		sonometer.addToNetwork();
		
		
	}
	@Override
	public void removeFromNetwork() {
		super.removeFromNetwork();
		pressure.removeFromNetwork();
		sonometer.removeFromNetwork();
		
		
	}
	@Override
	public synchronized void updatedStream() {
		super.updatedStream();
		pressure.setLevel(getStream().getIncomingPressure());
		sonometer.setLevel(0);
	}
	
	/**
	 * recursively update the diameter of the sector and of its parents. the difference between 
	 * the diameter category of a sector and its children should not be more than 1
	 */
	
	@Override
	public void updateDiameter() {
		double outgoingMaxFlow =0;
		
		if (getMaxFlow() < outgoingMaxFlow){
			//augmenter le diametre
			for (WaterDevice wd : incomingDevices){
				wd.updateDiameter();
			}
		}
			
			float minOutgoingDiametreCategory = 6;
			
			for (WaterDevice wd : outgoingDevices){
				outgoingMaxFlow+= wd.getMaxFlow();
				minOutgoingDiametreCategory = Math.min(minOutgoingDiametreCategory,
						getDiameterCategory(wd.getDiameter()));
			}
			boolean diameterChange = false;
			double minDiameter = getMinDiameter(outgoingMaxFlow);
			if (outgoingMaxFlow>getMaxFlow()){
				diameterChange = true;
			}
			
			
			if(diameterChange){
				/* make the difference of diameter category between 
				 * a parent sector and a child sector be not more than 1
				 */
				if(getDiameterCategory(minDiameter)-minOutgoingDiametreCategory>1){
					insertSector(minDiameter);
					
				}
				
				/* 
				 * make the diameter of a sector be not more than the max size 
				 */
				else if (minDiameter> MAXDIAMCAT5){
					splitSector(getDiameterCategory(MAXDIAMCAT5));
					
				}
				
				//if the diameter has not be updated
				else{
					setDiameter(minDiameter);
				}

				
				for (WaterDevice wd : incomingDevices){
					wd.updateDiameter();
				}
			}

	}
	
	/**
	 * insert a PublicSector between this one and each one from its incomingDevices list.
	 * The devices from the outgoingDevices list are split according to their diameters.
	 * The biggest are plugged to the inserted Public Sector and the smallest are kept to this one
	 * @param minDiameter the diameter of the inserted publicSector
	 */
	private void insertSector(double minDiameter){
		
			//list WaterDevice from the outgoingDevices list to move into the inserted Sector
			float outgoingDiameter = 0;		
			ArrayList<WaterDevice>list = new ArrayList<WaterDevice>();
			for(WaterDevice device : outgoingDevices){
				//the biggest WaterDevice are at most in the diameter category below the diameter category
				//of this WaterDevice
				float temp =getDiameterCategory(minDiameter);
				if(temp - getDiameterCategory(device.getDiameter())<2){

					list.add(device);
				}
				else{
					outgoingDiameter += device.getDiameter();
				}

			}
			
			//if WaterDevices could be split according to theirs diameters
			//then a new sector is created and inserted
			if(!list.isEmpty()){
				PublicSector ps =  new PublicSector();
				ps.setDiameter(minDiameter);
				ps.addToNetwork();
				for(WaterDevice device : list){
					device.replaceIncomingDevice(this, ps);
					
				}

				ArrayList<WaterDevice>incomingDevicesCopy = new ArrayList<>();
				for(WaterDevice device : incomingDevices){
					incomingDevicesCopy.add(device);
				}
				for(WaterDevice device : incomingDevicesCopy){
					device.replaceOutgoingDevice(this, ps);
				}

				incomingDevices = new ArrayList<>();
				incomingDevices.add(ps);
				ps.addOutgoingDevice(this);
				ps.updatedStream();
				setDiameter(outgoingDiameter);
				((Pipe)associatedView).verticalSplit(ps);
				
			}
			//if all the WaterDevices have the same diameter category then this Sector is split
			else{
				setDiameter(outgoingDiameter);
				splitSector(getDiameterCategory(outgoingDiameter)-1);
				
			}
	}
	
	
	/**
	 * Split this Sector into two ones. The created one is added to every device from the incomingDevices
	 * list. Devices from the outgoingDevices are split according to their order. The new Public Sector
	 * receive as many outgoing devices as possible.
	 */
	private void splitSector(int maxDiamCat){
		//if one new Sector is not enough, repeat until the diameter of this Sector is in the correct category
		while(getDiameterCategory()>maxDiamCat){
			PublicSector ps =  new PublicSector();
			int index = 0;
			ps.setDiameter(0);
			//create a temporary list containing the WaterDevice which will be plugged to the new Sector
			ArrayList<WaterDevice>list = new ArrayList<WaterDevice>();
			while (index< outgoingDevices.size() && ps.getDiameterCategory()<= maxDiamCat){
				WaterDevice device = outgoingDevices.get(index);
				if(getDiameterCategory(ps.getDiameter()+device.getDiameter())<= maxDiamCat ){
					list.add(device);
					ps.setDiameter(ps.getDiameter()+ device.getDiameter() );
					setDiameter(getDiameter() - device.getDiameter());
				}
				
				index++;

			}
			
			//if no WaterDevice has a diameter smaller than the diameter of this Sector
			if(list.isEmpty()){
				System.err.println("a Sector has reached max size ");

				break;
			}
			else{
				

				//Then the new Sector is plugged to the network
				ps.addToNetwork();
				//first into the incoming devices
				for(WaterDevice device : incomingDevices){
					ps.incomingDevices.add(device);
					device.addOutgoingDevice(ps);
				}
				
				//then to the outgoing devices
				for(WaterDevice device : list){

					device.replaceIncomingDevice(this, ps);

				}
				
				ps.updatedStream();


				((Pipe)associatedView).lateralSplit(ps);
			}
		}
		
	}
}
