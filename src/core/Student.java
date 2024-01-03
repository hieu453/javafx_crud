package core;

public class Student extends Human {
	private String class_;
	
	public Student() {
		
	}
	
	public Student(String code) {
		super(code);
	}
	
	public String getFullname() {
		return this.fullname;
	}
	
	public void setFullname(String fullname) {
		this.fullname = fullname;
	}
	
	public String getCode() {
		return this.code;
	}

	public String getClass_() {
		return class_;
	}

	public void setClass_(String class_) {
		this.class_ = class_;
	}
}
