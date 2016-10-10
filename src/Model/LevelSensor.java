package Model;

/**
 * Specific sensor class managing data with a continuous value set
 * @author fcalvier
 *
 */
public class LevelSensor extends Sensor{

	/**
	 * the numeric representation of the data
	 */
	private double level = 0;
	
	/**
	 * a storage for recurrent increment of the data value.
	 */
	private double increment = 0;
	
	

	
	public double getLevel() {
		return level;
	}

	public void setLevel(double d) {
		this.level = d;
		
	}
	
	public void setIncrement(double increment) {
		this.increment = increment;
		
	}
	
	
	/**
	 * decrement the level attribute from the given consumption according to the default time unit
	 * @param consumption in cubic meter per hour
	 */
	public void decrease(double consumption){
		level = Math.floor((level - consumption*Time.defaultTimeUnit())* 1000) / 1000;

	}

	/**
	 * increment the level attribute of the given consumption according to the default time unit
	 * @param consumption in cubic meter per hour
	 */
	public void increase(double consumption) {
		level = Math.floor((level + consumption*Time.defaultTimeUnit())* 1000) / 1000;
		
	}
	
	/**
	 * decrement the level attribute from the default consumption according to the default time unit
	 */
	public void decrease(){
		decrease(increment);
		
	}

	/**
	 * increment the level attribute of the default consumption according to the default time unit
	 */
	public void increase() {
		increase(increment);
	}
	
	/**
	 * Once measure action done, the level attribute is increased.  
	 */
	@Override
	public void measure() {
		super.measure();
		increase();
		
	}

	/**
	 * transformation of the level into String value.
	 */
	@Override
	protected String getData() {
		return ""+level;
	}
}
