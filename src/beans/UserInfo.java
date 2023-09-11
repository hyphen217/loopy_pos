package beans;

public class UserInfo {
	private String employeeCode;
	private String name;
	private String position;
	private String password;
	
	public UserInfo() {	}

	public UserInfo(String employeeCode, String name, String position) {
		this.employeeCode = employeeCode;
		this.name = name;
		this.position = position;
	}
	
	public UserInfo(String employeeCode, String password, String name, String position) {
		this.employeeCode = employeeCode;
		this.password = password;
		this.name = name;
		this.position = position;
	}

	public void setEmployeeCode(String employeeCode) {
		this.employeeCode = employeeCode;
	}

	public String getEmployeeCode() {
		return this.employeeCode;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}

	public String getPassword() {
		return this.password;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getPosition() {
		return this.position;
	}
	

}