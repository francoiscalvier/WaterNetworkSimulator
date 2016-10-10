package Model;

/**
 * end point of the network. The min pressure is defined by the law and the diameter is normalized.
 * Thus, the max pressure and the max flow are defined
 * @author fcalvier
 *
 */

public class PrivateSector extends Sector {
	
	//Qmax = 7,82543 m3/h

	final static int MAXPRESSURE = 6;
	
	
	public PrivateSector(){
		//this can not be changed
		outgoingDevices = null;
		
		//this can be modified
		meter.setFrequency(1);
		setDiameter(32);
	}

	
	@Override
	public void addToNetwork(WaterDevice source) {
		super.addToNetwork(source);
		Network.add(this);
		
	}
	
	
}
