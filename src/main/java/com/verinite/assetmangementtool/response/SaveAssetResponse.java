package com.verinite.assetmangementtool.response;

import lombok.Data;

import java.util.Date;

@Data
public class SaveAssetResponse {

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
    private Date returnDate;
    private String addedBy;
    private Date assignedDate;
    private String assignedBy;
    private String assertSourcedBy;

}
