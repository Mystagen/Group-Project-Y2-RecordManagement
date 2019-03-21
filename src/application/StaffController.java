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
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.util.Pair;

public class StaffController implements Initializable {
	
	@FXML
	public TextField searchField;
	@FXML
	public Label staffID; // Found through staffConnection
	@FXML
	public TextField houseText;
	public TextField streetText;
	public TextField cityText;
	public TextField countyText;
	public TextField postcodeText;
	public TextField staffFirstName;
	public TextField staffMiddleName;
	public TextField staffSurname;
	public TextField contactPhoneText;
	public TextField contactEmailText;
	
	@FXML public ChoiceBox<String> moduleList;
	@FXML public ChoiceBox<String> staffRoll;
	
	@FXML public VBox moduleTaughtContainer;
	
	PopupInputs dialog = new PopupInputs();
	
	SQLTable staffConnection; // Contact info, Name, Status, Reason
	SQLTable contactConnection; // Address information
	SQLTable moduleConnection; // Module info for course
	
	
	public void initialize(URL arg0, ResourceBundle arg1) {
		try {
			this.staffConnection = new SQLTable("staff");
			this.contactConnection = new SQLTable("contact_address");
			this.moduleConnection = new SQLTable("staff_modules");
			this.staffRoll.setItems(FXCollections.observableArrayList("N/A", "LECTURER", "ADMINISTRATOR"));
		} catch(Exception e) {
			System.out.print(e);
		}
	}
	
	
	public void staffSearch() {
		try {
			if(!searchField.getText().equals("")) {
				try {
					int search = Integer.valueOf(searchField.getText());
					try {
						ResultSet staffResultSet = staffConnection.findAll();
						while(staffResultSet.next()) {
							if(staffResultSet.getInt(1)==search) {
								staffFirstName.setText(staffResultSet.getString(2));
								staffMiddleName.setText(staffResultSet.getString(3));
								staffSurname.setText(staffResultSet.getString(4));
								staffID.setText("Staff ID:"+staffResultSet.getInt(1));
								ResultSet contactResultSet = contactConnection.findAllWhere("address_id",staffResultSet.getInt(5));
								while(contactResultSet.next()) {
									houseText.setText(contactResultSet.getString(2));
									streetText.setText(contactResultSet.getString(3));
									cityText.setText(contactResultSet.getString(4));
									countyText.setText(contactResultSet.getString(5));
									postcodeText.setText(contactResultSet.getString(6));
								}
								contactPhoneText.setText(staffResultSet.getString(6));
								contactEmailText.setText(staffResultSet.getString(7));
								staffRoll.setValue(staffResultSet.getString(8));
								
							}else {
								staffID.setText("Staff ID:" + searchField.getText());
								staffFirstName.setText("Unavailable");
								staffMiddleName.setText("Unavailable");
								staffSurname.setText("Unavailable");
								houseText.setText("Unavailable");
								streetText.setText("Unavailable");
								cityText.setText("Unavailable");
								countyText.setText("Unavailable");
								postcodeText.setText("Unavailable");
								contactPhoneText.setText("Unavailable");
								contactEmailText.setText("Unavailable");
								staffRoll.setValue("N/A");
							}
						}
					} catch (Exception e) {
						staffID.setText("Staff ID: "+ searchField.getText());
						staffFirstName.setText("Unavailable");
						staffMiddleName.setText("Unavailable");
						staffSurname.setText("Unavailable");
						houseText.setText("Unavailable");
						streetText.setText("Unavailable");
						cityText.setText("Unavailable");
						countyText.setText("Unavailable");
						postcodeText.setText("Unavailable");
						contactPhoneText.setText("Unavailable");
						contactEmailText.setText("Unavailable");
						staffRoll.setValue("N/A");
					}
				} catch (NumberFormatException e) {
					String staffChosen = dialog.displayStaff(searchField.getText(), staffConnection);
					if (!staffChosen.equals("null")) {
						searchField.setText(staffChosen);
						staffSearch();
					}
				}
			}
			
		}catch(Exception e){
			staffID.setText("Staff ID:"+ searchField.getText());
			staffFirstName.setText("Unavailable");
			staffMiddleName.setText("Unavailable");
			staffSurname.setText("Unavailable");
			houseText.setText("Unavailable");
			streetText.setText("Unavailable");
			cityText.setText("Unavailable");
			countyText.setText("Unavailable");
			postcodeText.setText("Unavailable");
			contactPhoneText.setText("Unavailable");
			contactEmailText.setText("Unavailable");
			staffRoll.setValue("N/A");
		}
	}
	
	public void staffSave(){
		staffConnection.updateStaff(staffID.getText().split(":")[1], staffFirstName.getText(), staffMiddleName.getText(), staffSurname.getText(), houseText.getText(), streetText.getText(), cityText.getText(), countyText.getText(), postcodeText.getText(), contactPhoneText.getText(), contactEmailText.getText(), staffRoll.getValue());
	}
	
	public void addModule() {
		System.out.println("Add module");
	}
	
	public void staffAdd() {
		String[] textFieldNames = {"Firstname", "Middle Name", "Surname", "House", "Street", "City", "County", "Postcode", "Contact Phone", "Email"};
		
		String[] roles = {"N/A", "LECTURER", "ADMINISTRATOR"};
		
		Pair<String, String[]> role = new Pair<String, String[]>("Role", roles);
		
		ArrayList<String> staffDetail = dialog.inputDialog("Add a Member of Staff", "Add a Member of Staff", "Add", textFieldNames, null, role);

		if (staffDetail.size() == 11) {
			staffConnection.insertStaff(staffDetail.get(0), staffDetail.get(1), staffDetail.get(2), staffDetail.get(3), staffDetail.get(4), staffDetail.get(5), staffDetail.get(6), staffDetail.get(7), staffDetail.get(8), staffDetail.get(9), staffDetail.get(10));
		}
	}
}
