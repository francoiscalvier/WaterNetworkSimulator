package Model;
/**
 * Pumping group which use a bladder to maintain the pressure. 
 * not tested
 * @author fcalvier
 *
 */
public class Booster extends PumpingGroup{
	
	/**
	 * sensor relative to the bladder
	 */
	LevelSensor bladderPressure;
	
	public Booster(){
		super();
		bladderPressure = new LevelSensor();
	}
	
	@Override
	public void addToNetwork() {
		bladderPressure.setLevel(0);
		bladderPressure.addToNetwork();
		super.addToNetwork();
	}
	
	@Override
	public void removeFromNetwork() {
		bladderPressure.removeFromNetwork();
		super.removeFromNetwork();
	}
	
	@Override
	public void pump(){
		bladderPressure.increase(1);
		super.pump();
	}
	@Override
	protected boolean canPump() {
		return super.canPump()&& bladderPressure.getLevel()<MINPRESSURE;
	}
	@Override
	protected void modifyStreamPressure() {
		getOutStream().setIncomingPressure(getStream().getIncomingPressure() + 1);
	}
}
