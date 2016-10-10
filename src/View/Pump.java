package View;

import Model.Device;
import Model.PumpingGroup;


public class Pump extends Controler{
	private ElectricComponent pump;
	
	@Override
	public void initModel() {
		setUserData(new PumpingGroup());
		((Device)getUserData()).setAssociatedView(this);
		
		
	}
	@Override
	public void initForm() {
		super.initForm();
		pump= new ElectricComponent();
		pump.setUserData(getUserData());
		getChildren().add(pump);
		
	}

}
