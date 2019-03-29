package application;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.util.Pair;

public class AssignmentsController implements Initializable {

	@FXML private ChoiceBox<String> moduleChoiceBox;
	@FXML private ChoiceBox<String> assignmentChoiceBox;
	
	@FXML private Label idLabel;
	
	@FXML private TextField titleTextField;
	@FXML private TextField weightingTextField;
	
	@FXML private TextArea descriptionTextArea;
	
	@FXML private Button selectAssignment;
	@FXML private Button saveChangesButton;
	
	@FXML private Label weightingLabel;
	@FXML private Label titleLabel;
	@FXML private Label descriptionLabel;
	@FXML private Label idNameLabel;
	@FXML private Label assignmentLabel;
	
	SQLTable moduleConnection;
	SQLTable assignmentConnection;
	SQLTable assignmentGradeConnection;
	SQLTable studentRecordConnection;
	SQLTable courseModulesConnection;
	
	String moduleCode;
	String assignmentCode;
	
	PopupInputs dialog = new PopupInputs();
	
	private ArrayList<String> studentIDs = new ArrayList<String>();
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		moduleConnection = new SQLTable("modules");
		assignmentConnection = new SQLTable("assignments");
		assignmentGradeConnection = new SQLTable("assignment_grade");
		studentRecordConnection = new SQLTable("student_records");
		courseModulesConnection = new SQLTable("course_modules");
		
		idLabel.setText("");
		
		weightingLabel.setDisable(true);
		titleLabel.setDisable(true);
		idNameLabel.setDisable(true);
		descriptionLabel.setDisable(true);
		descriptionTextArea.setDisable(true);
		weightingTextField.setDisable(true);
		titleTextField.setDisable(true);
		idLabel.setDisable(true);
		assignmentChoiceBox.setDisable(true);
		saveChangesButton.setDisable(true);
		selectAssignment.setDisable(true);
		assignmentLabel.setDisable(true);
		
		populateModules();
	}
	
	private void populateAssignments() {
		if (!moduleCode.equals("")) {
			ResultSet assignments = assignmentConnection.findAll();
			
			ArrayList<String> assignmentList = new ArrayList<String>();
			assignmentList.add("");
			
			try {
				while (assignments.next()) {
					if (assignments.getString(5).equals(moduleCode)) {
						assignmentList.add(assignments.getString(2) + " - " + assignments.getString(1));
					}
				}
			} catch (Exception e) {
				System.out.println("Error populating assignmetns: " + e);
				e.printStackTrace();
			}
			assignmentChoiceBox.setItems(FXCollections.observableArrayList(assignmentList));
			assignmentChoiceBox.setValue("");
		}
	}
	
	private void populateModules() {
		ResultSet modules = moduleConnection.findAll();
		
		ArrayList<String> moduleList = new ArrayList<String>();
		moduleList.add("");
		
		try {
			while (modules.next()) {
				moduleList.add(modules.getString(2) + " - " + modules.getString(1));
			}
		} catch (Exception e) {
			System.out.println("Error populating modules: " + e);
			e.printStackTrace();
		}
		moduleChoiceBox.setItems(FXCollections.observableArrayList(moduleList));
		moduleChoiceBox.setValue("");
	}
	
	private void addInformation() {
		ResultSet assignmentInformation = assignmentConnection.findAllWhere("assignment_id", assignmentCode);
		
		try {
			while (assignmentInformation.next()) {
				idLabel.setText(assignmentInformation.getString(1));
				titleTextField.setText(assignmentInformation.getString(2));
				descriptionTextArea.setText(assignmentInformation.getString(3));
				weightingTextField.setText(assignmentInformation.getString(4));
			}
		} catch (Exception e) {
			System.out.println("Error adding information: " + e);
			e.printStackTrace();
		}
	}
	
	@FXML
	public void selectModule() {
		if (!moduleChoiceBox.getValue().equals("")) {
			selectAssignment.setDisable(false);
			assignmentChoiceBox.setDisable(false);
			assignmentLabel.setDisable(false);
			moduleCode = moduleChoiceBox.getValue().split(" - ")[1];
			populateAssignments();
		}
	}
	
	@FXML
	public void selectAssignment() {
		if (!assignmentChoiceBox.getValue().equals("")) {
			weightingLabel.setDisable(false);
			titleLabel.setDisable(false);
			idNameLabel.setDisable(false);
			descriptionLabel.setDisable(false);
			descriptionTextArea.setDisable(false);
			weightingTextField.setDisable(false);
			titleTextField.setDisable(false);
			idLabel.setDisable(false);
			saveChangesButton.setDisable(false);
			assignmentCode = assignmentChoiceBox.getValue().split(" - ")[1];
			addInformation();
		}
	}
	
	@FXML
	public void addAssignment() {
		String[] fields = {"Title", "Description", "Weighting"};
		ArrayList<Pair<String, Pair<String, SQLTable>>> sqlChoiceBox = new ArrayList<Pair<String, Pair<String, SQLTable>>>();
		Pair<String, SQLTable> pair1 = new Pair<String, SQLTable>("Module", moduleConnection);
		Pair<String, Pair<String, SQLTable>> pair2 = new Pair<String, Pair<String, SQLTable>>("", pair1);
		sqlChoiceBox.add(pair2);
		ArrayList<String> newAssignment = dialog.inputDialog("Add an assessment", "Add an assessment", "Add", fields, sqlChoiceBox, null);
		String moduleCodeInsert = newAssignment.get(3).split(" - ")[0];
		assignmentConnection.assignmentInsert(newAssignment.get(0), newAssignment.get(1), Integer.parseInt(newAssignment.get(2)), moduleCodeInsert);
		
		ArrayList<String> codes = new ArrayList<String>();
		
		ResultSet courseCodes = courseModulesConnection.findAllWhere("module_code", moduleCodeInsert);
		
		try {
			String assignmentIDInserted = "";
			
			ResultSet asID = assignmentConnection.findAllWhere("title", newAssignment.get(0));
			
			while (asID.next()) {
				if (asID.getString(3).equals(newAssignment.get(1)) && asID.getString(5).equals(moduleCodeInsert)) {
					assignmentIDInserted = asID.getString(1);
				}
			}
			
			while (courseCodes.next()) {
				codes.add(courseCodes.getString(1));
			}
			
			for (int i = 0; i < codes.size(); i++) {
				ResultSet studentIDSet = studentRecordConnection.findAllWhere("course_code", codes.get(i));
				
				while (studentIDSet.next()) {
					studentIDs.add(studentIDSet.getString(1));
				}
			}
			
			for (int i = 0; i < studentIDs.size(); i++) {
				if (!assignmentIDInserted.equals("")) {
					assignmentGradeConnection.insertAG(assignmentIDInserted, Integer.parseInt(studentIDs.get(i)));
				}
			}
		} catch (Exception e) {
			System.out.println("Error: " + e);
			e.printStackTrace();
		}
	}
	
	@FXML
	public void deleteAssignment() {
		System.out.println("Delete");
	}
	
	@FXML
	public void saveChanges() {
		if (!idLabel.getText().equals("")) {
			assignmentConnection.updateAssignment(Integer.parseInt(idLabel.getText()), titleTextField.getText(), descriptionTextArea.getText(), Integer.parseInt(weightingTextField.getText()));
		}
	}
}
