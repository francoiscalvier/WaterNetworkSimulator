package View;

import Controler.NetworkDisplay;
import Model.Device;
import Model.WaterDevice;
import javafx.scene.transform.Rotate;

public class Connector extends WaterComponent{
	WaterComponentSet children;
	
	
	public Connector(WaterDevice wd){
		super(wd);
		
		
	}
	
	
	@Override
	public void add(WaterComponent c) {
		children.add(c);
	}
	@Override
	public void remove(WaterComponent c) {
		children.remove(c);
	}
	
	@Override
	public void initModel(Device d) {
		super.initModel(d);
		children = new WaterComponentSet();
		children.setUserData(getUserData());
		getChildren().add(children);
		

		
	}
	@Override
	public void initForm() {
		super.initForm();
		form.setRotationAxis(Rotate.Y_AXIS);
		form.setRotate(90.0);
		form.setVisible(false);
	}
	
	
	@Override
	public void updateForm() {
		
		if (!getParent().equals(NetworkDisplay.world)){
			((WaterComponent)getParent()).updateForm();
			if(form.isVisible()){
				form.setHeight(children.insideSize);
				updateColor();
				children.setTranslateX(form.getRadius());
				children.setTranslateY(-children.insideSize/2);
			}
			else{
				children.setTranslateX(0);
				children.setTranslateY(0);
			}
		} 
		initTooltip();
	}
	
	@Override
	protected void updateSize() {
	
		size = children.size;
		//this Connector is visible if and only if it has more than one child
		form.setVisible(children.getChildren().size()>1);
		if (!getParent().equals(NetworkDisplay.world)){
			((WaterComponent)getParent()).updateSize();
		} 
		

	}

}
