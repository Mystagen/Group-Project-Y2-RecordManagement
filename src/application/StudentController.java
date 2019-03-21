package application;


import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.util.Pair;
/*
 Column 1 - Name VARCHAR2
 Column 2 - ID INT
 Column 3 - Status VARCHAR2
 Column 4 - Reason VARCHAR2
 Column 5 - address VARCHAR2
 Column 6 - contact VARCHAR2
 Column 7 - qualifications VARCHAR2
 Column 8 - course VARCHAR2
 Column 9 - attendence VARCHAR2
 
 */
public class StudentController implements Initializable {
	
	@FXML
	public Label stuID;
	public Label emailConformationLabel;
	@FXML public ChoiceBox<String> stuStatus;
	@FXML public ChoiceBox<String> stuReason;
	@FXML public ChoiceBox<String> courseText;
	public TextField stuFirstName;
	public TextField stuMiddleName;
	public TextField stuSurname;
	public TextField stuSearch;
	public TextField houseText;
	public TextField streetText;
	public TextField cityText;
	public TextField countyText;
	public TextField postcodeText;
	public TextField contactPhoneText;
	public TextField contactEmailText;
	public TextField usernameText;
	public TextArea qualificationsText;
	public TextArea attendenceText;
	
	PopupInputs dialog = new PopupInputs();
	
