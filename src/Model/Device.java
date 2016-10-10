package Model;

import java.util.ArrayList;

import View.Component;

public abstract class Device {
	
	/**
	 * link with the user interface
	 */
	Component associatedView;
	
	/**
	 * list of possible actions of a given device. Used by the user interface.   
	 */
	ArrayList<String>possibleActions= new ArrayList<String>();
	
	
	

	/**
	 * thread for device action (water/energy consumption/production) 
	 */
	Thread  t ;

	/**
	 * add the device to the network list of devices 
	 */
	public void addToNetwork (){
		Network.add(this);
	}
	
	/**
	 * remove the device from the network list of devices 
	 */
	public void removeFromNetwork (){
		Network.remove(this);
		
	}
	
	public void setAssociatedView(Component associatedView) {
		this.associatedView = associatedView;
	}
	public Component getAssociatedView() {
		return associatedView;
	}

	/**
	 * call the action associated to the parameter value.
	 * @param selectedAction a String from the possibleActions list.
	 */
	public void doSelectedAction(String selectedAction){
		
	}
	
	public ArrayList<String> getPossibleActions() {
		return possibleActions;
	}
}
