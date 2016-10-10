package Model;

/**
 * Special sensor similar to level sensor but without increment attribute.
 * @author fcalvier
 *
 */
public class Valve extends Sensor{
	/**
	 * the numeric representation between 0 and 1 of the data. 0 is a closed valve and 1 is an open
	 *  valve.
	 */
	private double openRate;



	public Valve() {
		super();
		setOpenRate(0);
	}

	public double getOpenRate() {
		return openRate;
	}

	public void setOpenRate(double openRate) {
		
		this.openRate = Math.max(0, Math.min(1, openRate));

	}

	@Override
	protected String getData() {
		return ""+openRate;
	}

}
