package com.verinite.assetmanagementtool.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssetExportDto {

    private String assetName;
    private String serialNumber;
    private String assignedTo;
    private String status;
    private String type;
    private String purchaseDate;
    private String warrantyDate;
    private String location;
    private String modelName;
    private String operatingSystem;
    private String returnDate;
    private String addedBy;
    private String assignedDate;
    private String assignedBy;
    private String assetSourcedBy;
}
