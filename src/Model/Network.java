package Model;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 
 * @author fcalvier
 *
 */
public class Network  {
	
	/**
	 * list of the devices composing the network
	 */
	private static ArrayList<Device> deviceList = new ArrayList<Device>();
	/**
	 * list of the sensors in the network and their associated date of last data sending
	 */
	private static HashMap<Sensor,Long> sensorList = new HashMap<Sensor,Long>(); 
	/**
	 * list of the water devices composing the network
	 */
	private static ArrayList<WaterDevice> waterDeviceList = new ArrayList<WaterDevice>();
	/**
	 * list of the clients of the water network
	 */
	private static ArrayList<PrivateSector> clientList = new ArrayList<PrivateSector>();

	public static ArrayList<Device> getDeviceList() {
		return deviceList;
	}
	
	public static HashMap<Sensor, Long> getSensorList() {
		return sensorList;
	}
	public static ArrayList<WaterDevice> getWaterDeviceList() {
		return waterDeviceList;
	}
	
	
	public static ArrayList<PrivateSector> getClientList() {
		return clientList;
	}

	/**
	 * Constructor initializing every list
	 */
	public Network (){
		deviceList = new ArrayList<Device>();
		sensorList = new HashMap<Sensor, Long>();
		waterDeviceList = new ArrayList<WaterDevice>();
		
	}
	
	public static void add(Device device){
		deviceList.add(device);
		
	}
	public static void add(WaterDevice device){
		deviceList.add(device);
		waterDeviceList.add(device);
		
	}
	public static void add(Sensor device){
		deviceList.add(device);
		sensorList.put(device,0l);
		
		
	}
	public static void add(PrivateSector device){
		clientList.add(device);
		
	}
	
	public static void remove(Device device){
		deviceList.remove(device);
		
	}
	public static void remove(WaterDevice device){
		deviceList.remove(device);
		waterDeviceList.remove(device);
		
	}
	public static void remove(Sensor device){
		deviceList.remove(device);
		sensorList.remove(device);
		
	}
	
	
	public static void remove (PrivateSector device){
		deviceList.remove(device);
		waterDeviceList.remove(device);
		clientList.remove(device);
		
	}
	
}
