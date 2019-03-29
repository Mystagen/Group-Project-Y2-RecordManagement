package application;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

public class WindowController {

	@FXML
	public AnchorPane displayPane;
	
	private void updateScreen(String fxmlDocPath) throws IOException {
		displayPane.getChildren().clear();
		FXMLLoader loader = new FXMLLoader();
	    FileInputStream fxmlStream = new FileInputStream(fxmlDocPath);
	    
	    AnchorPane studentContainer = (AnchorPane) loader.load(fxmlStream);
	    
	    displayPane.getChildren().add(studentContainer);
		AnchorPane.setLeftAnchor(studentContainer, 0.0);
		AnchorPane.setRightAnchor(studentContainer, 0.0);
		AnchorPane.setTopAnchor(studentContainer, 0.0);
		AnchorPane.setBottomAnchor(studentContainer, 0.0);
	}
	
	@FXML
	private void studentView() {
	    try {
			updateScreen("./src/student.fxml");
		} catch (IOException e) {
			System.out.println("Error file not found: " + e);
		}

	}
	
	@FXML
	private void staffView() {
		try {
			updateScreen("./src/staff.fxml");
		} catch (IOException e) {
			System.out.println("Error file not found: " + e);
		}
	}
	
	@FXML
	private void timetableView() {
		try {
			updateScreen("./src/timetable.fxml");
		} catch (IOException e) {
			System.out.println("Error file not found: " + e);
			e.printStackTrace();
		}
	}
	
	@FXML
	private void attendanceView() {
		try { 
			updateScreen("./src/attendance.fxml");
		} catch (IOException e) {
			System.out.println("Error file not found: " + e);
			e.printStackTrace();
		}
	}
	
	@FXML
	private void modulesView() {
		try {
			updateScreen("./src/modules.fxml");
		} catch (IOException e) {
			System.out.println("Error file not found: " + e);
		}
	}
	
	@FXML
	private void gradesView() {
		try {
			updateScreen("./src/grades.fxml");
		} catch (IOException e) {
			System.out.println("Error file not found: " + e);
		}
	}
	
	@FXML
	private void personalTutorView() {
		try {
			updateScreen("./src/personalTutor.fxml");
		} catch (IOException e) {
			System.out.println("Error file not found: " + e);
		}
	}
	
	@FXML
	private void assignmentView() {
		try {
			updateScreen("./src/assignments.fxml");
		} catch (IOException e) {
			System.out.println("Error file not found: " + e);
			e.printStackTrace();
		}
	}

}
