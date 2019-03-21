package application;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Pair;

public class ModulesController {
	
	@FXML Label departmentName;
	
	@FXML Accordion coursePanel;
	
	PopupInputs dialog = new PopupInputs();
	
	@FXML
    public void initialize() {
		drawCourses();
    }
	
	SQLTable courseConnection = new SQLTable("courses");
	SQLTable moduleConnection = new SQLTable("modules");
	SQLTable moduleCourseConnection = new SQLTable("course_modules");
	SQLTable timetableConnection = new SQLTable("timetables");
	
	private ArrayList<String> addCourseDialog() {
		String[] textFieldNames = {"Course Name", "Course ID"};
		
		ArrayList<String> courseDetailArray = dialog.inputDialog("Add a Course", "Add a Course", "Add", textFieldNames, null, null);
		
		return courseDetailArray;
	}
	
	private ArrayList<String> addModuleDialog() {

		String[] textFieldNames = {"Module Name", "Module ID", "Module Description", "Module Credit"};
		
		String[] yearList = {"Foundation", "First", "Second", "Third", "Fourth"};
		
		Pair<String, String[]> yearPair = new Pair<String, String[]>("Year", yearList);
		
		ArrayList<String> moduleDetailArray = dialog.inputDialog("Add a Module", "Add a Module", "Add", textFieldNames, null, yearPair);
		
		return moduleDetailArray;
		
	}

	public void drawCourses() {
		coursePanel.getPanes().clear();

		departmentName.setText("Department of Computing");
		
		ResultSet allCoursesQueryResult = courseConnection.findAll();
		ArrayList<String> allCourses = new ArrayList<String>();
		
		try {
			//Create some ArrayLists to store all courses
			ArrayList<String> courseCodes = new ArrayList<String>();
			ArrayList<String> courseTitles = new ArrayList<String>();
			
			//Get all courses and add the respective columns to the ArrayLists
			while (allCoursesQueryResult.next()) {
				courseCodes.add(allCoursesQueryResult.getString(1));
				courseTitles.add(allCoursesQueryResult.getString(2));
			}	
			
			//For each course
			for (int x = 0; x<courseCodes.size(); x++) {
				TitledPane course;
				AnchorPane modulesDropDownContainer = new AnchorPane();
				VBox moduleList = new VBox();
				ResultSet courseModules = moduleCourseConnection.findAllWhere("course_code", courseCodes.get(x));
			
				//For each module/course link
				while (courseModules.next()) {
					ResultSet moduleResult = moduleConnection.findAllWhere("module_code", courseModules.getString(2));
					while (moduleResult.next()) {
						Label module = new Label(moduleResult.getString(2));
						Button delete = new Button("Delete");
						Button amend = new Button("Amend");
						HBox moduleContainer = new HBox();
						moduleContainer.setSpacing(10);
						moduleContainer.getChildren().addAll(module, delete, amend);
						moduleList.getChildren().add(moduleContainer);
					}
				}
				modulesDropDownContainer.getChildren().add(moduleList);
				
				String courseText = courseCodes.get(x) + " - " + courseTitles.get(x);
				course = new TitledPane(courseText, modulesDropDownContainer);
				course.setExpanded(false);
				coursePanel.getPanes().add(course);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	public void addCourse() {
		
		ArrayList<String> courseDetails = addCourseDialog(); 
		courseConnection.insert(courseDetails.get(1), courseDetails.get(0));
		ResultSet timetableIndexCounter = timetableConnection.find("timetable_id");
		int timetableCurrentID = 0;
		try {
			while (timetableIndexCounter.next()) {
				timetableCurrentID = timetableIndexCounter.getInt(1);
			}
		} catch (Exception e) {
			System.out.println("Error: " + e);
		}
		timetableConnection.insert(timetableCurrentID+1, courseDetails.get(1));
		
		drawCourses();
		
	}
	
	@FXML
	public void assignModule() {
		String[] labels = {"Course", "Module"};
		Pair<String, SQLTable> coursePair = new Pair<String, SQLTable>("course_code", courseConnection);
		Pair<String, SQLTable> modulePair = new Pair<String, SQLTable>("module_code", moduleConnection);
		Pair<String, String> modulePairing = dialog.twinPairDropBox("Assign Modules", "Assign a module to a course", labels, coursePair, modulePair);
		
		if (modulePairing.getKey() != null && modulePairing.getValue() != null) {
			moduleCourseConnection.insert(modulePairing.getKey(), modulePairing.getValue());
		}
		
		drawCourses();
	}
	
	public void addModule() {
		ArrayList<String> moduleDetails = addModuleDialog();

		if (moduleDetails.size() == 5) {
			moduleConnection.insert(moduleDetails.get(1), moduleDetails.get(0), moduleDetails.get(2), moduleDetails.get(4), moduleDetails.get(3));
		}
	}
	
	@FXML
	public void searchDepartment() {
		
	}

}
