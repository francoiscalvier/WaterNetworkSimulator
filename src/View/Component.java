package View;


import java.util.List;
import java.util.Optional;

import Model.Device;
import Model.WaterDevice;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.input.MouseEvent;

public class Component extends Group {
	protected boolean mouseRightClick;
	
	public Component(){
		init();
		initModel();
		initForm();
		initTooltip();
	}
	
	public Component(Device d) {
		init();
		initModel(d);
		initForm();
		initTooltip();
	}

	

	public Component(String name) {
		init();
		initModel(name);
		initForm();
		initTooltip();
	}



	protected void initForm(){
		
	}

	private void init() {
		setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent me) {
            	 mouseRightClick =me.isSecondaryButtonDown();
            	 me.consume();
            	 
            }
		});
		setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent me) {
            	if(mouseRightClick){
            	
            	}
            	else{
            		List<String> choices = ((WaterDevice)getUserData()).getPossibleActions();
            		Optional<String> result = 
            		new ChoiceDialog<>(choices.get(0),choices).showAndWait();
            		if(result.isPresent()){
            			((WaterDevice)getUserData()).doSelectedAction(result.get());
            		}
            		
            	}
            	me.consume();
            }
		});
	}
	
	protected void initModel() {
		
	}

	protected void initModel(String name) {
		
	}
	
	protected void initModel(Device d) {
		setUserData(d);
	}
	
	protected void initTooltip(){

	}

	public void updateForm() {
		
	}
}


	
