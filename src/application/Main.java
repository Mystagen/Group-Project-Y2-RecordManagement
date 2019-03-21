package application;
	
import java.io.FileInputStream;
import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;


public class Main extends Application {
	
	public void start(Stage stage) {
		
		try {
	        FXMLLoader loader = new FXMLLoader();
	        String fxmlDocPath = "./src/stage.fxml";
	        FileInputStream fxmlStream = new FileInputStream(fxmlDocPath);
	        
	        AnchorPane root = (AnchorPane) loader.load(fxmlStream);
	
	        Scene scene = new Scene(root);
	        stage.setScene(scene);
	        stage.setWidth(700);
	        stage.setResizable(false);
	        stage.setTitle("Woodlands Record Management System");
	        stage.show();
	        
	        StageController controller = loader.getController(); 
		} catch (Exception e) {
	    	e.printStackTrace();
	    }
	}

	public static void main(String[] args) {
		launch(args);
	}
}
