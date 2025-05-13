package com.verinite.assetmangementtool.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Setter
@Getter
@Entity
@Table(name = "tbl_assined_assets")
public class AssignedAssetsEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "assigned_assets_id")
	private int assignedAssetsId;

	private Integer assetId;

	private String assetName;

	private String serialNumber;

	private String empId;

	private String status;

	private String type;

	private String purchaseDate;

	private String warrantyDate;

	private String location;

	private int locCode;

	private String modelName;

	private String operatingSystem;
	private Date returnDate;
	private String addedBy;
	private Date assignedDate;
	private String assignedBy;

	public int getLocCode() {
		return locCode;
	}

	public void setLocCode(int locCode) {
		this.locCode = locCode;
	}

	public int getAssignedAssetsId() {
		return assignedAssetsId;
	}

	public void setAssignedAssetsId(int assignedAssetsId) {
		this.assignedAssetsId = assignedAssetsId;
	}

	public Integer getAssetId() {
		return assetId;
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

	public void setAssetId(Integer assetId) {
		this.assetId = assetId;
	}

}
