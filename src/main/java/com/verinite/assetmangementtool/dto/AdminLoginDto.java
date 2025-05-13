package com.verinite.assetmangementtool.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminLoginDto {
    private String empId ;
    private String password;
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
