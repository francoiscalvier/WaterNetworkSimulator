package View;

import javafx.scene.Node;

public class WaterComponentSet extends WaterComponent{
	double insideSize;
	
	@Override
	protected void initModel() {
		
	}
	
	@Override
	protected void initModel(String name) {
		
	}
	@Override
	public void initForm() {
		
	}
	
	@Override
	protected void initTooltip(){

	}
	
	@Override
	public void updateForm() {
		((WaterComponent)getParent()).updateForm();
	}
	@Override
	public void updateColor() {
		((WaterComponent)getParent()).updateColor();
	}
	
	@Override
	protected void updateSize() {
		
			size = 0;
			insideSize = 0;
			double pos =0;
			if(!getChildren().isEmpty()){
				pos =-((WaterComponent)getChildren().get(0)).size/2;
			double step =0;
			double childSize =pos;
			for(Node n : getChildren()){
				childSize =((WaterComponent)n).size;
				size+=childSize+100;
				
				step+= childSize/2;
				pos +=  step;
				((WaterComponent)n).setTranslateY(pos);
				step = childSize/2 +100;
			}
			//border children are moved in order to be inside the local form
			if(getChildren().size()>1){
				WaterComponent temp =(WaterComponent)getChildren().get(0);
				temp.setTranslateY(temp.form.getRadius());
				temp =(WaterComponent)getChildren().get(getChildren().size()-1);
				temp.setTranslateY(temp.getTranslateY() - temp.form.getRadius());
			}
			size-=100;
			insideSize = pos;
			}
				
			
			
				((WaterComponent)getParent()).updateSize();
			
		}
	
	@Override
	public Object getUserData() {
		return getParent().getUserData();
	}
	
	@Override
	public void add(WaterComponent c) {
		getChildren().add(c);
		updateSize();
		updateForm();
	}
	
	@Override
	public void remove(WaterComponent c) {
		getChildren().remove(c);
		updateSize();
		updateForm();
	}
}
