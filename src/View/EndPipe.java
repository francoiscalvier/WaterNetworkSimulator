package View;

import Model.Device;
import Model.PrivateSector;
import Model.WaterDevice;
import javafx.scene.transform.Rotate;

public class EndPipe extends WaterComponent {
	
	
	
	@Override
	public void add(WaterComponent c) {
		
		
	}
	@Override
	public void remove(WaterComponent c) {
		
		
		
	}
	@Override
	public void initModel() {
		setUserData(new PrivateSector());
		((Device)getUserData()).setAssociatedView(this);
		size = ((WaterDevice)getUserData()).getDiameter()/10;
	}
	@Override
	public void initForm() {
		
		form =new WaterCylinder (5,50);
		form.setUserData(this);
		getChildren().add(form);
		form.setRotationAxis(Rotate.Z_AXIS);
		form.setRotate(90.0);
		form.setRadius(size);
		setTranslateX(+25);
		setEmpty();
	}
	
	
	@Override
	public void updateForm() {
		initTooltip();
	}
	
	@Override
	protected void updateSize() {
		
	}
}
