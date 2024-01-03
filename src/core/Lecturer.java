package core;

public class Lecturer extends Human {
	private String password;
	
	public Lecturer() {
		
	}
	
	public Lecturer(String code, String password) {
		super(code);
		this.password = password;
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

	public String getCode() {
		return code;
	}
	
	public void setCode(String code) {
		this.code = code;
	}
}
