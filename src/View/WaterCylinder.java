package View;

import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Cylinder;

public class WaterCylinder extends Cylinder{
	protected final static PhongMaterial fullMaterial = new PhongMaterial();
	protected final static PhongMaterial emptyMaterial = new PhongMaterial();
	protected final static PhongMaterial consumingMaterial = new PhongMaterial();
	
	public WaterCylinder() {
		super();
	}
	public WaterCylinder(double radius, double height, int divisions) {
		super(radius, height, divisions);
	}
	public WaterCylinder(double radius, double height) {
		super(radius, height);
	}
	
	public void setEmpty() {
		
		setMaterial(emptyMaterial);
	}
	public void setFull() {
		
		setMaterial(fullMaterial);
		
	}
public void setConsuming() {
		
		setMaterial(consumingMaterial);
		
	}
	
	public static void initColor(){
		fullMaterial.setDiffuseColor(Color.AQUA);
		fullMaterial.setSpecularColor(Color.BLUE);
		consumingMaterial.setDiffuseColor(Color.DARKBLUE);
		consumingMaterial.setSpecularColor(Color.BLUE);
		emptyMaterial.setDiffuseColor(Color.BURLYWOOD);
		emptyMaterial.setSpecularColor(Color.BEIGE);    
 }

}
