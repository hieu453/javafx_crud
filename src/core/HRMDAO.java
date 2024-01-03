package core;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class HRMDAO {
	private static String jdbcURL;
	private static String jdbcUsername;
	private static String jdbcPassword;
	private static Connection jdbcConnection;
	
	public HRMDAO(String jdbcURL, String jdbcUsername, String jdbcPassword) {
		this.jdbcURL = jdbcURL;
		this.jdbcUsername = jdbcUsername;
		this.jdbcPassword = jdbcPassword;
	}
	
	protected static void connect() {
		try {
			if (jdbcConnection == null || jdbcConnection.isClosed()) {
				Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
				jdbcConnection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
			}
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
	protected static void disconnect() {
		try {
			if (!jdbcConnection.isClosed() || jdbcConnection != null) {
				jdbcConnection.close();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static boolean insertStudent(Student student) {
		String sql = "insert into students (code, fullname, class) values (?, ?, ?)";
		
		connect();
	
		boolean rowInserted = false;
		try {
			PreparedStatement statement = jdbcConnection.prepareStatement(sql);
			statement.setString(1, student.getCode());
			statement.setString(2, student.getFullname());
			statement.setString(3, student.getClass_());
			
			rowInserted = statement.executeUpdate() > 0;
			statement.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		disconnect();
		
		return rowInserted;
	}
	
	public static List<Student> listAllStudent() {
		List<Student> listStudent = new ArrayList<>();
		
		String sql = "select * from students";
		
		connect();
		
		try {
			Statement statement = jdbcConnection.createStatement();
			ResultSet results = statement.executeQuery(sql);
			
			while (results.next()) {
				String studentCode = results.getString("code");
				String studentFullname = results.getString("fullname");
				String studentClass = results.getString("class");
				Student student = new Student(studentCode);
				student.setFullname(studentFullname);
				student.setClass_(studentClass);
				listStudent.add(student);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return listStudent;
	}
	
	public static boolean deleteStudent(String code) {
		String sql = "delete from students where code = ?";
		
		connect();
		
		boolean rowDeleted = false;
		try {
			PreparedStatement statement = jdbcConnection.prepareStatement(sql);
			
			statement.setString(1, code);
			
			rowDeleted = statement.executeUpdate() > 0;
			statement.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		disconnect();
		
		return rowDeleted;
	}
	
	public static boolean updateStudent(Student student, String oldCode) {
		String sql = "update students set code = ?, fullname = ?, class = ? where code = ?";
		
		connect();
		
		boolean rowUpdated = false;
		try {
			PreparedStatement statement = jdbcConnection.prepareStatement(sql);
			statement.setString(1, student.getCode());
			statement.setString(2, student.getFullname());
			statement.setString(3, student.getClass_());
			statement.setString(4, oldCode);
			
			rowUpdated = statement.executeUpdate() > 0;
			
			statement.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		disconnect();
		
		return rowUpdated;
	}
	
	public static Student getStudent(String code) {
		Student student = null;
        String sql = "select * from students where code = ?";
         
        connect();
         
        try {
			PreparedStatement statement = jdbcConnection.prepareStatement(sql);
			statement.setString(1, code);
			 
			ResultSet result = statement.executeQuery();
			 
			if (result.next()) {
			    String studentCode = result.getString("code");
			    String studentFullname = result.getString("fullname");
			    String studentClass = result.getString("class");
			    student = new Student(studentCode);
			    student.setFullname(studentFullname);
			    student.setClass_(studentClass);
			}
			 
			result.close();
			statement.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
         
        return student;
	}
	
	public static Lecturer getLecturer(String code) {
		Lecturer lecturer = null;
        String sql = "select * from lecturers where code = ?";
         
        connect();
         
        try {
			PreparedStatement statement = jdbcConnection.prepareStatement(sql);
			statement.setString(1, code);
			 
			ResultSet result = statement.executeQuery();
			 
			if (result.next()) {
			    String lecturerCode = result.getString("code");
			    String lecturerFullname = result.getString("fullname");
			    String lecturerPassword = result.getString("password");
			    lecturer = new Lecturer(lecturerCode, lecturerPassword);
			    lecturer.setFullname(lecturerFullname);
			}
			 
			result.close();
			statement.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
         
        return lecturer;
	}
	
	public static boolean login(Human hm) {
		String sql = null;
		
		connect();
		
		if (hm instanceof Student) {
			sql = "select * from students where code = ?";
			try {
				PreparedStatement statement = jdbcConnection.prepareStatement(sql);
				statement.setString(1, ((Student)hm).getCode());
				 
				ResultSet result = statement.executeQuery();
				 
				if (result.next()) {
				   return true;
				}
				 
				result.close();
				statement.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			sql = "select * from lecturers where code = ? and password = ?";
			try {
				PreparedStatement statement = jdbcConnection.prepareStatement(sql);
				statement.setString(1, ((Lecturer)hm).getCode());
				statement.setString(2, ((Lecturer)hm).getPassword());
				 
				ResultSet result = statement.executeQuery();
				 
				if (result.next()) {
				   return true;
				}
				 
				result.close();
				statement.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return false;
	}
}
