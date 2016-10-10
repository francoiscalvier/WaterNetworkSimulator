package Model;
/**
 * Specific Sensor class managing data with boolean values
 * @author fcalvier
 *
 */
public class StateSensor extends Sensor{

	/**
	 * the boolean representation of the data 
	 */
	private boolean isOk;

	public boolean isOk() {
		return isOk;
	}

	public void setOk(boolean isOk) {
		this.isOk = isOk;
	}

	/**
	 * transformation of the boolean value to String value
	 */
	@Override
	protected String getData() {
		return ""+isOk;
	}

}
