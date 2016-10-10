package Model;


/** 
 * Class with physic laws implementation 
 * @author fcalvier
 *
 */
public final class Physic {

	
	/**
	 * flow calculus
	 * @param speed  the water speed in meter per second
	 * @param diameter the diameter of the waterDevice in meters
	 * @return the flow in cubic meters per hour value of a stream passing through a device of the given diameter at the given speed  
	 */
	static final double flow(double speed, double diameter){
		return  3600 * speed *Math.PI *Math.pow(diameter/2.,2);
	}
	
	/**
	 * water speed calculus of a stream passing through a device of the given diameter with the given 
	 * flow 
	 * @param flow flow of a stream passing through a device in cubic meters per hour
	 * @param diameter diameter of the device in meters
	 * @return the flow speed in meters per second 
	 */
	static final double waterSpeed(double flow, double diameter){
		return flow/(3600*Math.PI *Math.pow(diameter/2.,2));
	}
	
	/**
	 * compute charge loss in water pipe
	 * @param length of the pipe in meters
	 * @param diameter of the pipe in meters
	 * @param flow goigng through the pipe in cubic meters per hour
	 * @return charge loss expressed in Bar
	 */
	static final double chargeLoss(double length, double diameter, double flow){
		return length *Math.pow(waterSpeed(flow, diameter),2)
				/(2*diameter*100);

	}

	/**
	 * minimal required diameter for a given flow and speed couple
	 * @param speed in meters per second
	 * @param flow in cubic meters per hour
	 * @return
	 */
	static final double getDiameter(double speed, double flow ){
		return  Math.sqrt(4* flow/(3600 * speed * Math.PI));
	}
	
	
}
