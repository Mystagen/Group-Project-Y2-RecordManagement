package application;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import application.TimetableController.Timetable;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Pair;

public class GradesController implements Initializable {
	
	@FXML private TableView<Grade> gradeTable;
	@FXML private TableColumn<Grade, String> idColumn;
	@FXML private TableColumn<Grade, String> gradeColumn;
	@FXML private TableColumn<Grade, Button> editColumn;
	@FXML private Button viewButton;
	
	@FXML private ChoiceBox<String> assignmentChoiceBox;
	@FXML private ChoiceBox<String> moduleCodeChoiceBox;
	
	@FXML private Label assignmentLabel;

	SQLTable moduleConnection;
	SQLTable assignmentConnection;
	SQLTable assignmentGradeConnection;
	
	String moduleCode = "";

	static PopupInputs dialog = new PopupInputs();
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		this.moduleConnection = new SQLTable("modules");
		this.assignmentConnection = new SQLTable("assignments");
		this.assignmentGradeConnection = new SQLTable("assignment_grade");

		idColumn.setCellValueFactory(new PropertyValueFactory<Grade, String>("idSlot"));
		gradeColumn.setCellValueFactory(new PropertyValueFactory<Grade, String>("gradeSlot"));
		editColumn.setCellValueFactory(new PropertyValueFactory<Grade, Button>("editSlot"));
		
		assignmentChoiceBox.setDisable(true);
		viewButton.setDisable(true);
		assignmentLabel.setDisable(true);
		
		populateModules();
		
	}
	
	private void drawGrades(int assignmentCode) {
		ResultSet assignmentResultSet = assignmentGradeConnection.findAllWhere("assignment_id", assignmentChoiceBox.getValue().split(" - ")[1]);

		ArrayList<String[]> dataArray = new ArrayList<String[]>();
		ArrayList<Button> buttonArray = new ArrayList<Button>();
		
		try {
			while (assignmentResultSet.next()) {
				String currentGrade = "N/A";
				if (assignmentResultSet.getString(3) != null) {
					currentGrade = assignmentResultSet.getString(3);
				}
				String[] row = {assignmentResultSet.getString(2), currentGrade};
				dataArray.add(row);
				Button editButton = new Button();
				editButton.setId(assignmentResultSet.getString(2));
				editButton.setText("Edit");
				buttonArray.add(editButton);
			}
		} catch (Exception e1) {
			System.out.println("Error: " + e1);
		}
		
		gradeTable.getItems().setAll(getData(dataArray));
	}
	
	private List<Grade> getData(ArrayList<String[]> dataArray) {
		List<Grade> x = new ArrayList<Grade>();
		
		for (int j = 0; j < dataArray.size(); j++) {
			Grade y = new Grade(dataArray.get(j)[0], dataArray.get(j)[1]);
			x.add(y);
		}

		return x;
	}
	
	private void populateAssignments() {
		if (!moduleCode.equals("")) {
			ResultSet assignments = assignmentConnection.findAllWhere("module_code", moduleCode);
			
			ArrayList<String> assignmentList = new ArrayList<String>();
			assignmentList.add("");
			
			try {
				while (assignments.next()) {
					assignmentList.add(assignments.getString(2) + " - " + assignments.getString(1));
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
		moduleCodeChoiceBox.setItems(FXCollections.observableArrayList(moduleList));
		moduleCodeChoiceBox.setValue("");
	}
	
	@FXML
	public void selectModule() {
		if (!moduleCodeChoiceBox.getValue().equals("")) {
			assignmentLabel.setDisable(false);
			viewButton.setDisable(false);
			assignmentChoiceBox.setDisable(false);
			moduleCode = moduleCodeChoiceBox.getValue().split(" - ")[1];
			populateAssignments();
		}
	}
	
	@FXML
	public void viewSelectedAssignment() {
		if (!assignmentChoiceBox.getValue().equals("")) {
			drawGrades(Integer.parseInt(assignmentChoiceBox.getValue().split(" - ")[1]));
		}
	}
		
	public class Grade {
		private final String idSlot;
		private final String gradeSlot;
		private final Button editSlot;
		
		private Grade(String id, String grade) {
			this.idSlot = new String(id);
			this.gradeSlot = new String(grade);
			this.editSlot = new Button("Edit");
			
			this.editSlot.setOnAction(new EventHandler<ActionEvent>() {
			    @Override public void handle(ActionEvent e) {
			    	
			    	String[] grades = {"A+", "A", "A-", "B+", "B", "B-", "C+", "C", "C-", "D+", "D", "D-", "E", "F", "U"};
			    	Pair<String, String[]> gradePair = new Pair<String, String[]>("Grade", grades);
			    	
			    	ArrayList<String> newRow = dialog.inputDialog("Edit grade", "Edit grade", "Save", null, null, gradePair);
			    	
			    	assignmentGradeConnection.updateAssignmentGrade(Integer.parseInt(id), newRow.get(0));

				    drawGrades(Integer.parseInt(assignmentChoiceBox.getValue().split(" - ")[1]));
		    	}
			});
			this.editSlot.setId(id);
		}
		
		public String getIdSlot() {
			return idSlot;
		}
		
		public String getGradeSlot() {
			return gradeSlot;
		}
		
		public Button getEditSlot() {
			return editSlot;
		}
		
		public String getEditID() {
			return editSlot.getId();
		}
	}
}
