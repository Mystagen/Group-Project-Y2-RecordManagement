package application;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Pair;

public class PopupInputs {
	
	public GridPane grid = new GridPane();
	public ChoiceBox<String> choice1 = new ChoiceBox<String>();
	public ChoiceBox<String> choice2 = new ChoiceBox<String>();
	String viewID = "";
	
	private ChoiceBox<String> populateChoiceBox(ArrayList<String> data) {
		ChoiceBox<String> choiceBox = new ChoiceBox<String>(
				FXCollections.observableArrayList(data)
			);
		
		return choiceBox;
	}
	
	private ArrayList<String> setGeneration (Pair<String, SQLTable> table) {
		ResultSet resultSet = table.getValue().findAll();
		
		ArrayList<String> set = new ArrayList<String>();
		
		try {
			while (resultSet.next()) {
				String label = resultSet.getString(1) + "-" + resultSet.getString(2);
				set.add(label);
			}
		} catch (SQLException e) {
			System.out.println("Error: " + e);
		}
		
		return set;
	}
	
	public void placeChoiceBoxes(ArrayList<String> set1, ArrayList<String> set2, ChoiceBox<String> choice1, ChoiceBox<String> choice2) {
		choice1 = populateChoiceBox(set1); 
		choice2 = populateChoiceBox(set2); 
		grid.add(choice1, 1, 1);
		grid.add(choice2, 4, 1);
	}
	
	private void setId(String id) {
		viewID = id;
	}
	
	public String displayStudents(String studentName, SQLTable connection) {
		try {
			ResultSet studentSet = connection.findAllWhere("firstname", studentName);
			Dialog<ArrayList<String>> dialog = new Dialog<ArrayList<String>>();
			dialog.setTitle("Search Results");
			dialog.setHeaderText("Results for students called " + studentName);
			
			ButtonType selectButtonType = new ButtonType("View Selected");
			
			dialog.getDialogPane().getButtonTypes().addAll(selectButtonType, ButtonType.CANCEL);
			
			ArrayList<Label> studentIDList = new ArrayList<Label>();
			ArrayList<Label> studentNameList = new ArrayList<Label>();
			ArrayList<Label> studentMiddleNameList = new ArrayList<Label>();
			ArrayList<Label> studentSurnameList = new ArrayList<Label>();
			ArrayList<Button> studentButtonList = new ArrayList<Button>();
			ArrayList<RadioButton> studentRadioButtonList = new ArrayList<RadioButton>();
			
			GridPane layout = new GridPane();
			layout.setHgap(10);
			layout.setVgap(10);
			layout.setPadding(new Insets(20, 150, 10, 10));
			
			final ToggleGroup selectedStudent = new ToggleGroup();
			
			int index = 0;
			while (studentSet.next()) {
				Label id = new Label(studentSet.getString(1));
				studentIDList.add(id);
				Label name = new Label(studentSet.getString(4));
				studentNameList.add(name);
				Label middlename = new Label(studentSet.getString(5));
				studentMiddleNameList.add(middlename);
				Label surname = new Label(studentSet.getString(6));
				studentSurnameList.add(surname);
				Button search = new Button("Select");
				
				search.setOnAction(new EventHandler<ActionEvent>() {
				    @Override public void handle(ActionEvent e) {
				        Button source = (Button) e.getSource();
				    	for (int x = 0; x < studentRadioButtonList.size(); x++) {
				    		if (studentRadioButtonList.get(x).getId().equals(source.getId())) {
				    			studentRadioButtonList.get(x).setSelected(true);
				    		}
				    	}
				        setId(source.getId());
				    }
				});
				
				search.setId(id.getText());
				studentButtonList.add(search);
				RadioButton selectedRB = new RadioButton();
				selectedRB.setToggleGroup(selectedStudent);
				selectedRB.setId(id.getText());
				selectedRB.setMouseTransparent(true);
				studentRadioButtonList.add(selectedRB);
				layout.add(id, 0, index);
				layout.add(name, 1, index);
				layout.add(middlename, 2, index);
				layout.add(surname, 3, index);
				layout.add(search, 4, index);
				layout.add(selectedRB, 5, index);
				index++;
			}
			
			dialog.getDialogPane().setContent(layout);
			
			dialog.setResultConverter(dialogButton -> {
			    if (dialogButton == selectButtonType) {
					if (!viewID.equals("")) {
				    	ArrayList<String> dataExtract = new ArrayList<String>();
				    	dataExtract.add(0, viewID);
				    	
				        return dataExtract;
					} else {
						return null;
					}
			    } else {
			    	return null;
			    }
			});

			
			Optional<ArrayList<String>> result = dialog.showAndWait();
			ArrayList<String> idList = new ArrayList<String>();
			
			result.ifPresent(details -> {
				idList.add(details.get(0));
			});

			return idList.get(0);
			
			
		} catch (Exception e) {
		}
		return null;
		
	}
	
