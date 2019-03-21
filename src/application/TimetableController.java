package application;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;


import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Pair;

public class TimetableController {
	
	@FXML private TableView<Timetable> courseTable;
	@FXML private TableColumn<Timetable, String> timeColumn;
	@FXML private TableColumn<Timetable, String> mondayColumn;
	@FXML private TableColumn<Timetable, String> tuesdayColumn;
	@FXML private TableColumn<Timetable, String> wednesdayColumn;
	@FXML private TableColumn<Timetable, String> thursdayColumn;
	@FXML private TableColumn<Timetable, String> fridayColumn;
	@FXML private TableColumn<Timetable, Button> editColumn;
	
	@FXML private ChoiceBox<String> courseSelect;
	
	public static SQLTable courseConnection = new SQLTable("courses");
	public static SQLTable timetableConnection = new SQLTable("timetables");
	public static SQLTable timetableModulesConnection = new SQLTable("timetables_modules");
	public static SQLTable modulesConnection = new SQLTable("modules");
	
	static PopupInputs dialog = new PopupInputs();

	String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};
	
	ArrayList<String> courses = new ArrayList<String>();
	
	@FXML
	public void courseTimetableSearch() {
		load(courseSelect.getValue().split(" - ")[0]);
	}
	
	@FXML
	protected void initialize()  {
		timeColumn.setCellValueFactory(new PropertyValueFactory<Timetable, String>("time"));
		mondayColumn.setCellValueFactory(new PropertyValueFactory<Timetable, String>("mondaySlot"));
		tuesdayColumn.setCellValueFactory(new PropertyValueFactory<Timetable, String>("tuesdaySlot"));
		wednesdayColumn.setCellValueFactory(new PropertyValueFactory<Timetable, String>("wednesdaySlot"));
		thursdayColumn.setCellValueFactory(new PropertyValueFactory<Timetable, String>("thursdaySlot"));
		fridayColumn.setCellValueFactory(new PropertyValueFactory<Timetable, String>("fridaySlot"));
		editColumn.setCellValueFactory(new PropertyValueFactory<Timetable, Button>("editSlot"));
		
		ResultSet courseResultSet = courseConnection.findAll();
		
		try {
			while (courseResultSet.next()) {
				courses.add(courseResultSet.getString(1) + " - " + courseResultSet.getString(2));
			}
			
			courseSelect.setItems(FXCollections.observableArrayList(courses));
		} catch (Exception e) {
			System.out.println("Error: " + e);
			e.printStackTrace();
		}
	}

	@FXML
	public void load(String searchCriteria) {
		ResultSet timetableResults = timetableConnection.findAllWhere("course_code", searchCriteria);
		String selectedTimetableID = "";
		try {
			while (timetableResults.next()) {
				selectedTimetableID = timetableResults.getString(1);
			}
		} catch (Exception e1) {
			System.out.println("Error: " + e1);
		}
		ResultSet timetableEntries = timetableModulesConnection.findAllWhere("timetable_id", selectedTimetableID);
		
		String[][] dataArray = new String[][] {
			{"", "", "", "", "", "0"}, 
			{"", "", "", "", "", "1"}, 
			{"", "", "", "", "", "2"}, 
			{"", "", "", "", "", "3"}, 
			{"", "", "", "", "", "4"}, 
			{"", "", "", "", "", "5"}, 
			{"", "", "", "", "", "6"}, 
			{"", "", "", "", "", "7"}, 
			{"", "", "", "", "", "8"}, 
			{"", "", "", "", "", "9"}
		};
		
		try {
			while (timetableEntries.next()) {
				int timeIndex = Integer.parseInt(timetableEntries.getString(5).split(":")[0])-9;
				int dayIndex = 0;
				
				switch (timetableEntries.getString(6)) {
				case "MONDAY":
					break;
				case "TUESDAY":
					dayIndex = 1;
					break;
				case "WEDNESDAY":
					dayIndex = 2;
					break;
				case "THURSDAY":
					dayIndex = 3;
					break;
				case "FRIDAY":
					dayIndex = 4;
					break;
				}
				
				dataArray[timeIndex][dayIndex] = timetableEntries.getString(2);
				
			}
		} catch (SQLException e) {
			System.out.println("Error: " + e);
		} catch (NullPointerException e) {
			System.out.println("TEMP");
		}
		
		courseTable.getItems().setAll(getData(dataArray));
	}
	
	//Takes the data provided and adds it to a List object of type specified within the angled brackets <>
	private List<Timetable> getData(String[][] dataArray) {
		List<Timetable> x = new ArrayList<Timetable>();
		int hour = 9;
		
		for (int j = 0; j < dataArray.length; j++) {
			String[] data = dataArray[j];
			Timetable y = new Timetable(hour + ":00", data);
			x.add(j, y);
			hour++;
		}

		return x;
	}
	
	public class Timetable {
		private final String time;
		private final String mondaySlot;
		private final String tuesdaySlot;
		private final String wednesdaySlot;
		private final String thursdaySlot;
		private final String fridaySlot;
		private final Button editSlot;
		
		private Timetable(String time, String[] timetableSlots) {
			this.time = new String(time);
			this.mondaySlot = new String(timetableSlots[0]);
			this.tuesdaySlot = new String(timetableSlots[1]);
			this.wednesdaySlot = new String(timetableSlots[2]);
			this.thursdaySlot = new String(timetableSlots[3]);
			this.fridaySlot = new String(timetableSlots[4]);
			this.editSlot = new Button("Edit");
			this.editSlot.setOnAction(new EventHandler<ActionEvent>() {
			    @Override public void handle(ActionEvent e) {
			    	Button source = (Button) e.getSource();
			    	Map<String, String> timeMapping = new HashMap<String, String>();
			    	for (int timeCount = 0; timeCount < 10; timeCount++) {
			    		if (timeCount == 0) {
			    			timeMapping.put("" + timeCount, "09:00:00");
			    		} else {
			    			timeMapping.put("" + timeCount, (9+timeCount) + ":00:00");
			    		}
			    	}
			    	ArrayList<Pair<String, Pair<String, SQLTable>>> modules = new ArrayList<Pair<String, Pair<String, SQLTable>>>();
			    	for (int i = 0; i < 5; i++) {
				    	modules.add(new Pair<String, Pair<String, SQLTable>>(timetableSlots[i], new Pair<String, SQLTable>(days[i], modulesConnection)));
			    	}
			    	
			    	ArrayList<String> newRow = dialog.inputDialog("Edit timetable", "Edit timetable", "Save", null, modules, null);
			    	
			    	String course = "";
		    		
		    		if (courseSelect.getValue() != null) {
		    			course = courseSelect.getValue().split(" - ")[0];
		    		}
		    		
			    	ResultSet timetableResultSet = timetableConnection.findAllWhere("course_code", course);
			    	
			    	String timetableId = null;
			    	
			    	try {
			    		while (timetableResultSet.next()) {
			    			timetableId = timetableResultSet.getString(1);
			    		}
					} catch (Exception e2) {
						System.out.println("Error e2: " + e2);
					}
			    	
			    	for (int moduleSlot = 0; moduleSlot < 5; moduleSlot++) {
			    		String module = "";
			    		
			    		if (newRow.size() > 0) {
				    		if (newRow.get(moduleSlot) != null) {
				    			module = newRow.get(moduleSlot).split(" - ")[0];
				    		}
			    		}
			    		
			    		timetableModulesConnection.updateTimetableModule(timetableId, module, timeMapping.get(source.getId()), days[moduleSlot]);
			    		
			    	}
			    	
			    	load(courseSelect.getValue().split(" - ")[0]);
			    }
			});
			this.editSlot.setId(timetableSlots[5]);
		}
		
		public String getTime() {
			return time;
		}
		
		public String getMondaySlot() {
			return mondaySlot;
		}
		
		public String getTuesdaySlot() {
			return tuesdaySlot;
		}
		
		public String getWednesdaySlot() {
			return wednesdaySlot;
		}
		
		public String getThursdaySlot() {
			return thursdaySlot;
		}
		
		public String getFridaySlot() {
			return fridaySlot;
		}
		
		public Button getEditSlot() {
			return editSlot;
		}
		
		public String getEditID() {
			return editSlot.getId();
		}
	}
}
