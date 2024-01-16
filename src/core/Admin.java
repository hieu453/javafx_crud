package core;

public class Admin extends Human {
	private String password;
	
	public Admin() {
		
	}
	
	public Admin(String code, String password) {
		super(code);
		this.password = password;
	}
	
	public String getCode() {
		return code;
	}
	
	public void setCode(String code) {
		this.code = code;
	}
	
	public String getFullname() {
		return this.fullname;
	}
	
	public void setFullname(String fullname) {
		this.fullname = fullname;
	}

	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
}
