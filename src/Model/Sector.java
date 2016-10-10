package Model;

import java.util.HashMap;

/**
 * A part of the network which correspond to a continuous set of pipes having the same diameter
 * and which possess a meter and a valve 
 * 
 * @author fcalvier
 *
 */
public class Sector extends WaterDevice{
	
	/**
	 * known diameters of pipes. Expressed in millimeters 
	 */
	static final int[] existingDiameters = {6,8,10,15,20,25,32,40,50,65,80,100,125,150,200,250,300
			,350,400,450,500,550,600,650,700,750,800,900,1000,1050,1100,1200,1300,1400
			,1500,1600,1700,1800,1900,2000,2200};
	
	/**
	 * fore-computed maxFlow values for known diameters of pipes
	 */
	public static final HashMap<Integer, Double>maxFlow;
	
	static{
		maxFlow = new HashMap<Integer, Double>();
		for(int index =0; index < existingDiameters.length;index++){
			int diameter = existingDiameters[index];
			//diameter expressed in meters
			maxFlow.put(diameter,Physic.flow(MAXWATERSPEED, diameter/1000));
		}
	};
		
	
	//information about pipe material. MaxFlow should depends on the material
	//acier >500
	//fonte 100 - 300
	//pvc/pehd <100 
	
	// information about ideal flow in some real network 
	// 0,5m/s < vitesse < 2m/s
	
	// mathematical formulae 
	// Q = 3600 Pi v Math.power(d/2,2)
	
	// v = Q/(3600 Pi * Math.power(d/2),2)
	// v default = 1.5m/s
	//d = Math.sqrt(q/(3600v) *4/Math.Pi)
	//dmin = Math.sqrt(q/(1800 * Math.Pi))
	
	/**
	 * sensor measuring the water volume passing through the sector
	 */
	LevelSensor meter;
	
	/**
	 * valve permitting to close water incoming in the sector
	 */
	Valve sectoringValve;
	
	//information about the water network of St Etienne 
	//103439m entre 0 et 80 mm
	//210339m entre 80 et 150 mm
	//180911m entre 150 et 250 mm
	//122077m entre 250 et 500 mm
	//82316m entre 500 et 1300 mm
	
	
	final static double MAXDIAMCAT1 = 80;
	final static double MAXDIAMCAT2 = 150;
	final static double MAXDIAMCAT3 = 250;
	final static double MAXDIAMCAT4 = 500;
	final static double MAXDIAMCAT5 = 1300;
	
	/**
	 * default constructor. The name attribute is set to empty string
	 */
	public  Sector() {
		meter = new LevelSensor();
		sectoringValve = new Valve();
		meter.setFrequency(1);
		
		
	}
	
	/**
	 * constructor of a named Sector
	 * @param name the value of the name attribute
	 */
	public  Sector(String name) {
		super(name);
		meter = new LevelSensor();
		sectoringValve = new Valve();

		
	}
	
	@Override
	public String toString() {
		
		return super.toString()+"\nmeter\n"+ meter.toString()+"\nvalve\n"+ sectoringValve.toString();
	}
	
	/**
	 * the components of the sector are added to the network and the valve is set to open
	 */
	@Override
	public void addToNetwork(WaterDevice source) {
		
		meter.setLevel(0);
		meter.setFrequency(1);
		meter.addToNetwork();
		sectoringValve.setOpenRate(1);
		sectoringValve.addToNetwork();
		openValve();
		super.addToNetwork(source);
		
	}
	
	/**
	 * the components of the sector are added to the network and the valve is set to open
	 */
	@Override
	public void addToNetwork() {
		meter.setLevel(0);
		meter.setFrequency(1);
		meter.addToNetwork();
		sectoringValve.setOpenRate(1);
		sectoringValve.addToNetwork();
		openValve();
		super.addToNetwork();
		
	}
	
	/**
	 * the components of the sector are removed from the network
	 */
	
	@Override
	public void removeFromNetwork() {
		super.removeFromNetwork();
		meter.removeFromNetwork();
		sectoringValve.removeFromNetwork();
		
	}
	
	/**
	 * the real section depends on the opening rate of the valve.
	 */
	@Override
	public double getRealSection() {
		return getSection() * sectoringValve.getOpenRate();
	}
	
	
	/**
	 * update the meter information according to the stream passing through the sector
	 */
	
	@Override
	public synchronized void updatedStream() {
		super.updatedStream();
		meter.setIncrement(getStream().getOutgoingFlow());
		
	}
	
	/**
	 * @return the category corresponding to the diameter of the sector 
	 */
	public int getDiameterCategory(){
		return getDiameterCategory(getDiameter());
	}
	
	/**
	 * 
	 * @param size a diameter expressed in mm 
	 * @return the category corresponding to the diameter given in parameter
	 */
	public int getDiameterCategory(double size){
		if(size<MAXDIAMCAT1){
			return 1;
		}
		 if(size<MAXDIAMCAT2){
			return 2;
		}
		 if(size<MAXDIAMCAT3){
			return 3;
		}
		 if(size<MAXDIAMCAT4){
			return 4;
		}
		 if(size<MAXDIAMCAT5){
			return 5;
		}
		else return 6;
	}
	
	
	/**
	 * close the sector valve and update the information
	 */
	public void closeValve(){
		sectoringValve.setOpenRate(0);
		setUsed(false);
		getStream().updatePressure();
		
		
	}
	
	/**
	 * open the sector valve and update the information
	 */
	public void openValve() {
		sectoringValve.setOpenRate(1);
		setUsed(true);
		getStream().updatePressure();
	}
	
	

	@Override
	public void doSelectedAction(String selectedAction) {
		if(possibleActions.contains(selectedAction)){
			if(selectedAction.contentEquals("Close Valve")){
				closeValve();
				possibleActions.remove("Close Valve");
				possibleActions.add("Open Valve");
			}
			else if(selectedAction.contentEquals("Open Valve")){
				openValve();
				possibleActions.remove("Open Valve");
				possibleActions.add("Close Valve");
			}
				
		}
	}
	
	/**
	 * set the sector diameter to the first existing diameter upper than he  diameter given
	 * in parameter 
	 */
	@Override
	public void setDiameter(double diameter) {
		int i =0;
		double res = 0;
		do {
			res = existingDiameters[i];
			i++;
		}
		while(i<existingDiameters.length && res<diameter);
		
		
		super.setDiameter(res);
	}
	
	@Override
	public double getMaxFlow(){

		return  maxFlow.get((int)getDiameter());
	}
	
	
	
	
}
