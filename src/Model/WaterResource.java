package Model;

/**
 * specific water device with no parent 
 * @author fcalvier
 *
 */
public class WaterResource extends WaterDevice{

	/**
	 * default constructor, the name attribute is set to the empty string
	 */
	public WaterResource (){
		maxPressure = MAXPRESSURE;
		setStream(new WaterStream(this, 0, MAXPRESSURE));
	}
	
	/**
	 * Constructor specifying the name attribute value
	 * @param name the name of the resource
	 */
	public WaterResource(String name) {
		maxPressure = MAXPRESSURE;
		setStream(new WaterStream(this, 0, MAXPRESSURE));
		this.name = name;
	}
	@Override
	public void addToNetwork() {
		
		Network.add(this);
		setUsed(true);
		possibleActions.add("Empty source");
		
	}
	
	@Override
	public void removeFromNetwork() {
		
		Network.remove(this);
		
	}
	
	
	
	@Override
	public void doSelectedAction(String selectedAction) {
		if(possibleActions.contains(selectedAction)){
			if(selectedAction.contentEquals("Empty source")){
				getStream().setIncomingPressure(0);
				possibleActions.remove("Empty source");
				possibleActions.add("Fill source");
			}
			else if(selectedAction.contentEquals("Fill source")){
				getStream().setIncomingPressure(MAXPRESSURE);
				possibleActions.remove("Fill source");
				possibleActions.add("Empty source");
			}
				
		}
	}
}
