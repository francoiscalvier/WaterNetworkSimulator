package View;

public class ElectricComponent extends Component{
	
	private StateBox stateLED;
	
	public ElectricComponent(){
		stateLED= new StateBox(20, 20, 20);
		stateLED.setUserData(getUserData());
		getChildren().add(stateLED);
	}
	

	
	
}
