package com.verinite.assetmangementtool.model;

public class LoginDto {
private String empId;
	private String password;
	
	
	public LoginDto()
	{
		
	}


	public LoginDto(String empId, String password) {
		super();
		this.empId = empId;
		this.password = password;
	}


	public String getEmpId() {
		return empId;
	}


	public void setEmpId(String empId) {
		this.empId = empId;
	}


	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}

	

	
}
