package com.verinite.assetmangementtool.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;

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
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate returnDate;
    private String addedBy;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate assignedDate;
    private String assignedBy;
    private String assertSourcedBy;

}
