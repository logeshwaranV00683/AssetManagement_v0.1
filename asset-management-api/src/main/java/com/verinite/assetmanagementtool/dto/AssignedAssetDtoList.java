package com.verinite.assetmanagementtool.dto;

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
    private String assetName;
    private String serialNumber;
    private String empId;
    private String empFirstName;
    private String empLastName;
    private String status;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate assignedDate; // LocalDateTime could be used here too
    private String assignedBy;
    private String type;

}
