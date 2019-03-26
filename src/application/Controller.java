package application;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class Controller {
	
	private StageController stageController;

	public void setParent(StageController controller) {
		stageController = controller;
	}
	
	@FXML
	private Button btnLogin;
	
	@FXML
	private void loginRequest() throws IOException {
		stageController.logIn();
	}
	
}