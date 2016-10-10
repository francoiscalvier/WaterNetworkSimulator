package View;

import Controler.NetworkDisplay;
import Model.Device;
import Model.Reservoir;
import Model.WaterDevice;
import Model.WaterStream;
import javafx.scene.control.Tooltip;
/**
 * pipe surrounded by a water tower
 * @author fcalvier
 *
 */
public class WaterTowerPipe extends Pipe{


	private WaterTower tower;
	
	
	public WaterTowerPipe(String name) {
		super(name);
	}
	
	@Override
	public void setEmpty() {
		super.setEmpty();
		tower.setEmpty();

	}
	@Override
	public void setFull() {
		super.setFull();
		tower.setFull();
	}
	
	@Override
	public void setConsuming() {
		super.setConsuming();
		tower.setConsuming();
	}
	
	@Override
	public void initModel() {
		setUserData(new Reservoir());
		((Device)getUserData()).setAssociatedView(this);
		
		
	}
	@Override
	public void initModel(String name) {
		setUserData(new Reservoir(name));
		((Device)getUserData()).setAssociatedView(this);
		
	}
	
	@Override
	public void initForm() {
		super.initForm();
		tower= new WaterTower((Reservoir)getUserData());
		getChildren().add(tower);
		
	}
	
	@Override
	public void updateForm() {
		double radius = ((WaterDevice)getUserData()).getDiameter()/10;
		tower.updateForm();
		double height = Math.max(tower.size, radius);
		form.setHeight(height);
		setTranslateX(height/2);
		con.setTranslateX(height/2);

			
		initTooltip();
	}
	
	@Override
	public void updateColor(){
		WaterStream ws = ((Model.Reservoir)getUserData()).getOutStream();
		if(ws.getIncomingPressure()>0){
			if(ws.getOutgoingFlow()>0){
				setConsuming();
			}
			else{
				setFull();
			}
			
		}
		else{
			setEmpty();
		}
	}
	
	
	@Override
	protected void updateSize() {
		double oldSize = size;
		size = Math.max(tower.size, con.size);
		if(oldSize!=size){
			if (!getParent().equals(NetworkDisplay.world)){
				((WaterComponent)getParent()).updateSize();
			} 
		}
	}
	
	@Override
	protected void initTooltip(){
		super.initTooltip();
		Tooltip t = new Tooltip(getUserData().toString());
		Tooltip.install(form, t);
		
	}
}
