package core;

public class Human {
	protected String code;
	protected String fullname;
	
	public Human() {
		
	}
	
	public Human(String code) {
		this.code = code;
	}

	public String getFullname() {
		return fullname;
	}
	
	public void setFullname(String fullname) {
		this.fullname = fullname;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
}
