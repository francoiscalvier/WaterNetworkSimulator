package View;

import Controler.NetworkDisplay;
import Model.WaterDevice;
import Model.WaterStream;
import javafx.application.Platform;
import javafx.scene.control.Tooltip;





/**
 * A generic pipe used as a base for the view of many ComponentDevices
 * @author fcalvier
 *
 */
public class WaterComponent extends Component{
	protected boolean mouseRightClick;
	protected WaterCylinder form;
	protected double size=0;

	public WaterComponent() {
		
	}
	
	public WaterComponent(WaterDevice wd) {
		super(wd);
	}
	
	public WaterComponent(String name) {
		super(name);
	}

	public void add(WaterComponent c){
		getChildren().add(c);
		((WaterDevice)c.getUserData()).addToNetwork((WaterDevice)getUserData());
		c.updateSize();
		c.updateForm();
	}
	
	public void addExisting(WaterComponent c) {
		getChildren().add(c);
		updateSize();
		updateForm();
		
	}
	
	
	@Override
	public void updateForm() {
		double radius = ((WaterDevice)getUserData()).getDiameter()/10;
		form.setRadius(radius);
		form.setHeight(radius*10);
		
	}
	
	public void updateColor(){
		Platform.runLater(() ->{
		WaterStream ws = ((WaterDevice)getUserData()).getStream();
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
		});
	}
	
	protected void updateSize(){
		
	}

	public void remove(WaterComponent c){
		getChildren().remove(c);
	}
	
	
	public void setEmpty(){
		form.setEmpty();
	}
	public void setFull(){
		form.setFull();
		
	}
	public void setConsuming(){
		form.setConsuming();
		
	}
	
	@Override
	protected void initTooltip(){
		Tooltip t = new Tooltip(getUserData().toString());
		Tooltip.install(form, t);
	}
	
	@Override
	public void initForm() {
		form =new WaterCylinder (10,100);
		form.setUserData(getUserData());
		getChildren().add(form);

	}
	
	public void verticalSplit(Model.Controler c){
		
		Controler parent = new Controler(c);
		if (!getParent().equals(NetworkDisplay.world)){
			((WaterComponent)getParent()).addExisting(parent);
			
		}
		parent.addExisting(this)  ;
		parent.updateForm();
		parent.updateColor();
	}
}
