package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class SQLTable {
	private String table;
	private Statement stmt;
	
	
	public SQLTable(String table) {
		try {
			DriverManager.setLoginTimeout(1);
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con=DriverManager.getConnection("jdbc:mysql://v.je:3306/groupProjectY2", "student", "student");
			Statement stmt = con.createStatement();
			this.stmt = stmt;
		} catch (Exception e) {
			
		}
		
		this.table = table;
	}
	
	public void insert(String column1, String column2) {
		try {
			stmt.execute("INSERT INTO " + this.table + " VALUES (\"" + column1 + "\", \"" + column2 + "\")");
		} catch (SQLException e) {
			System.out.println("Error: " + e);
		}
	}
	
	public void insert(int column1, String column2) {
		try {
			stmt.execute("INSERT INTO " + this.table + " VALUES (" + column1 + ", \"" + column2 + "\")");
		} catch (SQLException e) {
			System.out.println("Error: " + e);
		}
	}
	
	public void insert(String column1, String column2, String column3) {
		try {
			stmt.execute("INSERT INTO " + this.table + " VALUES (\"" + column1 + "\", \"" + column2 + "\", \"" + column3 + "\")");
		} catch (SQLException e) {
			System.out.println("Error: " + e);
		}
	}
	
	public void insert(int column1, String column2, String column3) {
		try {
			stmt.execute("INSERT INTO " + this.table + " VALUES (" + column1 + ", \"" + column2 + "\", \"" + column3 + "\")");
		} catch (SQLException e) {
			System.out.println("Error: " + e);
		}
	}
	
	public void insert(String column1, String column2, String column3, String column4) {
		try {
			stmt.execute("INSERT INTO " + this.table + " VALUES (\"" + column1 + "\", \"" + column2 + "\", \"" + column3 + "\", \"" + column4 + "\")");
		} catch (SQLException e) {
			System.out.println("Error: " + e);
		}
	}
	
	public void insert(String column1, String column2, String column3, String column4, String column5) {
		try {
			stmt.execute("INSERT INTO " + this.table + " VALUES (\"" + column1 + "\", \"" + column2 + "\", \"" + column3 + "\", \"" + column4 + "\", \"" + column5 + "\")");
		} catch (SQLException e) {
			System.out.println("Error: " + e);
		}
	}
	
	public void insert(String column1, String column2, String column3, String column4, String column5, String column6) {
		try {
			stmt.execute("INSERT INTO " + this.table + " VALUES (\"" + column1 + "\", \"" + column2 + "\", \"" + column3 + "\", \"" + column4 + "\", \"" + column5 + "\", \"" + column6 + "\")");
		} catch (SQLException e) {
			System.out.println("Error: " + e);
		}
	}
	
	public void updateTimetableModule(String column1, String column2, String column3, String column4) {
		if (column2.equals("")) {
			try {
				stmt.execute("DELETE FROM timetables_modules WHERE timetable_id = " + column1 + " AND time = \"" + column3 + "\" AND week_day = \"" + column4 + "\"");
			} catch (Exception e) {
				System.out.println("Error1: " + e);
			}
		} else {
			try {
				ResultSet tmFound = stmt.executeQuery("SELECT timetable_id, module_code FROM timetables_modules WHERE timetable_id = " + column1 + " AND time = \"" + column3 + "\" AND week_day = \"" + column4 + "\"");
				
				if (tmFound.next()) {
					stmt.execute("UPDATE timetables_modules SET module_code = \"" + column2 + "\" WHERE timetable_id = " + column1 + " AND time = \"" + column3 + "\" AND week_day = \"" + column4 + "\"");
				} else {
					stmt.execute("INSERT INTO timetables_modules (timetable_id, module_code, time, week_day) VALUES (\"" + column1 + "\", \"" + column2 + "\", \"" + column3 + "\", \"" + column4 + "\")");
				}
			} catch (SQLException e) {
				System.out.println("Error2: " + e);
			}
		}
	}
	
	public int insertStudent(String column1, String column2, String column3, String column4, String column5, String column6, String column7, String column8, String column9, String column10, String column11) {
		try {
			stmt.execute("INSERT INTO contact_address (house, street, city, county, postcode) VALUES (\"" + column4 + "\", \"" + column5 + "\", \"" + column6 + "\", \"" + column7 + "\", \"" + column8 + "\")");
			ResultSet rs = stmt.executeQuery("SELECT address_id FROM contact_address WHERE house = \"" + column4 + "\" && street = \"" + column5 + "\" && city = \"" + column6 + "\" && county = \"" + column7 + "\" && postcode = \"" + column8 + "\"");
			int address_id = 0;
			while (rs.next()) {
				address_id = rs.getInt(1);
			}
			stmt.execute("INSERT INTO student_records (firstname, record_status, middle_name, surname, address_id, contact_phone, contact_email, course_code) VALUES (\"" + column1 + "\", \"PROVISIONAL\", \"" + column2 + "\", \"" + column3 + "\", " + address_id + ", \"" + column9 + "\", \"" + column10 + "\", \"" + column11 + "\")");
			ResultSet idSet = stmt.executeQuery("SELECT * FROM student_records WHERE firstname = \"" + column1 + "\" AND middle_name = \"" + column2 + "\" AND surname = \"" + column3 + "\" AND contact_email = \"" + column10 + "\"");
			while (idSet.next()) {
				return idSet.getInt(1);
			}
			return -1;
		} catch (SQLException e) {
			System.out.println("Error: " + e);
			e.printStackTrace();
			return -1;
		}
	}
	
	public void insertAG(String assignmentID, int studentID) {
		try {
			stmt.execute("INSERT INTO assignment_grade (assignment_id, student_id) VALUES (\"" + assignmentID + "\", " + studentID + ")");
		} catch (SQLException e) {
			System.out.println("Error inserting assignment grade: " + e);
			e.printStackTrace();
		}
	}
	
	public void updateAssignment(int id, String title, String description, int weighting) {
		try {
			stmt.execute("UPDATE assignments SET title = \"" + title + "\" WHERE assignment_id = " + id);
			stmt.execute("UPDATE assignments SET description = \"" + description + "\" WHERE assignment_id = " + id);
			stmt.execute("UPDATE assignments SET weighting = " + weighting + " WHERE assignment_id = " + id);
			
		} catch (Exception e) {
			System.out.println("Error updating assignment: " + e);
			e.printStackTrace();
		}
	}
	
	public void assignmentInsert(String column1, String column2, int column3, String column4) {
		try {
			stmt.execute("INSERT INTO assignments (title, description, weighting, module_code) VALUES (\"" + column1 + "\", \"" + column2 + "\", " + column3 + ", \"" + column4 + "\")");
		} catch (SQLException e) {
			System.out.println("Error inserting assignment: " + e);
			e.printStackTrace();
		}
	}
	
	public void insertStaff(String column1, String column2, String column3, String column4, String column5, String column6, String column7, String column8, String column9, String column10, String column11) {
		try {
			stmt.execute("INSERT INTO contact_address (house, street, city, county, postcode) VALUES (\"" + column4 + "\", \"" + column5 + "\", \"" + column6 + "\", \"" + column7 + "\", \"" + column8 + "\")");
			ResultSet rs = stmt.executeQuery("SELECT address_id FROM contact_address WHERE house = \"" + column4 + "\" && street = \"" + column5 + "\" && city = \"" + column6 + "\" && county = \"" + column7 + "\" && postcode = \"" + column8 + "\"");
			int address_id = 0;
			while (rs.next()) {
				address_id = rs.getInt(1);
			}
			stmt.execute("INSERT INTO staff (firstname, middle_name, surname, address_id, contact_phone, contact_email, role) VALUES (\"" + column1 + "\", \"" + column2 + "\", \"" + column3 + "\", " + address_id + ", \"" + column9 + "\", \"" + column10 + "\", \"" + column11 + "\")");
		} catch (Exception e) {
			System.out.println("Insert Staff Error: " + e);
			e.printStackTrace();
		}
	}
	
	public void updateStudent(String id, String column1, String column2, String column3, String column4, String column5, String column6, String column7, String column8, String column9, String column10, String column11) {
		try {
			ResultSet addressIDSet = stmt.executeQuery("SELECT address_id, course_code FROM student_records WHERE student_id = " + id);
			String addressID = "";
			String courseCode = "";
			while (addressIDSet.next()) {
				addressID = addressIDSet.getString(1);
				courseCode = addressIDSet.getString(2);
			}
			
			stmt.execute("UPDATE contact_address SET house = \"" + column4 + "\" WHERE address_id = " + addressID);
			stmt.execute("UPDATE contact_address SET street = \"" + column5 + "\" WHERE address_id = " + addressID);
			stmt.execute("UPDATE contact_address SET city = \"" + column6 + "\" WHERE address_id = " + addressID);
			stmt.execute("UPDATE contact_address SET county = \"" + column7 + "\" WHERE address_id = " + addressID);
			stmt.execute("UPDATE contact_address SET postcode = \"" + column8 + "\" WHERE address_id = " + addressID);
	
			stmt.execute("UPDATE student_records SET firstname = \"" + column1 + "\" WHERE student_id = " + id);
			stmt.execute("UPDATE student_records SET middle_name = \"" + column2 + "\" WHERE student_id = " + id);
			stmt.execute("UPDATE student_records SET surname = \"" + column3 + "\" WHERE student_id = " + id);
			stmt.execute("UPDATE student_records SET contact_phone = \"" + column9 + "\" WHERE student_id = " + id);
			stmt.execute("UPDATE student_records SET contact_email = \"" + column10 + "\" WHERE student_id = " + id);
			if (!courseCode.equals(column11)) {
				stmt.execute("UPDATE student_records SET course_code = \"" + column11 + "\" WHERE student_id = " + id);
				
				stmt.execute("DELETE FROM total_attendance WHERE student_id = " + id);
				ResultSet modules = stmt.executeQuery("SELECT * FROM course_modules WHERE course_code = \"" + column11 + "\"");
				ArrayList<String> moduleCodes = new ArrayList<String>();
				while (modules.next()) {
					moduleCodes.add(modules.getString(2));
				}
				
				for (int i = 0; i < moduleCodes.size(); i++) {
					stmt.execute("INSERT INTO total_attendance VALUES (" + id + ", + \"" + moduleCodes.get(i) + "\", 0, 0)");
				}
			}
		} catch (SQLException e) {
			System.out.println("Error: " + e);
			e.printStackTrace();
		}
	}
	
	public void updateStaff(String id, String column1, String column2, String column3, String column4, String column5, String column6, String column7, String column8, String column9, String column10, String column11) {
		try {
			ResultSet addressIDSet = stmt.executeQuery("SELECT address_id FROM staff WHERE staff_id = " + id);
			String addressID = "";
			while (addressIDSet.next()) {
				addressID = addressIDSet.getString(1);
			}
			stmt.execute("UPDATE contact_address SET house = \"" + column4 + "\" WHERE address_id = " + addressID);
			stmt.execute("UPDATE contact_address SET street = \"" + column5 + "\" WHERE address_id = " + addressID);
			stmt.execute("UPDATE contact_address SET city = \"" + column6 + "\" WHERE address_id = " + addressID);
			stmt.execute("UPDATE contact_address SET county = \"" + column7 + "\" WHERE address_id = " + addressID);
			stmt.execute("UPDATE contact_address SET postcode = \"" + column8 + "\" WHERE address_id = " + addressID);
	
			stmt.execute("UPDATE staff SET firstname = \"" + column1 + "\" WHERE staff_id = " + id);
			stmt.execute("UPDATE staff SET middle_name = \"" + column2 + "\" WHERE staff_id = " + id);
			stmt.execute("UPDATE staff SET surname = \"" + column3 + "\" WHERE staff_id = " + id);
			stmt.execute("UPDATE staff SET contact_phone = \"" + column9 + "\" WHERE staff_id = " + id);
			stmt.execute("UPDATE staff SET contact_email = \"" + column10 + "\" WHERE staff_id = " + id);
			stmt.execute("UPDATE staff SET role = \"" + column11 + "\" WHERE staff_id = " + id);
		} catch (SQLException e) {
			System.out.println("Error: " + e);
			e.printStackTrace();
		}
	}
	
	public void updatePersonalAttendance(String id, String module_code) {
		try {
			ResultSet studentAttendance = stmt.executeQuery("SELECT * FROM total_attendance WHERE student_id = " + id + " AND module_code = \"" + module_code + "\"");
			
			ArrayList<String> studentAttendanceList = new ArrayList<String>();
			while (studentAttendance.next()) {
				studentAttendanceList.add(Integer.toString((studentAttendance.getInt(3) + 1)));
			}
			
			for (int i = 0; i < studentAttendanceList.size(); i++) {
				stmt.execute("UPDATE total_attendance SET attended_classes = " + studentAttendanceList.get(i) + " WHERE student_id = " + id + " AND module_code = \"" + module_code + "\"");
			}
			
		} catch (Exception e) {
			System.out.println("Error: " + e);
			e.printStackTrace();
		}
	}
	
	public void updateClasses(String module_code) {
		try {
			ResultSet modulesRan = stmt.executeQuery("SELECT * FROM total_attendance WHERE module_code = \"" + module_code + "\"");
			
			int currentClassCount = 0;
			
			while (modulesRan.next()) {
				if (modulesRan.getInt(4) > currentClassCount) {
					currentClassCount = modulesRan.getInt(4);
				}
			}
			currentClassCount++;
			stmt.execute("UPDATE total_attendance SET total_number_of_classes = " + currentClassCount + " WHERE module_code = \"" + module_code + "\"");
			
		} catch (Exception e) {
			System.out.println("Error: " + e);
			e.printStackTrace();
		}
	}
	
	public void updateStudentStatus(String id, String record, String dormancy) {
		try {
			stmt.execute("UPDATE student_records SET record_status = \"" + record + "\" WHERE student_id = " + id);
			stmt.execute("UPDATE student_records SET dormancy_reason = \"" + dormancy + "\" WHERE student_id = " + id);
		} catch (SQLException e) {
			System.out.println("Error: " + e);
			e.printStackTrace();
		}
	}
	
	public void updateAssignmentGrade(int id, String grade) {
		try {
			stmt.execute("UPDATE assignment_grade SET grade = \"" + grade + "\" WHERE student_id = " + id);
		} catch (Exception e) {
			System.out.println("Updating Assignment Error: " + e);
			e.printStackTrace();
		}
	}
	
	public ResultSet findAll() {
		ResultSet rs = null;
		try {
			rs = stmt.executeQuery("SELECT * FROM "+this.table);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rs;
	}
	
	public ResultSet findAllWhere(String column, String pk) {
		ResultSet rs = null;
		try {
			rs = stmt.executeQuery("SELECT * FROM "+this.table+" WHERE "+column+" = \""+pk+"\"");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rs;
	}
	
	public ResultSet find(String column) {
		ResultSet rs = null;
		try {
			rs = stmt.executeQuery("SELECT " + column + " FROM " + this.table);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rs;
	}
	
	public ResultSet findAllWhere(String column, int pk) {
		ResultSet rs = null;
		try {
			rs = stmt.executeQuery("SELECT * FROM " + this.table + " WHERE " + column + " = " + pk);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rs;
	}
	
	public void insertTutor(int staffID) {
		try {
			ResultSet currentTutors = stmt.executeQuery("SELECT * FROM " + this.table);
			boolean present = false;
			
			while (currentTutors.next()) {
				if (currentTutors.getInt(2) == staffID) {
					present = true;
				}
			}
			
			if (!present) {
				stmt.execute("INSERT INTO " + this.table + " (staff_id) VALUES (" + staffID + ")");
			}
		} catch (Exception e) {
			System.out.println("Error: " + e);
		}
	}
	
	public void deleteTutorialStudents(int studentID) {
		try {
			stmt.execute("DELETE FROM tutorial_students WHERE student_id = " + studentID);
		} catch (Exception e) {
			System.out.println("Error: " + e);
		}
	}
}
