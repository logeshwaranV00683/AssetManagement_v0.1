package com.verinite.assetmangementtool.dto;

import java.util.Date;

public class AssignedAssetDtoList {

	private int assignedAssetsId;
	private int assetId;
	private String assetName;
	private String serialNumber;
	private String empId;
	private String empFirstName;
	private String empLastName;
	private String status;
	private String type;
	private String purchaseDate;
	private String warrantyDate;
	private String location;
	private int locCode;
	private String modelName;
	private String operatingSystem;
	private Date returnDate; // You can use LocalDateTime if you want more control over the date formatting
	private String addedBy;
	private Date assignedDate; // LocalDateTime could be used here too
	private String assignedBy;

	private String assertSourcedBy;

	public int getAssignedAssetsId() {
		return assignedAssetsId;
	}

	public void setAssignedAssetsId(int assignedAssetsId) {
		this.assignedAssetsId = assignedAssetsId;
	}

	public String getEmpFirstName() {
		return empFirstName;
	}

	public void setEmpFirstName(String empFirstName) {
		this.empFirstName = empFirstName;
	}

	public String getEmpLastName() {
		return empLastName;
	}

	public void setEmpLastName(String empLastName) {
		this.empLastName = empLastName;
	}

	public int getAssetId() {
		return assetId;
	}

	public void setAssetId(int assetId) {
		this.assetId = assetId;
	}

	public String getAssetName() {
		return assetName;
	}

	public void setAssetName(String assetName) {
		this.assetName = assetName;
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public String getEmpId() {
		return empId;
	}

	public void setEmpId(String empId) {
		this.empId = empId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getPurchaseDate() {
		return purchaseDate;
	}

	public void setPurchaseDate(String purchaseDate) {
		this.purchaseDate = purchaseDate;
	}

	public String getWarrantyDate() {
		return warrantyDate;
	}

	public void setWarrantyDate(String warrantyDate) {
		this.warrantyDate = warrantyDate;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public int getLocCode() {
		return locCode;
	}

	public void setLocCode(int locCode) {
		this.locCode = locCode;
	}

	public String getModelName() {
		return modelName;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;
	}

	public String getOperatingSystem() {
		return operatingSystem;
	}

	public void setOperatingSystem(String operatingSystem) {
		this.operatingSystem = operatingSystem;
	}

	public Date getReturnDate() {
		return returnDate;
	}

	public void setReturnDate(Date returnDate) {
		this.returnDate = returnDate;
	}

	public String getAddedBy() {
		return addedBy;
	}

	public void setAddedBy(String addedBy) {
		this.addedBy = addedBy;
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

	public String getAssertSourcedBy() {
		return assertSourcedBy;
	}

	public void setAssertSourcedBy(String assertSourcedBy) {
		this.assertSourcedBy = assertSourcedBy;
	}

}
