package View;

import javafx.scene.shape.Box;

public class Generator extends Component{
	protected Box form;
	@Override
	protected void initForm() {
		form = new Box(10, 10, 10);
		form.setUserData(this);
		getChildren().add(form);
	}
}
