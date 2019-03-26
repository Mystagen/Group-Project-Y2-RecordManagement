package application;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class PersonalTutorController implements Initializable {
	
	@FXML private Label tutorName;
	@FXML private Label tutorID;
	@FXML private Label staffID;
	
	@FXML private VBox studentContainer;
	
	@FXML private ChoiceBox<String> tutorSelection;
	
	@FXML private Button addStudentButton;
	@FXML private Button removeStudentButton;
	
	PopupInputs dialog = new PopupInputs();
	
	SQLTable staffConnection;
	SQLTable studentConnection;
	SQLTable tutorialStudentsConnection;
	SQLTable tutorsConnection;
	SQLTable tutorialsConnection;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		this.staffConnection = new SQLTable("staff");
		this.studentConnection = new SQLTable("student_records");
		this.tutorialStudentsConnection = new SQLTable("tutorial_students");
		this.tutorsConnection = new SQLTable("personal_tutors");
		this.tutorialsConnection = new SQLTable("personal_tutorials");
		populateTutorChoiceBox();
		
		addStudentButton.setDisable(true);
		removeStudentButton.setDisable(true);
	}
	
	private void drawStudents(int tutorID) {
		
		studentContainer.getChildren().clear();
		
		ResultSet tutorStudents = tutorialStudentsConnection.findAllWhere("tutor_id", tutorID);
		
		ArrayList<String> studentIDList = new ArrayList<String>();
		
		try {
			while (tutorStudents.next()) {
				studentIDList.add(tutorStudents.getString(2));
			}
			
			for (int i = 0; i < studentIDList.size(); i++) {
				ResultSet studentInfo = studentConnection.findAllWhere("student_id", studentIDList.get(i));
				
				while (studentInfo.next()) {
					HBox studentRow = new HBox();
					studentRow.setSpacing(10.0);
					studentRow.setPadding(new Insets(10.0));
					studentRow.getChildren().addAll(new Label(studentInfo.getString(4) + " " + studentInfo.getString(5) + " " + studentInfo.getString(6)),
													new Label("ID: " + studentInfo.getString(1)),
													new Label(studentInfo.getString(9)));
					studentContainer.getChildren().add(studentRow);
				}
			}
			
		} catch (Exception e) {
			System.out.println("Error drawing students: " + e);
			e.printStackTrace();
		}
	}
	
	private void populateTutorChoiceBox() {
		ResultSet tutors = tutorsConnection.findAll();
		
		ArrayList<String> tutorIDList = new ArrayList<String>();
		ArrayList<String> finalList = new ArrayList<String>();
		finalList.add("");
		
		try {
			while (tutors.next()) {
				tutorIDList.add(tutors.getString(2));
			}
			
			for (int i = 0; i < tutorIDList.size(); i++) {
				ResultSet staffName = staffConnection.findAllWhere("staff_id", Integer.parseInt(tutorIDList.get(i)));
				
				while (staffName.next()) {
					finalList.add(staffName.getString(2) + " " + staffName.getString(4) + " (ID: " + staffName.getInt(1) + ")");
				}
			}
		} catch (Exception e) {
			System.out.println("Error populating tutors: " + e);
			e.printStackTrace();
		}
		
		tutorSelection.setItems(FXCollections.observableArrayList(finalList));
		tutorSelection.setValue("");
	}
	
	@FXML
	public void addTutor() {
		int selectedID = dialog.staffSelection(staffConnection);
		
		if (selectedID != -1) {
			tutorsConnection.insertTutor(selectedID);
			populateTutorChoiceBox();
		}
	}
	
	@FXML
	public void deleteTutor() {
	
	}
	
	@FXML
	public void addStudent() {
		ArrayList<String> ids = dialog.multipleStudentSelect(studentConnection, tutorialStudentsConnection, Integer.parseInt(tutorID.getText().split(":")[1].substring(1, tutorID.getText().split(":")[1].length())));
		ArrayList<String> names = new ArrayList<String>();
		for (int i = 0; i < ids.size(); i++) {
			ResultSet studentLookUp = studentConnection.findAllWhere("student_id", ids.get(i));
			
			tutorialStudentsConnection.insert(Integer.parseInt(tutorID.getText().split(":")[1].substring(1, tutorID.getText().split(":")[1].length())), ids.get(i));
		}
		drawStudents(Integer.parseInt(tutorID.getText().split(":")[1].substring(1, tutorID.getText().split(":")[1].length())));
	}
	
	@FXML
	public void removeStudent() {
		dialog.multipleStudentDelete(studentConnection, tutorialStudentsConnection, Integer.parseInt(tutorID.getText().split(":")[1].substring(1, tutorID.getText().split(":")[1].length())));
		drawStudents(Integer.parseInt(tutorID.getText().split(":")[1].substring(1, tutorID.getText().split(":")[1].length())));
	}
	
	@FXML 
	public void viewSelectedTutor() {
		if (!tutorSelection.getValue().equals("")) {
			addStudentButton.setDisable(false);
			removeStudentButton.setDisable(false);
			
			int id = Integer.parseInt(tutorSelection.getValue().split(": ")[1].substring(0, tutorSelection.getValue().split(": ")[1].length()-1));
			ResultSet staffInformation = staffConnection.findAllWhere("staff_id", id);
			
			int tutorIDStored = -1;
			try {
				while (staffInformation.next()) {
					tutorName.setText(staffInformation.getString(2) + " " + staffInformation.getString(4));
					staffID.setText("Staff ID: " + id);
					tutorIDStored = staffInformation.getInt(1);
				}
				
				ResultSet tutors = tutorsConnection.findAllWhere("staff_id", id);
				
				while (tutors.next()) {
					tutorID.setText("Tutor ID: " + tutors.getString(2));
				}
				
				drawStudents(tutorIDStored);
			} catch (Exception e) {
				System.out.println("View error: " + e);
				e.printStackTrace();
			}
		}
	}


}
