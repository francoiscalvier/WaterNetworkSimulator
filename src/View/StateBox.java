package View;

import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;

public class StateBox extends Box{
	 
		protected final static PhongMaterial onMaterial = new PhongMaterial();
		protected final static PhongMaterial offMaterial = new PhongMaterial();
		protected final static PhongMaterial alertMaterial = new PhongMaterial();
		
		public StateBox() {
			super();
		}
		

		
		public StateBox(double width, double height, double depth) {
			super(width, height, depth);
			
		}



		public void setOn() {
			
			setMaterial(onMaterial);
		}
		public void setOff() {
			
			setMaterial(offMaterial);
			
		}
	public void setAlert() {
			
			setMaterial(alertMaterial);
			
		}
		
		public static void initColor(){
			onMaterial.setDiffuseColor(Color.GREEN);
			onMaterial.setSpecularColor(Color.GREEN);
			offMaterial.setDiffuseColor(Color.ORANGE);
			offMaterial.setSpecularColor(Color.ORANGE);
			alertMaterial.setDiffuseColor(Color.RED);
			alertMaterial.setSpecularColor(Color.RED);    
	 }

}
