package com.verinite.assetmangementtool.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "tbl_Count")
@Data
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
}