	public String displayStaff(String staffName, SQLTable connection) {
		try {
			ResultSet staffSet = connection.findAllWhere("firstname", staffName);
			Dialog<ArrayList<String>> dialog = new Dialog<ArrayList<String>>();
			dialog.setTitle("Search Results");
			dialog.setHeaderText("Results for staff called " + staffName);
			
			ButtonType selectButtonType = new ButtonType("View Selected");
			
			dialog.getDialogPane().getButtonTypes().addAll(selectButtonType, ButtonType.CANCEL);
			
			ArrayList<Label> staffIDList = new ArrayList<Label>();
			ArrayList<Label> staffNameList = new ArrayList<Label>();
			ArrayList<Label> staffMiddleNameList = new ArrayList<Label>();
			ArrayList<Label> staffSurnameList = new ArrayList<Label>();
			ArrayList<Button> staffButtonList = new ArrayList<Button>();
			ArrayList<RadioButton> staffRadioButtonList = new ArrayList<RadioButton>();
			
			GridPane layout = new GridPane();
			layout.setHgap(10);
			layout.setVgap(10);
			layout.setPadding(new Insets(20, 150, 10, 10));
			
			final ToggleGroup selectedStaff = new ToggleGroup();
			
			int index = 0;
			while (staffSet.next()) {
				Label id = new Label(staffSet.getString(1));
				staffIDList.add(id);
				Label name = new Label(staffSet.getString(2));
				staffNameList.add(name);
				Label middlename = new Label(staffSet.getString(3));
				staffMiddleNameList.add(middlename);
				Label surname = new Label(staffSet.getString(4));
				staffSurnameList.add(surname);
				Button search = new Button("Select");
				
				search.setOnAction(new EventHandler<ActionEvent>() {
				    @Override public void handle(ActionEvent e) {
				        Button source = (Button) e.getSource();
				    	for (int x = 0; x < staffRadioButtonList.size(); x++) {
				    		if (staffRadioButtonList.get(x).getId().equals(source.getId())) {
				    			staffRadioButtonList.get(x).setSelected(true);
				    		}
				    	}
				        setId(source.getId());
				    }
				});
				
				search.setId(id.getText());
				staffButtonList.add(search);
				RadioButton selectedRB = new RadioButton();
				selectedRB.setToggleGroup(selectedStaff);
				selectedRB.setId(id.getText());
				selectedRB.setMouseTransparent(true);
				staffRadioButtonList.add(selectedRB);
				layout.add(id, 0, index);
				layout.add(name, 1, index);
				layout.add(middlename, 2, index);
				layout.add(surname, 3, index);
				layout.add(search, 4, index);
				layout.add(selectedRB, 5, index);
				index++;
			}
			
			dialog.getDialogPane().setContent(layout);
			
			dialog.setResultConverter(dialogButton -> {
			    if (dialogButton == selectButtonType) {
					if (!viewID.equals("")) {
				    	ArrayList<String> dataExtract = new ArrayList<String>();
				    	dataExtract.add(0, viewID);
				    	
				        return dataExtract;
					} else {
						return null;
					}
			    } else {
			    	return null;
			    }
			});

			
			Optional<ArrayList<String>> result = dialog.showAndWait();
			ArrayList<String> idList = new ArrayList<String>();
			
			result.ifPresent(details -> {
				idList.add(details.get(0));
			});

			return idList.get(0);
			
			
		} catch (Exception e) {
		}
		return null;
		
	}
	
