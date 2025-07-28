package com.verinite.assetmanagementtool.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;

@Data
public class SaveAssetResponse {

    private int assetId;
    @NotBlank
    @Pattern(regexp = "^(\\w+)?$", message = "Must be a Alphanumeric & _")
    private String assetName;
    @NotBlank
    @Pattern(regexp = "^(\\w+)?$", message = "Must be a Alphanumeric & _")
    private String serialNumber;
    @NotBlank
    @Pattern(regexp = "^(V\\d{5})?$", message = "Must be V followed by 5 digits or empty")
    private String empId;
    private String status;
    @NotBlank
    @Pattern(regexp = "^(\\w+)?$", message = "Must be a Alphanumeric & _")
    private String type;
    @Pattern(regexp = "^(\\d{4}-\\d{2}-\\d{2})?$", message = "Date must be in format yyyy-MM-dd")
    private String purchaseDate;
    @Pattern(regexp = "^(\\d{4}-\\d{2}-\\d{2})?$", message = "Date must be in format yyyy-MM-dd")
    private String warrantyDate;
    @NotBlank
    @Pattern(regexp = "^(\\w+)?$", message = "Must be a Alphanumeric & _")
    private String location;
    @NotNull
    private Integer locCode;
    @NotBlank
    @Pattern(regexp = "^(\\w+)?$", message = "ModelName must be alphanumeric & _")
    private String modelName;
    private String operatingSystem;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate returnDate;
    @NotBlank
    @Pattern(regexp = "^(V\\d{5})?$", message = "Must be V followed by 5 digits or empty")
    private String addedBy;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate assignedDate;
    @Pattern(regexp = "^(V\\d{5})?$", message = "Must be V followed by 5 digits or empty")
    private String assignedBy;
    @NotBlank
    @Pattern(regexp = "^(\\w+)?$", message = "Must be a Alphanumeric & _")
    private String assertSourcedBy;
}
