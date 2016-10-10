package Gui;

import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.PickResult;
import javafx.scene.input.ScrollEvent;

/**
 * class responsible for all interactions with the GUI environment (mouse, keyboard)
 * @author fcalvier
 *
 */
public class HandlingInputScene extends Scene{
    private static final double CONTROL_MULTIPLIER = 0.1;
    private static final double SHIFT_MULTIPLIER = 10.0;
    private static final double MOUSE_SPEED = 0.1;
    private static final double ROTATION_SPEED = 2.0;
    private static final double TRACK_SPEED = 0.3;
    private double mousePosX;
    private double mousePosY;
    private double mouseDeltaX;
    private double mouseDeltaY;
    private boolean mouseRightClick;
    
    private CameraGroup cameraGroup;
    
    
    
	public HandlingInputScene(Parent parent) {
		
		super (parent, 1024, 768, true,SceneAntialiasing.BALANCED);
		
	
        setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent me) {
                mousePosX = me.getSceneX();
                mousePosY = me.getSceneY();
                mouseRightClick =me.isSecondaryButtonDown();
                me.consume();
            }
        });
        
        setOnMouseReleased(new EventHandler<MouseEvent>() {
        	@Override public void handle(MouseEvent me) {
        		if(mouseRightClick){
        		PickResult res = me.getPickResult();
        		
        		if(!me.isConsumed() && me.isDragDetect()&& res.getIntersectedNode() == null){
        			

        			


        			me.consume();
        		}
        		}
        	}


        });
        
        
       
        setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent me) {
            	
                mouseDeltaX = (me.getSceneX() - mousePosX); 
                mouseDeltaY = (me.getSceneY() - mousePosY); 
                mousePosX = me.getSceneX();
                mousePosY = me.getSceneY();
                double modifier = 1.0;
                
                if (me.isControlDown()) {
                    modifier = CONTROL_MULTIPLIER;
                } 
                if (me.isShiftDown()) {
                    modifier = SHIFT_MULTIPLIER;
                }     
                if (me.isPrimaryButtonDown()) {
                	
                    cameraGroup.rotateZ( - mouseDeltaX*MOUSE_SPEED*modifier*ROTATION_SPEED);  
                    cameraGroup.rotateX( mouseDeltaY*MOUSE_SPEED*modifier*ROTATION_SPEED);  
                   
                  
                   
                }
                else if (me.isSecondaryButtonDown()) {
                	cameraGroup.translateX( -mouseDeltaX*MOUSE_SPEED*modifier*TRACK_SPEED);  
                	cameraGroup.translateY(  mouseDeltaY*MOUSE_SPEED*modifier*TRACK_SPEED); 
                }
                else if (me.isMiddleButtonDown()) {
                	cameraGroup.translateZ( mouseDeltaX*MOUSE_SPEED*modifier);
                	 
                }
                me.consume();
            }
        });
        
        setOnScroll(new EventHandler<ScrollEvent>() {
        	
            @Override public void handle(ScrollEvent event) {
            	cameraGroup.translateZ(event.getDeltaY()*MOUSE_SPEED);
            
            	event.consume();
            }
            
        });
        setOnKeyPressed(new EventHandler<KeyEvent>() {
        	
            @Override public void handle(KeyEvent event) {
            	 switch (event.getCode()) {
            	 case RIGHT:
            		 cameraGroup.rotateZ( - 1);  
                     
                     break;
                 case LEFT:
                	 cameraGroup.rotateZ( 1);  ;
                      break;
                  case UP:
                	  cameraGroup.rotateX(-1);  
                     break;
                  case DOWN :
                	  cameraGroup.rotateX(1);  
                	  break;
				default:
					break;
                	  
            	 }
            	
            	 event.consume();
              
            }

        });
    }


	
	public void setCamera(CameraGroup cameraGroup) {
		super.setCamera (cameraGroup.getCamera());
		this.cameraGroup = cameraGroup;
	}
   
}
