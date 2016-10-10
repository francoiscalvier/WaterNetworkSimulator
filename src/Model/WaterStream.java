package Model;

import java.util.ArrayList;
import java.util.HashMap;
/**
 * class representing a node in the water stream graph of the network. Responsible for up to down
 * propagation of the pressure and for down to up propagation of the flow. 
 * @author fcalvier
 *
 */
public class WaterStream{
	/**
	 * value of the incoming flow
	 */
	private double incomingFlow;
	/**
	 * value of the sum of outgoing flows
	 */
	private double outgoingFlow;
	/**
	 * outgoing flows allocation
	 */
	private HashMap<WaterStream, Double> outgoingFlowMap;
	/**
	 * outgoing pressures allocation
	 */
	private HashMap<WaterStream, Double> outgoingPressureMap;
	/**
	 * children list in the stream graph
	 */
	private ArrayList <WaterStream> outgoingStream;
	/**
	 * value of the incoming pressure
	 */
	private double incomingPressure;
	/**
	 * value of the remaining pressure (incoming pressure - pressure loss in the device)
	 */
	private double staticPressure;
	/**
	 * temperature of the water. Not managed for the moment
	 */
	private double temperature;
	/**
	 * device through which the water stream is going 
	 */
	private WaterDevice associatedWD;
	/**
	 * parent stream in the stream graph
	 */
	private WaterStream incomingStream;
	
/**
 * default constructor for unbound stream
 * @param wd the associated water device which must be created befor the stream
 */
	public WaterStream(WaterDevice wd){
		associatedWD = wd;
		outgoingStream = new ArrayList <WaterStream>();
		synchronized(flowLock){
			incomingFlow=0;
		}
		outgoingFlow=0;
		outgoingFlowMap = new HashMap<WaterStream, Double>();
		incomingPressure=0;
		outgoingPressureMap = new HashMap<WaterStream, Double>();
		setTemperature(20);		
	}

	/**
	 * constructor for unbound stream with known pressure and flow 
	 * @param wd the associated water device which must be created befor the stream
	 * @param flow the incoming and outgoing flow value 
	 * @param pressure the incoming pressure value
	 */
	public WaterStream(WaterDevice wd, double flow, double pressure){
		associatedWD = wd;
		outgoingStream = new ArrayList <WaterStream>();
		this.incomingFlow=flow;
		this.outgoingFlow=flow;
		outgoingFlowMap = new HashMap<WaterStream, Double>();
		this.outgoingPressureMap = new HashMap<WaterStream, Double>();
		incomingPressure=pressure;
		setTemperature(20);		
	}




	@Override
	public String toString() {
		return "Flow: " +getIncomingFlow() 
		+"\nPressure: "+getIncomingPressure() 
		+"\ntemperature: " + getTemperature();


	}

	/**
	 * add an outgoing stream then updating temperature, flow and pressure
	 * @param ws the outgoing stream to add
	 */
	public void addOutgoing(WaterStream ws){
		ws.incomingStream = this;
		ws.setIncomingPressure(getOutgoingPressure(ws));
		ws.setTemperature(getTemperature());	
		outgoingFlowMap.put(ws,  ws.getIncomingFlow());
		setOutgoingFlow(getOutgoingFlow()+ws.getIncomingFlow());




	}


	/**
	 * The WaterStream child responsible for the update is given in parameter. The outgoing flow
	 * part due to this child is compared to its registered value. If different, the new value is
	 * registered and this waterStream flow is updated.
	 * @param outgoingStream the child responsible for the flow update
	 */
	private void updateFlow(WaterStream outgoingStream){
		double deltaFlow = outgoingStream.getIncomingFlow() - outgoingFlowMap.get(outgoingStream);
		if(deltaFlow!=0){
			outgoingFlowMap.put(outgoingStream, outgoingStream.getIncomingFlow());
			setOutgoingFlow(getOutgoingFlow() + deltaFlow);
		}
	}
	/**
	 * Recursive part of the flow updateFlow method. This is a bottom-up algorithm. incoming 
	 * and outgoing flow must be equal. Otherwise, incoming flow is set to outgoing flow and
	 * the delta demand is propagated to the incoming WaterStream.
	 */
	private void updateFlow(){
		double deltaFlow=getOutgoingFlow() - getIncomingFlow();
		if(deltaFlow!=0){
			setIncomingFlow(outgoingFlow);
			if(incomingStream!=null){
				incomingStream.updateFlow(this);
				
			}
		}		
	}

	/**
	 * pressure update method. the static pressure is updated according to the height difference 
	 * between the ends of the device the pressure update is propagated recursively to the children
	 *  streams
	 */
	public synchronized void updatePressure() {
		if(associatedWD.isUsed() && incomingPressure !=0){
			
			this.staticPressure = incomingPressure + associatedWD.drop/10;
		}
		else {
			this.staticPressure =0;
				
		}
		for(WaterStream ws : outgoingPressureMap.keySet()){
			updatePressure(ws);
			
		}

	}

