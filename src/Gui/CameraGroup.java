package Gui;

import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.transform.Rotate;

/**
 * Specific Group holding the camera in order to rotate around the CameraGroup fix point 
 * @author fecalvier
 */
 
public class CameraGroup extends Group {
 
    
        
   /*
    * rotation axes
    */
    private Rotate rx = new Rotate();
    { rx.setAxis(Rotate.X_AXIS); }
    private Rotate ry = new Rotate();
    { ry.setAxis(Rotate.Y_AXIS); }
    private Rotate rz = new Rotate();
    { rz.setAxis(Rotate.Z_AXIS); }
   
    /*
     *  the hold Camera
     */
    private PerspectiveCamera camera = new PerspectiveCamera(true);
    

    public CameraGroup() {
        super();
        getTransforms().addAll(rz, ry, rx);
        getChildren().add(camera);
        camera.setFarClip(10000);
        
    }
    
    
    public PerspectiveCamera getCamera(){
    	return camera;
    }
   

    public void setTranslate(double x, double y, double z) {
       camera.setTranslateX(x);
       camera.setTranslateY(y);
       camera.setTranslateZ(z);

    }

    public void setTranslate(double x, double y) {
    	camera.setTranslateX(x);
        camera.setTranslateY(y);
    }

   
    public void setTx(double x) { camera.setTranslateX(x); }
    public void setTy(double y) { camera.setTranslateY(y); }
    public void setTz(double z) { camera.setTranslateZ(z); }

    public void setRotate(double x, double y, double z) {
        rx.setAngle(x);
        ry.setAngle(y);
        rz.setAngle(z);
    }

    public void setRotateX(double x) { rx.setAngle(x); }
    public void setRotateY(double y) { ry.setAngle(y); }
    public void setRotateZ(double z) { rz.setAngle(z); }
    public void setRy(double y) { ry.setAngle(y); }
    public void setRz(double z) { rz.setAngle(z); }

    public void setScale(double scaleFactor) {
    	camera.setScaleX(scaleFactor);
    	camera.setScaleY(scaleFactor);
    	camera.setScaleZ(scaleFactor);

    	
    }


    public void setSx(double x) {camera.setScaleX(x); }
    public void setSy(double y) { camera.setScaleY(y); }
    public void setSz(double z) { camera.setScaleZ(z); }


    public void reset() {
    	setTranslate(0.0, 0.0, 0.0);
    	setRotate(0.0, 0.0, 0.0);
        setScale(1.0);

		
    }
    
    public void translateX(double x) {
		camera.setTranslateX(camera.getTranslateX()+x);
		
	}
    
	public void translateY(double y) {
		camera.setTranslateY(camera.getTranslateY()+y);
		
	}

	public static double trz = 0;
	public void translateZ(double z) {
		camera.setTranslateZ(camera.getTranslateZ()+z);

        
		trz+=Math.abs(z);
		if(trz>10){
			save();
		}
		
	}

	
	public static double angle = 0;
	
	public void rotateX(double x) {
		setRotateX(rx.getAngle()+x);

		
		angle+=Math.abs(x);
		if(angle>=10){
			save();
		}
		
		
	}
	public void rotateY(double y) {
		setRotateY(ry.getAngle()+y);
		
	}
	
	public void rotateZ(double z) {
		setRotateZ(rz.getAngle()+z);
		angle+=Math.abs(z);
		if(angle>=10){
		save();
		
	}

	}
	
	private void save(){
			angle =0;
			trz =0;
	}
   
}