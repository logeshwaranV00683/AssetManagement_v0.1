package com.verinite.assetmangementtool.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
public class AssignableAssetDto {

	private String empId;
	private String serialNumber;
	private Date assignedDate;
	private String assignedBy;

	public String getEmpId() {
		return empId;
	}

	public void setEmpId(String empId) {
		this.empId = empId;
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public Date getAssignedDate() {
		return assignedDate;
	}

	public void setAssignedDate(Date assignedDate) {
		this.assignedDate = assignedDate;
	}

	public String getAssignedBy() {
		return assignedBy;
	}

	public void setAssignedBy(String assignedBy) {
		this.assignedBy = assignedBy;
	}

}
