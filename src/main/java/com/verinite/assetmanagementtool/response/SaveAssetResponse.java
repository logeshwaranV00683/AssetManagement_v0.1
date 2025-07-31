package com.verinite.assetmanagementtool.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.Pattern;
import java.time.LocalDate;

@Data
public class SaveAssetResponse {

    private int assetId;
    @Pattern(regexp = "^(\\w+)?$", message = "Must be a Alphanumeric & _")
    private String assetName;
    @Pattern(regexp = "^(\\w+)?$", message = "Must be a Alphanumeric & _")
    private String serialNumber;
    @Pattern(regexp = "^(V\\d{5})?$", message = "Must be V followed by 5 digits or empty")
    private String empId;
    @Pattern(regexp = "^(?i)(Assigned|Unassigned|Scarp)?$", message = "status can be Assigned or unassigned or scrap")
    private String status;
    @Pattern(regexp = "^(\\w+)?$", message = "Must be a Alphanumeric & _")
    private String type;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate purchaseDate;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate warrantyDate;
    @Pattern(regexp = "^(\\w+)?$", message = "Must be a Alphanumeric & _")
    private String location;
    @Pattern(regexp = "^(\\w+)?$", message = "ModelName must be alphanumeric & _")
    private String modelName;
    @Pattern(regexp = "^(\\w+)?$", message = "operatingSystem must be alphanumeric & _")
    private String operatingSystem;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate returnDate;
    @Pattern(regexp = "^(V\\d{5})?$", message = "Must be V followed by 5 digits or empty")
    private String addedBy;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate assignedDate;
    @Pattern(regexp = "^(V\\d{5})?$", message = "Must be V followed by 5 digits or empty")
    private String assignedBy;
    @Pattern(regexp = "^(\\w+)?$", message = "Must be a Alphanumeric & _")
    private String assertSourcedBy;
}
