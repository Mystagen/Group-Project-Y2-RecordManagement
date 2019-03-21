package application;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

public class StageController {
	
	@FXML
	public AnchorPane mainStage;
	
	@FXML
    protected void initialize() throws IOException {
		FXMLLoader loader = new FXMLLoader();
	    String fxmlDocPath = "./src/login.fxml";
	    FileInputStream fxmlStream = new FileInputStream(fxmlDocPath);
	    loadScreen(0, loader, fxmlStream);
    }
	
	private void loadScreen(int clearPanel, FXMLLoader loader, FileInputStream fxmlStream) {

		try {
		    AnchorPane loginContainer = (AnchorPane) loader.load(fxmlStream);
		    
			if (clearPanel == 1) {
				mainStage.getChildren().clear();
			} else if (clearPanel == 0) {
			    Controller controller = loader.<Controller>getController();
				controller.setParent(this);
			}
			
		    mainStage.getChildren().add(loginContainer);
			AnchorPane.setLeftAnchor(loginContainer, 0.0);
			AnchorPane.setRightAnchor(loginContainer, 0.0);
			AnchorPane.setTopAnchor(loginContainer, 0.0);
			AnchorPane.setBottomAnchor(loginContainer, 0.0);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void logIn() throws IOException {
		FXMLLoader loader = new FXMLLoader();
	    String fxmlDocPath = "./src/window.fxml";
	    FileInputStream fxmlStream = new FileInputStream(fxmlDocPath);
		loadScreen(1, loader, fxmlStream);
	}
	

	
}
