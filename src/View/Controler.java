package View;

import Controler.NetworkDisplay;
import Model.Device;
import Model.WaterStream;
import javafx.scene.control.Tooltip;
import javafx.scene.shape.Box;

public class Controler extends Pipe{

	private Box controlSystem;
	
	public Controler(){
		
	}
	
	public Controler(String name) {
		super(name);
	}
	
	public Controler(Model.Controler c){
		super(c);
		c.setAssociatedView(this);
	}
	
	@Override
	public void initModel() {
		setUserData(new Model.Controler());
		((Device)getUserData()).setAssociatedView(this);
		
		
		
	}
	@Override
	public void initModel(String name) {
		setUserData(new Model.Controler(name));
		((Device)getUserData()).setAssociatedView(this);
		
		
		
	}
	
	@Override
	public void initForm() {
		super.initForm();
		controlSystem= new Box (20,20,20);
		controlSystem.setUserData(getUserData());
		getChildren().add(controlSystem);
		controlSystem.setTranslateX(-40);
		
		
	}
	
	@Override
	public void updateForm() {
		super.updateForm();
		double radius = form.getRadius();
		controlSystem.setHeight(radius*2);
		controlSystem.setWidth(radius*2);
		controlSystem.setDepth(radius*2);
		controlSystem.setTranslateX(-radius*4);
		
	}
	
	@Override
	public void updateColor(){
		WaterStream ws = ((Model.Controler)getUserData()).getOutStream();
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
		size = Math.max(controlSystem.getWidth(), con.size);
		if(oldSize!=size){
			if (!getParent().equals(NetworkDisplay.world)){
				((WaterComponent)getParent()).updateSize();
			} 
		}
	}
	
	@Override
	protected void initTooltip() {
		super.initTooltip();
		Tooltip t = new Tooltip(getUserData().toString());
		Tooltip.install(controlSystem, t);

	}
}
