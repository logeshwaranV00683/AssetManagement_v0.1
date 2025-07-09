package com.verinite.assetmangementtool.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CountOfAssetsDTO {

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

    private int assignedLaptopCount;
    private int assignedBagCount;
    private int assignedMouseCount;
    private int assignedHeadphonesCount;
    private int assignedLaptopChargerCount;
    private int assignedDataCardCount;
    private int assignedMobileCount;
    private int assignedCameraCount;
    private int assignedProjectorCount;
    private int assignedFireWallCount;
    private int assignedSwitchCount;
    private int assignedDvrCount;
    private int assignedSpeakerCount;


    @Override
    public String toString() {
        return "CountOfAssetsDTO{" +
                "location='" + location + '\'' +
                ", laptopCount=" + laptopCount +
                ", bagCount=" + bagCount +
                ", mouseCount=" + mouseCount +
                ", headPhonesCount=" + headPhonesCount +
                ", laptopChargerCount=" + laptopChargerCount +
                ", dataCardCount=" + dataCardCount +
                ", mobileCount=" + mobileCount +
                ", cameraCount=" + cameraCount +
                ", projectorCount=" + projectorCount +
                ", fireWallCount=" + fireWallCount +
                ", switchCount=" + switchCount +
                ", dvrCount=" + dvrCount +
                ", speakerCount=" + speakerCount +
                ", unAssignedLaptopCount=" + unAssignedLaptopCount +
                ", unAssignedBagCount=" + unAssignedBagCount +
                ", unAssignedMouseCount=" + unAssignedMouseCount +
                ", unAssignedHeadphonesCount=" + unAssignedHeadphonesCount +
                ", unAssignedLaptopChargerCount=" + unAssignedLaptopChargerCount +
                ", unAssignedDataCardCount=" + unAssignedDataCardCount +
                ", unAssignedMobileCount=" + unAssignedMobileCount +
                ", unAssignedCameraCount=" + unAssignedCameraCount +
                ", unAssignedProjectorCount=" + unAssignedProjectorCount +
                ", unAssignedFireWallCount=" + unAssignedFireWallCount +
                ", unAssignedSwitchCount=" + unAssignedSwitchCount +
                ", unAssignedDvrCount=" + unAssignedDvrCount +
                ", unAssignedSpeakerCount=" + unAssignedSpeakerCount +
                ", assignedLaptopCount=" + assignedLaptopCount +
                ", assignedBagCount=" + assignedBagCount +
                ", assignedMouseCount=" + assignedMouseCount +
                ", assignedHeadphonesCount=" + assignedHeadphonesCount +
                ", assignedLaptopChargerCount=" + assignedLaptopChargerCount +
                ", assignedDataCardCount=" + assignedDataCardCount +
                ", assignedMobileCount=" + assignedMobileCount +
                ", assignedCameraCount=" + assignedCameraCount +
                ", assignedProjectorCount=" + assignedProjectorCount +
                ", assignedFireWallCount=" + assignedFireWallCount +
                ", assignedSwitchCount=" + assignedSwitchCount +
                ", assignedDvrCount=" + assignedDvrCount +
                ", assignedSpeakerCount=" + assignedSpeakerCount +
                '}';
    }
}
