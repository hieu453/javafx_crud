package core;

public final class UserSession {
	private static UserSession instance;
	
	private static String fullname;
	private static int role;
	
	private UserSession(String fullname, int role) {
		UserSession.fullname = fullname;
		UserSession.role = role;
	}
	
	public static UserSession getInstance(String fullname, int role) {
		if (instance == null) {
			instance = new UserSession(fullname, role);
		}
		return instance;
	}
	
	public static String getFullname() {
		return fullname;
	}
	
	public static int getRole() {
		return role;
	}
	
	public static void clearSession() {
		instance = null;
		fullname = "";
		role = -1;
	}
}
