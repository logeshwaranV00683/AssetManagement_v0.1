package com.verinite.assetmangementtool.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "tbl_scraptable")
public class ScrapEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int scrapId;
	private String Assetname;
	private String SerialNo;
	private Date purchaseDate;
	private Date warrantyDate;
	private String users;
	private String status;
	private String type;
	private int assetId;

	public ScrapEntity(int scrapId, String assetname, String serialNo, Date purchaseDate, Date warrantyDate,
			String users, String status, String type, int assetId) {
		this.scrapId = scrapId;
		Assetname = assetname;
		SerialNo = serialNo;
		this.purchaseDate = purchaseDate;
		this.warrantyDate = warrantyDate;
		this.users = users;
		this.status = status;
		this.type = type;
		this.assetId = assetId;
	}

	public int getScrapId() {
		return scrapId;
	}

	public void setScrapId(int scrapId) {
		this.scrapId = scrapId;
	}

	public String getAssetname() {
		return Assetname;
	}

	public void setAssetname(String assetname) {
		Assetname = assetname;
	}

	public String getSerialNo() {
		return SerialNo;
	}

	public void setSerialNo(String serialNo) {
		SerialNo = serialNo;
	}

	public Date getPurchaseDate() {
		return purchaseDate;
	}

	public void setPurchaseDate(Date purchaseDate) {
		this.purchaseDate = purchaseDate;
	}

	public Date getWarrantyDate() {
		return warrantyDate;
	}

	public void setWarrantyDate(Date warrantyDate) {
		this.warrantyDate = warrantyDate;
	}

	public String getUsers() {
		return users;
	}

	public void setUsers(String users) {
		this.users = users;
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

	public int getAssetId() {
		return assetId;
	}

	public void setAssetId(int assetId) {
		this.assetId = assetId;
	}

	@Override
	public String toString() {
		return "ScrapTable{" + "scrapId=" + scrapId + ", Assetname='" + Assetname + '\'' + ", SerialNo='" + SerialNo
				+ '\'' + ", purchaseDate=" + purchaseDate + ", warrantyDate=" + warrantyDate + ", users='" + users
				+ '\'' + ", status='" + status + '\'' + ", type='" + type + '\'' + ", assetId=" + assetId + '}';
	}

}
