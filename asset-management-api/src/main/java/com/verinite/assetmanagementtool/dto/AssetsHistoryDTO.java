package com.verinite.assetmanagementtool.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class AssetsHistoryDTO {
    private long historyId;
    private int assetId;
    private String assetName;
    private String serialNumber;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date returnDate;
    private String status;
    private String type;
    private String purchaseDate;
    private String warrantyDate;
    private String empId;
    private String location;
    private String modelName;
    private String operatingSystem;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date assignedDate;
    private String assignedBy;

    private String assetSourcedBy;

}