	/**
	 * recursive part of the pressure update method
	 * @param ws the children stream to update
	 */
	private synchronized void updatePressure(WaterStream ws) {
		double deltaPressure = getOutgoingPressure(ws) - Math.max(staticPressure-getChargeLoss(ws),0);
		if(deltaPressure!=0){
			outgoingPressureMap.replace(ws, getOutgoingPressure(ws) - deltaPressure);
			ws.setIncomingPressure(getOutgoingPressure(ws));

		}	
		
		
	}


	
	public synchronized double getOutgoingFlow() {
		return outgoingFlow;
	}

	/**
	 * lock object for multiThread usage
	 */
	Object flowLock = new Object();
	
	
	 public void setOutgoingFlow(double d) {
		 synchronized(flowLock){
			this.outgoingFlow = d;
			updateFlow();
		}
	}
	 
	/**
	 * increase the outgoing flow value of the given parameter 
	 * @param d the increment
	 */
	public  void modifyOutgoingFlow(double d) {
		setOutgoingFlow(outgoingFlow + d);
		
	}

	
	/**
	 * 
	 * @param ws a child stream 
	 * @return the outgoing pressure relative to the child stream ws. If ws is not a child stream, 
	 * it is first added and its pressure is initialized. 
	 */
	synchronized double getOutgoingPressure(WaterStream ws) {
		if(!outgoingPressureMap.containsKey(ws)){
			
			outgoingPressureMap.put(ws, 0.);
			outgoingStream.add(ws);
			if(associatedWD.isUsed()&&incomingPressure!=0){
				this.staticPressure = incomingPressure+associatedWD.drop/10;
				updatePressure(ws);
			}
			
		}
		
		
		return outgoingPressureMap.get(ws);
	}

	public double getIncomingPressure() {
		return incomingPressure;
		
		
	}

	 public  void setIncomingPressure(double pressure) {
		 this.incomingPressure = pressure;
		associatedWD.updatedStream();
		updatePressure();
	}

	
	/**
	 * compute the charge loss of the stream given in parameter
	 * @param ws a child stream
	 * @return the charge loss of the ws stream.
	 */
	private double getChargeLoss(WaterStream ws){
		return Physic.chargeLoss(outgoingStream.indexOf(ws)+1, associatedWD.getDiameter()/1000,
						outgoingFlow);
		

	}

	public double getTemperature() {
		return temperature;
	}

	public void setTemperature(double d) {
		this.temperature = d;
	}

	public double getIncomingFlow() {
		synchronized(flowLock){
			return incomingFlow;
		}
	}

	 private void setIncomingFlow(double d) {
		 synchronized(flowLock){ 
			 this.incomingFlow = d;
			 associatedWD.updatedStream();
		 }
	}
	



	 /**
	  * this method remove a given outgoing stream
	  * @param ws the stream to remove
	  */
	private void removeOutgoing(WaterStream ws){
		setOutgoingFlow(getOutgoingFlow()-ws.getIncomingFlow());
		outgoingFlowMap.remove(ws);
		outgoingPressureMap.remove(ws);
		int index = outgoingStream.indexOf(ws);
		outgoingStream.remove(ws);
		while(index<outgoingStream.size() ){
			updatePressure(outgoingStream.get(index));
		}
	}
	
	

	
	/**
	 * this method replace an incoming stream by another one and update flow and pressure values
	 * @param oldStream the stream to remove
	 * @param newStream the stream to add
	 */
	public void replaceIncomingStream(WaterStream oldStream, WaterStream newStream) {
		oldStream.removeOutgoing(this);
		newStream.addOutgoing(this);
	}

	
	/**
	 * this method replace an outgoing stream by another one and update flow and pressure values
	 * @param oldStream the stream to remove
	 * @param newStream the stream to add
	 */
	public void replaceOutgoinStream(WaterStream oldStream, WaterStream newStream) {
		outgoingFlowMap.remove(oldStream);
		outgoingFlowMap.put(newStream,oldStream.getIncomingFlow());
		setOutgoingFlow(getOutgoingFlow() + newStream.getIncomingFlow() - oldStream.getIncomingFlow());
		newStream.setIncomingPressure(getIncomingPressure());
		newStream.incomingStream = this;

	}

	/**
	 * this method remove this WaterStream from the network. Outgoing WaterStreams 
	 * are added to the incoming ones. Global outgoing pressure and flow of the incoming
	 * WaterStream are assumed unmodified 
	 */
	public void remove() {
		incomingStream.outgoingFlowMap.remove(this);
		incomingStream.outgoingPressureMap.remove(this);



		for(WaterStream ws : outgoingFlowMap.keySet()){
			incomingStream.outgoingFlowMap.put(ws, ws.incomingFlow);
			ws.setIncomingPressure(getIncomingPressure());
		}
	}

	
	
}