	SQLTable courseConnection;
	SQLTable contactConnection;
	SQLTable studentConnection;
	SQLTable qualificationConnection;
	SQLTable attendanceConnection;
	SQLTable studentAccountConnection;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		try {
			this.courseConnection = new SQLTable("courses");
			this.contactConnection = new SQLTable("contact_address");
			this.studentConnection = new SQLTable("student_records");
			this.qualificationConnection = new SQLTable("qualifications");
			this.attendanceConnection = new SQLTable("total_attendance");
			this.studentAccountConnection = new SQLTable("student_accounts");
			Class.forName("com.mysql.cj.jdbc.Driver");
			ResultSet courseSet = courseConnection.findAll();
			ArrayList<String> courses = new ArrayList<String>();
			while (courseSet.next()) {
				courses.add(courseSet.getString(1) + " - " + courseSet.getString(2));
			}
			courseText.setItems(FXCollections.observableArrayList(courses));
			stuStatus.setItems(FXCollections.observableArrayList("N/A", "PROVISIONAL", "LIVE", "DORMANT"));
			
			stuReason.setItems(FXCollections.observableArrayList("N/A", "GRADUATED", "WITHDREW", "TERMINATED"));
		} catch (Exception e) {
			System.out.print(e);
		}
	}
	
	public void studentSearch() {
		emailConformationLabel.setText("");
		if (!stuSearch.getText().equals("")) {
			try {
				int search = Integer.valueOf(stuSearch.getText()); 
				try { 
					boolean found = false;
					ResultSet studentResultSet = studentConnection.findAll();
					while (studentResultSet.next() && !found) {
						if (studentResultSet.getInt(1) == search) {
							
							stuFirstName.setText(studentResultSet.getString(4));
							stuMiddleName.setText(studentResultSet.getString(5));
							stuSurname.setText(studentResultSet.getString(6));
							stuID.setText("Student ID:"+studentResultSet.getInt(1));
							stuStatus.setValue(studentResultSet.getString(2));
							stuReason.setValue(studentResultSet.getString(3));
							ResultSet contactResultSet = contactConnection.findAllWhere("address_id", studentResultSet.getString(7));
							while (contactResultSet.next()) {
								houseText.setText(contactResultSet.getString(2));
								streetText.setText(contactResultSet.getString(3));
								cityText.setText(contactResultSet.getString(4));
								countyText.setText(contactResultSet.getString(5));
								postcodeText.setText(contactResultSet.getString(6));
							}
							contactPhoneText.setText(studentResultSet.getString(8));
							contactEmailText.setText(studentResultSet.getString(9));
							ResultSet courseResultSet = courseConnection.findAllWhere("course_code", studentResultSet.getString(10));
							ResultSet qualificationResultSet = qualificationConnection.findAllWhere("student_id", studentResultSet.getInt(1));
							while(qualificationResultSet.next()) {
								qualificationsText.setText("Institution"+qualificationResultSet.getString(3)+"Subject"+qualificationResultSet.getString(4)+"Educational_Facility"+qualificationResultSet.getString(5)+"Grade"+qualificationResultSet.getString(6));
							}
							while(courseResultSet.next()) {
								courseText.setValue(courseResultSet.getString(1) + " - " + courseResultSet.getString(2));
							}
							ResultSet attendanceResultSet = attendanceConnection.findAllWhere("student_id", studentResultSet.getInt(1));
							while(attendanceResultSet.next()) {
								attendenceText.setText("Currently Unavaliable");
							}
							ResultSet studentAccountResultSet = studentAccountConnection.findAllWhere("student_id", studentResultSet.getInt(1));
							while(studentAccountResultSet.next()) {
								usernameText.setText(studentAccountResultSet.getString(2));
							}
							found = true;
						}
						else {
							stuFirstName.setText("Name: Unavailable");
							stuMiddleName.setText("Name: Unavailable");
							stuSurname.setText("Name: Unavailable");
							stuID.setText("Student ID: unavailable");
							stuStatus.setValue("N/A");
							stuReason.setValue("N/A");
							houseText.setText("Address: Unavailable");
							streetText.setText("Address: Unavailable");
							cityText.setText("Address: Unavailable");
							countyText.setText("Address: Unavailable");
							postcodeText.setText("Address: Unavailable");
							contactEmailText.setText("Contact Information: Unavailable");
							contactPhoneText.setText("Contact Information: Unavailable");
							qualificationsText.setText("Qualifications: Unavailable");
							courseText.setValue("Course Detils: Unavailable");
							attendenceText.setText("Attendence: Unavailable");
							usernameText.setText("Username: Unavailable");
							
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
					stuFirstName.setText("Name: Unavailable");
					stuMiddleName.setText("Name: Unavailable");
					stuSurname.setText("Name: Unavailable");
					stuID.setText("Student ID: unavailable");
					stuStatus.setValue("N/A");
					stuReason.setValue("N/A");
					houseText.setText("Address: Unavailable");
					streetText.setText("Address: Unavailable");
					cityText.setText("Address: Unavailable");
					countyText.setText("Address: Unavailable");
					postcodeText.setText("Address: Unavailable");
					contactEmailText.setText("Contact Information: Unavailable");
					contactPhoneText.setText("Contact Information: Unavailable");
					qualificationsText.setText("Qualifications: Unavailable");
					courseText.setValue("Course Detils: Unavailable");
					attendenceText.setText("Attendence: Unavailable");
					usernameText.setText("Username: Unavailable");
				}
			} catch (NumberFormatException e) {
				String studentChosen = dialog.displayStudents(stuSearch.getText(), studentConnection);
				if (!studentChosen.equals("null")) {
					stuSearch.setText(studentChosen);
					studentSearch();
				}
			}
		}
	}
	
	public void resetPassword() {
		emailConformationLabel.setText("Reset email sent to student's email");
	}
	
	public void studentSave() {
		studentConnection.updateStudent(stuID.getText().split(":")[1], stuFirstName.getText(), stuMiddleName.getText(), stuSurname.getText(), houseText.getText(), streetText.getText(), cityText.getText(), countyText.getText(), postcodeText.getText(), contactPhoneText.getText(), contactEmailText.getText(), courseText.getValue().split(" - ")[0]);
		studentConnection.updateStudentStatus(stuID.getText().split(":")[1], stuStatus.getValue(), stuReason.getValue());
	}
	
	public void studentAdd() {
		String[] textFieldNames = {"Firstname", "Middle Name", "Surname", "House", "Street", "City", "County", "Postcode", "Contact Phone", "Email"};
		
		ResultSet courseResultSet = courseConnection.findAll();
		
		ArrayList<String> courseArrayList = new ArrayList<String>();
		
		try {
			while (courseResultSet.next()) {
				courseArrayList.add(courseResultSet.getString(1) + "-" + courseResultSet.getString(2));
			}
		} catch (SQLException e) {
			System.out.println("Error: " + e);
		}
		
		String[] courseList = new String[courseArrayList.size()];
		
		for (int i = 0; i<courseArrayList.size(); i++) {
			courseList[i] = courseArrayList.get(i);
		}
		
		Pair<String, String[]> coursePair = new Pair<String, String[]>("Course", courseList);
		ArrayList<String> studentDetail = dialog.inputDialog("Add a Student", "Add a Student", "Add", textFieldNames, null, coursePair);
		
		if (studentDetail.size() == 11) {
			int id = studentConnection.insertStudent(studentDetail.get(0), studentDetail.get(1), studentDetail.get(2), studentDetail.get(3), studentDetail.get(4), studentDetail.get(5), studentDetail.get(6), studentDetail.get(7), studentDetail.get(8), studentDetail.get(9), studentDetail.get(10).split("-")[0]);
			if (id != -1) {
				String username = "" + id + studentDetail.get(0).toLowerCase().charAt(0) + studentDetail.get(1).toLowerCase().charAt(0) + studentDetail.get(2).toLowerCase(); 
				studentAccountConnection.insert(id, username, "password");
			}
		}
	}

	
}
