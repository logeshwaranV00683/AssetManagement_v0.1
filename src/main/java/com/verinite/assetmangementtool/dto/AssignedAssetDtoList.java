package com.verinite.assetmangementtool.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssignedAssetDtoList {

    private int assignedAssetsId;
    private int assetId;
    private String assetName;
    private String serialNumber;
    private String empId;
    private String empFirstName;
    private String empLastName;
    private String status;
    private String type;
    private String purchaseDate;
    private String warrantyDate;
    private String location;
    private int locCode;
    private String modelName;
    private String operatingSystem;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate returnDate; // You can use LocalDateTime if you want more control over the date formatting
    private String addedBy;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate assignedDate; // LocalDateTime could be used here too
    private String assignedBy;

    private String assetSourcedBy;

}