	public Pair<String, String> twinPairDropBox(String title, String promptText, String[] labelLabels, Pair<String, SQLTable> table1, Pair<String, SQLTable> table2) {
		
		Dialog<ArrayList<String>> dialog = new Dialog<>();
		
		dialog.setTitle(title);
		dialog.setHeaderText(promptText);
		
		ButtonType functionButtonType = new ButtonType("Pair", ButtonData.OK_DONE);
		dialog.getDialogPane().getButtonTypes().addAll(functionButtonType, ButtonType.CANCEL);
		
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(20, 150, 10, 10));
		
		ArrayList<String> set1 = setGeneration(table1);
		ArrayList<String> set2 = setGeneration(table2);
		
		choice1 = populateChoiceBox(set1); 
		choice2 = populateChoiceBox(set2); 
		
		grid.add(new Label(labelLabels[0] + ":"), 0, 0);
		grid.add(choice1, 1, 0);
		grid.add(new Label("<->"), 2, 0);
		grid.add(new Label(labelLabels[1] + ":"), 3, 0);
		grid.add(choice2, 4, 0);
		
		dialog.getDialogPane().setContent(grid);
		
		dialog.setResultConverter(dialogButton -> {
		    if (dialogButton == functionButtonType) {
		    	ArrayList<String> dataExtract = new ArrayList<String>();
		    	dataExtract.add(0, choice1.getValue().toString().split("-")[0]);
		    	dataExtract.add(1, choice2.getValue().toString().split("-")[0]);
		    	
		        return dataExtract;
		    }
		    return null;
		});

		Pair<String, String> submittedResults;
		String[] tempList = new String[2];
		
		Optional<ArrayList<String>> result = dialog.showAndWait();
		
		result.ifPresent(details -> {
			tempList[0] = details.get(0);
			tempList[1] = details.get(1);
		});

		submittedResults = new Pair<String, String>(tempList[0], tempList[1]);
		
