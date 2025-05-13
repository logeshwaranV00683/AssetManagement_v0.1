package com.verinite.assetmangementtool.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "tbl_Count")
public class CountOfAssets {

	@Id
	private String location;

	private int laptopCount;
	private int bagCount;
	private int mouseCount;
	private int headPhonesCount;
	private int laptopChargerCount;
	private int dataCardCount;
	private int mobileCount;
	private int cameraCount;
	private int projectorCount;
	private int fireWallCount;
	private int switchCount;
	private int dvrCount;
	private int speakerCount;

	private int unAssignedLaptopCount;
	private int unAssignedBagCount;
	private int unAssignedMouseCount;
	private int unAssignedHeadphonesCount;
	private int unAssignedLaptopChargerCount;
	private int unAssignedDataCardCount;
	private int unAssignedMobileCount;
	private int unAssignedCameraCount;
	private int unAssignedProjectorCount;
	private int unAssignedFireWallCount;
	private int unAssignedSwitchCount;
	private int unAssignedDvrCount;
	private int unAssignedSpeakerCount;

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public int getLaptopCount() {
		return laptopCount;
	}

	public void setLaptopCount(int laptopCount) {
		this.laptopCount = laptopCount;
	}

	public int getBagCount() {
		return bagCount;
	}

	public void setBagCount(int bagCount) {
		this.bagCount = bagCount;
	}

	public int getMouseCount() {
		return mouseCount;
	}

	public void setMouseCount(int mouseCount) {
		this.mouseCount = mouseCount;
	}

	public int getHeadPhonesCount() {
		return headPhonesCount;
	}

	public void setHeadPhonesCount(int headPhonesCount) {
		this.headPhonesCount = headPhonesCount;
	}

	public int getLaptopChargerCount() {
		return laptopChargerCount;
	}

	public void setLaptopChargerCount(int laptopChargerCount) {
		this.laptopChargerCount = laptopChargerCount;
	}

	public int getDataCardCount() {
		return dataCardCount;
	}

	public void setDataCardCount(int dataCardCount) {
		this.dataCardCount = dataCardCount;
	}

	public int getMobileCount() {
		return mobileCount;
	}

	public void setMobileCount(int mobileCount) {
		this.mobileCount = mobileCount;
	}

	public int getCameraCount() {
		return cameraCount;
	}

	public void setCameraCount(int cameraCount) {
		this.cameraCount = cameraCount;
	}

	public int getProjectorCount() {
		return projectorCount;
	}

	public void setProjectorCount(int projectorCount) {
		this.projectorCount = projectorCount;
	}

	public int getFireWallCount() {
		return fireWallCount;
	}

	public void setFireWallCount(int fireWallCount) {
		this.fireWallCount = fireWallCount;
	}

	public int getSwitchCount() {
		return switchCount;
	}

	public void setSwitchCount(int switchCount) {
		this.switchCount = switchCount;
	}

	public int getDvrCount() {
		return dvrCount;
	}

	public void setDvrCount(int dvrCount) {
		this.dvrCount = dvrCount;
	}

	public int getSpeakerCount() {
		return speakerCount;
	}

	public void setSpeakerCount(int speakerCount) {
		this.speakerCount = speakerCount;
	}

	public int getUnAssignedLaptopCount() {
		return unAssignedLaptopCount;
	}

	public void setUnAssignedLaptopCount(int unAssignedLaptopCount) {
		this.unAssignedLaptopCount = unAssignedLaptopCount;
	}

	public int getUnAssignedBagCount() {
		return unAssignedBagCount;
	}

	public void setUnAssignedBagCount(int unAssignedBagCount) {
		this.unAssignedBagCount = unAssignedBagCount;
	}

	public int getUnAssignedMouseCount() {
		return unAssignedMouseCount;
	}

	public void setUnAssignedMouseCount(int unAssignedMouseCount) {
		this.unAssignedMouseCount = unAssignedMouseCount;
	}

	public int getUnAssignedHeadphonesCount() {
		return unAssignedHeadphonesCount;
	}

	public void setUnAssignedHeadphonesCount(int unAssignedHeadphonesCount) {
		this.unAssignedHeadphonesCount = unAssignedHeadphonesCount;
	}

	public int getUnAssignedLaptopChargerCount() {
		return unAssignedLaptopChargerCount;
	}

	public void setUnAssignedLaptopChargerCount(int unAssignedLaptopChargerCount) {
		this.unAssignedLaptopChargerCount = unAssignedLaptopChargerCount;
	}

	public int getUnAssignedDataCardCount() {
		return unAssignedDataCardCount;
	}

	public void setUnAssignedDataCardCount(int unAssignedDataCardCount) {
		this.unAssignedDataCardCount = unAssignedDataCardCount;
	}

	public int getUnAssignedMobileCount() {
		return unAssignedMobileCount;
	}

	public void setUnAssignedMobileCount(int unAssignedMobileCount) {
		this.unAssignedMobileCount = unAssignedMobileCount;
	}

	public int getUnAssignedCameraCount() {
		return unAssignedCameraCount;
	}

	public void setUnAssignedCameraCount(int unAssignedCameraCount) {
		this.unAssignedCameraCount = unAssignedCameraCount;
	}

	public int getUnAssignedProjectorCount() {
		return unAssignedProjectorCount;
	}

	public void setUnAssignedProjectorCount(int unAssignedProjectorCount) {
		this.unAssignedProjectorCount = unAssignedProjectorCount;
	}

	public int getUnAssignedFireWallCount() {
		return unAssignedFireWallCount;
	}

	public void setUnAssignedFireWallCount(int unAssignedFireWallCount) {
		this.unAssignedFireWallCount = unAssignedFireWallCount;
	}

	public int getUnAssignedSwitchCount() {
		return unAssignedSwitchCount;
	}

	public void setUnAssignedSwitchCount(int unAssignedSwitchCount) {
		this.unAssignedSwitchCount = unAssignedSwitchCount;
	}

	public int getUnAssignedDvrCount() {
		return unAssignedDvrCount;
	}

	public void setUnAssignedDvrCount(int unAssignedDvrCount) {
		this.unAssignedDvrCount = unAssignedDvrCount;
	}

	public int getUnAssignedSpeakerCount() {
		return unAssignedSpeakerCount;
	}

	public void setUnAssignedSpeakerCount(int unAssignedSpeakerCount) {
		this.unAssignedSpeakerCount = unAssignedSpeakerCount;
	}

}
