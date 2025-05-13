package com.verinite.assetmangementtool.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AssetCounterDto {
	private String location;
	private int unAssigned;
	private int total;

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public int getUnAssigned() {
		return unAssigned;
	}

	public void setUnAssigned(int unAssigned) {
		this.unAssigned = unAssigned;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

}
