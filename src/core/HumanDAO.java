package core;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class HumanDAO {
	private static String jdbcURL;
	private static String jdbcUsername;
	private static String jdbcPassword;
	private static Connection jdbcConnection;
	
	public HumanDAO(String jdbcURL, String jdbcUsername, String jdbcPassword) {
		HumanDAO.jdbcURL = jdbcURL;
		HumanDAO.jdbcUsername = jdbcUsername;
		HumanDAO.jdbcPassword = jdbcPassword;
	}
	
	protected static void connect() {
		try {
			if (jdbcConnection == null || jdbcConnection.isClosed()) {
				Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
//				Class.forName("com.mysql.cj.jdbc.Driver");
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
	
	public static boolean insertHuman(Human hm) {
		String sql = null;
		
		connect();
	
		boolean rowInserted = false;
		
		if (hm instanceof User) {
			sql = "insert into users (code, fullname) values (?, ?)";
			try {
				PreparedStatement statement = jdbcConnection.prepareStatement(sql);
				statement.setString(1, hm.getCode());
				statement.setString(2, hm.getFullname());
				
				rowInserted = statement.executeUpdate() > 0;
				statement.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			sql = "insert into admins (code, fullname, password) values (?, ?, ?)";
			try {
				PreparedStatement statement = jdbcConnection.prepareStatement(sql);
				statement.setString(1, hm.getCode());
				statement.setString(2, hm.getFullname());
				statement.setString(3, ((Admin)hm).getPassword());
				
				rowInserted = statement.executeUpdate() > 0;
				statement.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		disconnect();
		
		return rowInserted;
	}
	
	public static User getUser(String code) {
		User user = null;
        String sql = "select * from users where code = ?";
         
        connect();
         
        try {
			PreparedStatement statement = jdbcConnection.prepareStatement(sql);
			statement.setString(1, code);
			 
			ResultSet result = statement.executeQuery();
			 
			if (result.next()) {
			    String userCode = result.getString("code");
			    String userFullname = result.getString("fullname");
			    user = new User(userCode);
			    user.setFullname(userFullname);
			}
			 
			result.close();
			statement.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
         
        return user;
	}
	
	public static Admin getAdmin(String code) {
		Admin admin = null;
        String sql = "select * from admins where code = ?";
         
        connect();
         
        try {
			PreparedStatement statement = jdbcConnection.prepareStatement(sql);
			statement.setString(1, code);
			 
			ResultSet result = statement.executeQuery();
			 
			if (result.next()) {
			    String adminCode = result.getString("code");
			    String adminFullname = result.getString("fullname");
			    String adminPassword = result.getString("password");
			    admin = new Admin(adminCode, adminPassword);
			    admin.setFullname(adminFullname);
			}
			 
			result.close();
			statement.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
         
        return admin;
	}
	
	public static boolean login(Human hm) {
		String sql = null;
		
		connect();
		
		if (hm instanceof User) {
			sql = "select * from users where code = ?";
			try {
				PreparedStatement statement = jdbcConnection.prepareStatement(sql);
				statement.setString(1, ((User)hm).getCode());
				 
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
			sql = "select * from admins where code = ? and password = ?";
			try {
				PreparedStatement statement = jdbcConnection.prepareStatement(sql);
				statement.setString(1, ((Admin)hm).getCode());
				statement.setString(2, ((Admin)hm).getPassword());
				 
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