		return submittedResults;
	}
	
	public ArrayList<String> inputDialog(String title, String promptText, String buttonPromptText, String[] textFieldKeys, ArrayList<Pair<String, Pair<String, SQLTable>>> sqlChoiceBox, Pair<String,String[]> cbOptions) {
		
		Dialog<ArrayList<String>> dialog = new Dialog<>();
		
		dialog.setTitle(title);
		dialog.setHeaderText(promptText);
		
		ButtonType functionButtonType = new ButtonType(buttonPromptText, ButtonData.OK_DONE);
		dialog.getDialogPane().getButtonTypes().addAll(functionButtonType);
		
		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(20, 150, 10, 10));
		
		ArrayList<TextField> textFields = new ArrayList<TextField>();
		
		ArrayList<ChoiceBox<String>> allSQLChoiceBoxes = new ArrayList<ChoiceBox<String>>();
		
		
		if (sqlChoiceBox != null) {
			for (int i = 0; i < sqlChoiceBox.size(); i++) {

				ArrayList<String> allCourses = new ArrayList<String>();
				allCourses.add(null);
				
				ResultSet allCoursesQueryResult = sqlChoiceBox.get(i).getValue().getValue().findAll();
				
				try {
					while (allCoursesQueryResult.next()) {
						String choiceCourseText = allCoursesQueryResult.getString(1) + " - " + allCoursesQueryResult.getString(2);
						allCourses.add(choiceCourseText);
					}
				} catch (SQLException e) {
					System.out.println("Error: " + e);
				}
				ChoiceBox<String> tableChoiceBox = new ChoiceBox<String>(
						FXCollections.observableArrayList(allCourses)
					);

				if (sqlChoiceBox.get(i).getKey() != null) {
					for (int counter = 1; counter < allCourses.size(); counter++) {
						if (sqlChoiceBox.get(i).getKey().equals(allCourses.get(counter).split(" - ")[0])) {
							tableChoiceBox.setValue(allCourses.get(counter));
						}
					}
				}
				allSQLChoiceBoxes.add(tableChoiceBox);
			}
		}
		
		
		
		ArrayList<String> predefinedChoiceStrings = new ArrayList<String>();
		
		if (cbOptions != null) {
			for (int a=0; a < cbOptions.getValue().length; a++) {
				predefinedChoiceStrings.add(a, cbOptions.getValue()[a]);
			}
		}
		
		ChoiceBox<String> predefinedChoiceBox = new ChoiceBox<String>(
				FXCollections.observableArrayList(predefinedChoiceStrings)
			);
		
		int overallIndex = 0;
		
		if (textFieldKeys != null) {
			for (int i=0; i<textFieldKeys.length; i++) {
				TextField textField = new TextField();
				textField.setPromptText(textFieldKeys[i]);
				textFields.add(i, textField);
				grid.add(new Label(textField.getPromptText() + ":"), 0, i);
				grid.add(textField, 1, i);

				if (i+1 == textFieldKeys.length) {
					overallIndex = i+1;
				}
			}
		}
		
		if (sqlChoiceBox != null) {
			for (int x=0; x < allSQLChoiceBoxes.size(); x++) {
				grid.add(new Label(sqlChoiceBox.get(x).getValue().getKey() + ":"), 0, overallIndex);
				grid.add(allSQLChoiceBoxes.get(x), 1, overallIndex);
				overallIndex++;
			}
		}
		
		if (cbOptions != null) {
			grid.add(new Label(cbOptions.getKey() + ":"), 0, overallIndex);
			grid.add(predefinedChoiceBox, 1, overallIndex);
			overallIndex++;
		}
		
		

		dialog.getDialogPane().setContent(grid);
		
		dialog.setResultConverter(dialogButton -> {
		    if (dialogButton == functionButtonType) {
		    	ArrayList<String> moduleDataExtract = new ArrayList<String>();
		    	int index = 0;
		    	for (int i = 0; i<textFields.size(); i++) {
			    	moduleDataExtract.add(index, textFields.get(i).getText());
			    	index++;
		    	}
		    	if (sqlChoiceBox != null) {
		    		for (int i=0; i<allSQLChoiceBoxes.size(); i++) {
				    	moduleDataExtract.add(index, allSQLChoiceBoxes.get(i).getValue());
				    	index++;
		    		}
		    	}
		    	if (cbOptions != null) {
			    	moduleDataExtract.add(index, predefinedChoiceBox.getValue());
			    	index++;
		    	}
		    	
		        return moduleDataExtract;
		    }
		    return null;
		});
		
		ArrayList<String> moduleDetailArray = new ArrayList<String>();
		
		Optional<ArrayList<String>> result = dialog.showAndWait();
		
		result.ifPresent(courseDetails -> {
			for (int i = 0; i<courseDetails.size(); i++) {
				moduleDetailArray.add(i, courseDetails.get(i));
			}
		});
		
		return moduleDetailArray;
	}
	
	public int staffSelection(SQLTable staffConnection) {
		
		Dialog<ArrayList<String>> dialog = new Dialog<>();
		
		dialog.setTitle("Add a tutor");
		dialog.setHeaderText("Select a member of staff to make a personal tutor");
		
		ButtonType functionButtonType = new ButtonType("Add", ButtonData.OK_DONE);
		dialog.getDialogPane().getButtonTypes().addAll(functionButtonType);
		
		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(20, 150, 10, 10));

		ArrayList<RadioButton> staffRadioButtonList = new ArrayList<RadioButton>();
		
		final ToggleGroup selectedStaff = new ToggleGroup();
		
		ResultSet staff = staffConnection.findAll();
		
		int counter = 0;
		
		try {
			while (staff.next()) {
				RadioButton selectedRB = new RadioButton();
				selectedRB.setToggleGroup(selectedStaff);
				
				selectedRB.setId(staff.getString(1));
				
				staffRadioButtonList.add(selectedRB);
				
				grid.add(new Label(staff.getString(1)), 0, counter);
				grid.add(new Label(staff.getString(2)), 1, counter);
				grid.add(new Label(staff.getString(3)), 2, counter);
				grid.add(new Label(staff.getString(4)), 3, counter);
				grid.add(selectedRB, 4, counter);
				counter++;
			}
		} catch (Exception e) {
			System.out.println("Staff Select Error: " + e);
			e.printStackTrace();
		}
		
		dialog.getDialogPane().setContent(grid);
		
		dialog.setResultConverter(dialogButton -> {
		    if (dialogButton == functionButtonType) {
		    	ArrayList<String> dataExtract = new ArrayList<String>();
		    	for (int i = 0; i < staffRadioButtonList.size(); i++) {
		    		if (staffRadioButtonList.get(i).isSelected()) {
				    	dataExtract.add(0, staffRadioButtonList.get(i).getId());
		    		}
		    	}
		    	
		        return dataExtract;
		    }
		    return null;
		});

		int[] returnID = {-1};
		
		Optional<ArrayList<String>> result = dialog.showAndWait();
		
		try {
			result.ifPresent(details -> {
				returnID[0] = Integer.parseInt(details.get(0));
			});
		} catch (Exception e) {
			returnID[0] = -1;
		}

		return returnID[0];
		
	}
	
	public ArrayList<String> addAttendance(SQLTable attendanceConnection, String module_code) {
		ArrayList<String> attendanceIDs = new ArrayList<String>();
		
		ResultSet attendingStudents = attendanceConnection.findAllWhere("module_code", module_code);
		
		ArrayList<String> studentIDs = new ArrayList<String>();
		ArrayList<CheckBox> checkboxList = new ArrayList<CheckBox>();
		
		try {
			while (attendingStudents.next()) {
				studentIDs.add(attendingStudents.getString(1));
			}
			
			for (int i = 0; i < studentIDs.size(); i++) {
				CheckBox attendCB = new CheckBox();
				attendCB.setId(studentIDs.get(i));
				checkboxList.add(attendCB);
			}
			
			Dialog<ArrayList<String>> dialog = new Dialog<>();
			
			dialog.setTitle("Attendance");
			dialog.setHeaderText("Select students who attended");
			
			ButtonType functionButtonType = new ButtonType("Submit", ButtonData.OK_DONE);
			dialog.getDialogPane().getButtonTypes().addAll(functionButtonType);
			
			VBox content = new VBox();
			
			for (int i = 0; i < studentIDs.size(); i++) {
				
				HBox row = new HBox();
				
				row.getChildren().addAll(new Label(studentIDs.get(i)), checkboxList.get(i));
				
				content.getChildren().add(row);
			}
			
			dialog.getDialogPane().setContent(content);
			
			dialog.setResultConverter(dialogButton -> {
			    if (dialogButton == functionButtonType) {
			    	ArrayList<String> dataExtract = new ArrayList<String>();
			    	for (int i = 0; i < checkboxList.size(); i++) {
			    		if (checkboxList.get(i).isSelected()) {
					    	dataExtract.add(checkboxList.get(i).getId());
			    		}
			    	}
			        return dataExtract;
			    }
			    return null;
			});
			
			Optional<ArrayList<String>> result = dialog.showAndWait();
			
			try {
				result.ifPresent(details -> {
					for (int i = 0; i < details.size(); i++) {
						attendanceIDs.add(details.get(i));
					}
				});
			} catch (Exception e) {
				
			}
			
			
			
			return attendanceIDs;
			
		} catch (SQLException e) {
			System.out.println("Error: "  + e);
			e.printStackTrace();
		}
		
		return attendanceIDs;
	}
	
	public ArrayList<String> multipleStudentSelect(SQLTable studentConnection, SQLTable tutorialStudentConnections, int tutorID) {

		ArrayList<String> returnNames = new ArrayList<String>();
		
		ArrayList<String> idsInclude = new ArrayList<String>();
		
		ArrayList<String> studentIdIgnore = new ArrayList<String>();
		
		ResultSet tsID = tutorialStudentConnections.findAll();
		
		ArrayList<CheckBox> checkBoxes = new ArrayList<CheckBox>();
		
		try {
			while (tsID.next()) {
				studentIdIgnore.add(tsID.getString(2));
			}
			
			ResultSet allStudents = studentConnection.findAll();
			
			while (allStudents.next()) {
				int matches = 0;
				for (int i = 0; i < studentIdIgnore.size(); i++) {
					if (allStudents.getString(1).equals(studentIdIgnore.get(i))) {
						matches++;
					}
				}
				if (matches == 0) {
					idsInclude.add(allStudents.getString(1));
				}
			}
			
			Dialog<ArrayList<String>> dialog = new Dialog<>();
			
			dialog.setTitle("Assign Personal Tutors");
			dialog.setHeaderText("Select students to assign to current personal tutor");
			
			ButtonType functionButtonType = new ButtonType("Assign", ButtonData.OK_DONE);
			dialog.getDialogPane().getButtonTypes().addAll(functionButtonType);
			
			VBox content = new VBox();
			
			for (int i = 0; i < idsInclude.size(); i++) {
				ResultSet studentInfo = studentConnection.findAllWhere("student_id", idsInclude.get(i));
				
				HBox row = new HBox();
				
				while (studentInfo.next()) {
					
					CheckBox cb = new CheckBox();
					cb.setId(studentInfo.getString(1));
					
					checkBoxes.add(cb);
					
					row.getChildren().addAll(cb, new Label(studentInfo.getString(4) + " " + studentInfo.getString(5) + " " + studentInfo.getString(6)));
				}
				content.getChildren().add(row);
			}
			
			dialog.getDialogPane().setContent(content);
			
			dialog.setResultConverter(dialogButton -> {
			    if (dialogButton == functionButtonType) {
			    	ArrayList<String> dataExtract = new ArrayList<String>();
			    	for (int i = 0; i < checkBoxes.size(); i++) {
			    		if (checkBoxes.get(i).isSelected()) {
					    	dataExtract.add(checkBoxes.get(i).getId());
			    		}
			    	}
			        return dataExtract;
			    }
			    return null;
			});
			
			Optional<ArrayList<String>> result = dialog.showAndWait();
			
			try {
				result.ifPresent(details -> {
					for (int i = 0; i < details.size(); i++) {
						returnNames.add(details.get(i));
					}
				});
			} catch (Exception e) {
				
			}
			
			
			
			return returnNames;
			
		} catch (Exception e) {
			System.out.println("Error selecting students to ignore: " + e);
			e.printStackTrace();
		}
	
		return returnNames;
	}
	
	public void multipleStudentDelete(SQLTable studentConnection, SQLTable tutorialStudentConnections, int tutorID) {
		
		ResultSet studentsList = tutorialStudentConnections.findAllWhere("tutor_id", tutorID);
		
		ArrayList<CheckBox> checkBoxes = new ArrayList<CheckBox>();
		
		ArrayList<String> studentIDList = new ArrayList<String>();
		
		try {
			while (studentsList.next()) {
				studentIDList.add(studentsList.getString(2));
			}
			
			Dialog<ArrayList<String>> dialog = new Dialog<>();
			
			dialog.setTitle("Remove students");
			dialog.setHeaderText("Remove students from current personal tutor");
			
			ButtonType functionButtonType = new ButtonType("Remove", ButtonData.OK_DONE);
			dialog.getDialogPane().getButtonTypes().addAll(functionButtonType);
			
			VBox content = new VBox();
			
			for (int i = 0; i < studentIDList.size(); i++) {
				ResultSet studentInfo = studentConnection.findAllWhere("student_id", studentIDList.get(i));
				
				HBox row = new HBox();
				
				while (studentInfo.next()) {
					
					CheckBox cb = new CheckBox();
					cb.setId(studentInfo.getString(1));
					
					checkBoxes.add(cb);
					
					row.getChildren().addAll(cb, new Label(studentInfo.getString(4) + " " + studentInfo.getString(5) + " " + studentInfo.getString(6)));
				}
				content.getChildren().add(row);
			}
			
			dialog.getDialogPane().setContent(content);
			
			dialog.setResultConverter(dialogButton -> {
			    if (dialogButton == functionButtonType) {
			    	ArrayList<String> dataExtract = new ArrayList<String>();
			    	for (int i = 0; i < checkBoxes.size(); i++) {
			    		if (checkBoxes.get(i).isSelected()) {
					    	dataExtract.add(checkBoxes.get(i).getId());
			    		}
			    	}
			        return dataExtract;
			    }
			    return null;
			});
			
			Optional<ArrayList<String>> result = dialog.showAndWait();
			
			ArrayList<String> namesToDelete = new ArrayList<String>();
			
			try {
				result.ifPresent(details -> {
					for (int i = 0; i < details.size(); i++) {
						namesToDelete.add(details.get(i));
					}
				});
			} catch (Exception e) {
				
			}
			
			for (int i = 0; i < namesToDelete.size(); i++) {
				tutorialStudentConnections.deleteTutorialStudents(Integer.parseInt(namesToDelete.get(i)));
			}
			
		} catch (Exception e) {
			System.out.println("Error selecting students to ignore: " + e);
			e.printStackTrace();
		}
	}

}
