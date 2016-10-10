package View;


import Model.Device;
import Model.WaterDevice;
import Model.WaterResource;
import Model.WaterStream;
import javafx.scene.transform.Rotate;

/**
 * Peculiar WaterComponent. The form is not a pipe and its Parent is not a WaterComponent
 * @author fcalvier
 *
 */
public class Lake extends WaterComponent{
	
		public Lake(String name) {
		super(name);
	}

	@Override
	public void add(WaterComponent c) {
		super.add(c);
	}

	@Override
	public void initModel() {
		setUserData(new WaterResource());
		((Device)getUserData()).setAssociatedView(this);
		
	}
	@Override
	public void initModel(String name) {
		setUserData(new WaterResource(name));
		((Device)getUserData()).setAssociatedView(this);
		

	}
	
	@Override
	public void initForm() {
		form = new WaterCylinder (200,100);
		form.setUserData(getUserData());
		getChildren().add(form);
		form.setTranslateX(-100);
		form.setRotationAxis(Rotate.X_AXIS);
		form.setRotate(90.0);
		setFull();
	}
	
	@Override
	public void updateForm() {
		
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
		initTooltip();
	}
	
}
