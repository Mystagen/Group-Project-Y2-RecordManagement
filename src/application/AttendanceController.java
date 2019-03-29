package application;

import java.net.URL;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import application.GradesController.Grade;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Pair;

public class AttendanceController implements Initializable {
	
	@FXML private TableView<Attendance> attendanceTable;
	@FXML private TableColumn<Attendance, String> idColumn;
	@FXML private TableColumn<Attendance, String> attendanceColumn;

	@FXML private Button enterDataButton;
	
	@FXML private ChoiceBox<String> moduleSelect;
	
	SQLTable moduleConnection;
	SQLTable attendanceConnection;
	
	static PopupInputs dialog = new PopupInputs();
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		this.moduleConnection = new SQLTable("modules");
		this.attendanceConnection = new SQLTable("total_attendance");

		idColumn.setCellValueFactory(new PropertyValueFactory<Attendance, String>("idSlot"));
		attendanceColumn.setCellValueFactory(new PropertyValueFactory<Attendance, String>("attendanceSlot"));
		
		enterDataButton.setDisable(true);
		
		populateModules();
		
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
		moduleSelect.setItems(FXCollections.observableArrayList(moduleList));
		moduleSelect.setValue("");
	}

	@FXML
	private void enterData() {
		ArrayList<String> attendedIDs = dialog.addAttendance(attendanceConnection, moduleSelect.getValue().split(" - ")[1]);
		for (int i = 0; i < attendedIDs.size(); i++) {
			attendanceConnection.updatePersonalAttendance(attendedIDs.get(i), moduleSelect.getValue().split(" - ")[1]);
			attendanceConnection.updateClasses(moduleSelect.getValue().split(" - ")[1]);
		}
		drawAttendance();
	}
	
	private void drawAttendance() {
		ResultSet attendanceResultSet = attendanceConnection.findAllWhere("module_code", moduleSelect.getValue().split(" - ")[1]);

		ArrayList<String[]> dataArray = new ArrayList<String[]>();
		
		ArrayList<Integer> classesAttended = new ArrayList<Integer>();
		ArrayList<Integer> classesRun = new ArrayList<Integer>();
		
		int counter = 0;
		
		try {
			while (attendanceResultSet.next()) {
				float attended;
				if (attendanceResultSet.getString(4) != null && attendanceResultSet.getString(3) != null) {
					classesRun.add(attendanceResultSet.getInt(4));
					classesAttended.add(attendanceResultSet.getInt(3));
				} else {
					classesRun.add(0);
					classesAttended.add(0);
				}
				if (classesRun.get(counter) != 0) {
					if (classesAttended.get(counter) == 0) {
						attended = 0;
					} else {
						attended = ((float) classesAttended.get(counter)/classesRun.get(counter))*100;
					}
				} else {
					attended = classesAttended.get(counter);
				}
				String[] row = {attendanceResultSet.getString(1), Integer.toString((int)attended)};
				dataArray.add(row);
				counter++;
			}
		} catch (Exception e1) {
			System.out.println("Error: " + e1);
		}
		
		attendanceTable.getItems().setAll(getData(dataArray));
	}
	
	private List<Attendance> getData(ArrayList<String[]> dataArray) {
		List<Attendance> x = new ArrayList<Attendance>();
		
		for (int j = 0; j < dataArray.size(); j++) {
			Attendance y = new Attendance(dataArray.get(j)[0], dataArray.get(j)[1]);
			x.add(y);
		}

		return x;
	}
	
	@FXML
	private void viewModuleAttendance() {
		enterDataButton.setDisable(false);
		drawAttendance();
		
	}
	
	public class Attendance {
		private final String idSlot;
		private final String attendanceSlot;
		
		private Attendance(String id, String attendance) {
			this.idSlot = new String(id);
			this.attendanceSlot = new String(attendance);
		}
		
		public String getIdSlot() {
			return idSlot;
		}
		
		public String getAttendanceSlot() {
			return attendanceSlot;
		}
	}
}
