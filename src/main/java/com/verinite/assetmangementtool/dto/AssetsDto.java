package com.verinite.assetmangementtool.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssetsDto {

    private int assetId;
    private String assetName;
    private String serialNumber;
    private String empId;
    private String status;
    private String type;
    private String purchaseDate;
    private String warrantyDate;
    private String location;
    private Integer locCode;
    private String modelName;
    private String operatingSystem;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate returnDate;
    private String addedBy;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate assignedDate;
    private String assignedBy;
    private String assetSourcedBy;

    @Override
    public String toString() {
        return "AssetsDto{" +
                "assetId=" + assetId +
                ", assetName='" + assetName + '\'' +
                ", serialNumber='" + serialNumber + '\'' +
                ", empId='" + empId + '\'' +
                ", status='" + status + '\'' +
                ", type='" + type + '\'' +
                ", purchaseDate='" + purchaseDate + '\'' +
                ", warrantyDate='" + warrantyDate + '\'' +
                ", location='" + location + '\'' +
                ", locCode=" + locCode +
                ", modelName='" + modelName + '\'' +
                ", operatingSystem='" + operatingSystem + '\'' +
                ", returnDate=" + returnDate +
                ", addedBy='" + addedBy + '\'' +
                ", assignedDate=" + assignedDate +
                ", assignedBy='" + assignedBy + '\'' +
                ", assetSourcedBy='" + assetSourcedBy + '\'' +
                '}';
    }
}