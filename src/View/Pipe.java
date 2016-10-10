package View;


import Controler.NetworkDisplay;
import Model.Device;
import Model.PublicSector;
import Model.WaterDevice;
import javafx.scene.transform.Rotate;


/**
 * View for a PublicSector
 * @author fcalvier
 *
 */
public class Pipe extends WaterComponent{
	
	protected Connector con;
	
	public Pipe() {
		
	}
	
	public Pipe(WaterDevice wd) {
		super(wd);
		wd.setAssociatedView(this);
	}
	

	public Pipe(String name) {
		super(name);
	}

	@Override
	public void add(WaterComponent c) {
		con.add(c);
		((WaterDevice)c.getUserData()).addToNetwork((WaterDevice)getUserData());
		
	}

	@Override
	public void addExisting(WaterComponent c) {
		con.add(c);
		
		
	}
	
	@Override
	public void remove(WaterComponent c) {
		con.remove(c);
		
	}
	@Override
	public void initModel() {
		setUserData(new PublicSector());
		((Device)getUserData()).setAssociatedView(this);

	}
	
	@Override
	public void initModel(String name) {
		setUserData(new PublicSector(name));
		((PublicSector)getUserData()).setAssociatedView(this);
	}
	
	@Override
	public void initForm() {
		super.initForm();
		form.setRotationAxis(Rotate.Z_AXIS);
		form.setRotate(90.0);
		setTranslateX(50);
		con = new Connector((WaterDevice)getUserData());
		getChildren().add(con);
		con.setTranslateX(50);
		con.initTooltip();
		
	}
	
	@Override
	public void setEmpty(){
		super.setEmpty();
		con.setEmpty();
	}
	
	@Override
	public void setFull(){
		super.setFull();
		con.setFull();
	}
	
	@Override
	public void setConsuming() {
		super.setConsuming();
		con.setConsuming();
	}
	
	
	@Override
	public void updateForm() {
		double radius = form.getRadius();
		super.updateForm();
		if (radius != form.getRadius()){
			radius = form.getRadius();
			setTranslateX(radius*5);
			con.setTranslateX(radius*5);
			con.form.setRadius(radius);
		}
	}
	
	@Override
	protected void updateSize() {
		size = con.size;
		
		if (!getParent().equals(NetworkDisplay.world)){
			((WaterComponent)getParent()).updateSize();
		} 
	}
	
	
	public void lateralSplit(PublicSector ps) {
		Pipe sibling = new Pipe(ps);
		if (!getParent().equals(NetworkDisplay.world)){
			((WaterComponent)getParent()).addExisting(sibling);
			
		
			for(WaterDevice wd :  ps.getOutgoingDevices()){
				WaterComponent wc = (WaterComponent)  wd.getAssociatedView();
				remove(wc);
				sibling.addExisting(wc)  ;
			}
		}
		sibling.updateForm();
		sibling.updateColor();
		
	}
	

	public void verticalSplit(PublicSector ps) {
		Pipe parent = new Pipe(ps);
		if (!getParent().equals(NetworkDisplay.world)){
			((WaterComponent)getParent()).addExisting(parent);
			
		}
		for(WaterDevice wd :  ps.getOutgoingDevices()){
			WaterComponent wc =(WaterComponent)  wd.getAssociatedView();
			remove(wc);
			parent.addExisting(wc)  ;
		}
		parent.updateForm();
		parent.updateColor();
	}
}
