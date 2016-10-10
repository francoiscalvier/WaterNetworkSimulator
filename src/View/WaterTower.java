package View;


import Model.Reservoir;
import Model.WaterDevice;
import javafx.scene.control.Tooltip;
import javafx.scene.transform.Rotate;
/**
 * upper part of a water tower. The vertical pipe and the tank
 * @author fcalvier
 *
 */
public class WaterTower extends WaterComponent{
	private WaterCylinder emptyTank;
	private WaterCylinder fullTank;
	
	
	public WaterTower(WaterDevice wd) {
		super(wd);
		
	}
	
	@Override
	public void setFull() {
		super.setFull();
		fullTank.setFull();
		
	}
	
	@Override
	public void setConsuming() {
		super.setConsuming();
		fullTank.setConsuming();
		
	}
	
	
	
	@Override
	public void initForm() {
		//pipe creation
		super.initForm();
		//tower is placed the top side upon
		form.setRotationAxis(Rotate.X_AXIS);
		form.setRotate(90.0);
		
		//tank creation
		emptyTank= new WaterCylinder (100,200);
		emptyTank.setUserData(getUserData());
		emptyTank.setEmpty();
		getChildren().add(emptyTank);
		fullTank= new WaterCylinder (100,0);
		fullTank.setUserData(getUserData());
		getChildren().add(fullTank);
		//tank moved on the edge of the pipe
		fullTank.setRotationAxis(Rotate.X_AXIS);
		fullTank.setRotate(90.0);
		fullTank.setTranslateZ(-fullTank.getHeight()/2-form.getHeight()/2);
		emptyTank.setRotationAxis(Rotate.X_AXIS);
		emptyTank.setRotate(90.0);
		emptyTank.setTranslateZ(-emptyTank.getHeight()/2-fullTank.getHeight()-form.getHeight()/2);
		
		//tower bottom is moved on the floor 
		setTranslateZ(-form.getHeight()/2);
		
		
	}
	
	@Override
	public void updateForm() {
		super.updateForm();
		
		double radius = ((Reservoir)getUserData()).getCapacity()/1000;
		emptyTank.setRadius(radius);
		fullTank.setRadius(radius);
		double height = ((Reservoir)getUserData()).getEffectiveVolume()/500;
		emptyTank.setHeight(radius*2-height);
		fullTank.setHeight(height);
		//tank moved on the edge of the pipe
		fullTank.setTranslateZ(-fullTank.getHeight()/2-form.getHeight()/2);
		emptyTank.setTranslateZ(-emptyTank.getHeight()/2-fullTank.getHeight()-form.getHeight()/2);
		//tower bottom is moved on the floor
		setTranslateZ(-form.getHeight()/2);
		updateSize();
		
		initTooltip();
	}
	
	@Override
	protected void updateSize() {
		size = 2 * emptyTank.getRadius();
	}
	
	@Override
	protected void initTooltip(){
		super.initTooltip();
		Tooltip t = new Tooltip(getUserData().toString());
		Tooltip.install(emptyTank, t);
		Tooltip.install(fullTank, t);
	}
}
