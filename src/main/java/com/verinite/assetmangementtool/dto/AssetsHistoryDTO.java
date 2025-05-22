package com.verinite.assetmangementtool.dto;

import lombok.Data;

import java.util.Date;
@Data
public class AssetsHistoryDTO {
    private long historyId;
    private int assetId;
    private String assetName;
    private String serialNumber;
    private Date returnDate;
    private String status;
    private String type;
    private String purchaseDate;
    private String warrantyDate;
    private String empId;
    private String location;
    private int locCode;
    private String modelName;
    private String operatingSystem;
    private Date assignedDate;
    private String assignedBy;

    private String assertSourcedBy;

}
