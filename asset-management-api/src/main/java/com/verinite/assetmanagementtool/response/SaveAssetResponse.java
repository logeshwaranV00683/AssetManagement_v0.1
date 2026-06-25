package com.verinite.assetmanagementtool.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.Pattern;
import java.time.LocalDate;

@Data
public class SaveAssetResponse {

    private int assetId;

    @Pattern(
            regexp = "^(?=.{1,40}$)(?!.*([A-Za-z0-9_])\\1{3,})[A-Za-z0-9_]+$",
            message = "Must be alphanumeric/underscore, max 40 chars, no character repeated more than 3 times consecutively"
    )
    private String assetName;

    @Pattern(regexp = "^(?=.{1,40}$)\\w+$", message = "Must be a Alphanumeric & _")
    private String serialNumber;


    @Pattern(regexp = "^(V\\d{5})?$", message = "Must be V followed by 5 digits or empty")
    private String empId;

    @Pattern(regexp = "^(?i)(Assigned|Unassigned|Scrap)?$", message = "status can be Assigned or unassigned or scrap")
    private String status;

    @Pattern(
            regexp = "^(?=.{1,40}$)(?!.*([A-Za-z_])\\1{3,})[A-Za-z_ ]+$",
            message = "Only alphabets, spaces, and underscores allowed, max 40 chars, no character repeated more than 3 times consecutively"
    )
    private String type;


    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate purchaseDate;


    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate warrantyDate;


    @Pattern(
            regexp = "^(?=.{1,20}$)(?!.*([A-Za-z_])\\1{3,})[A-Za-z_ ]+$",
            message = "Only alphabets, spaces, and underscores allowed, max 20 chars, no character repeated more than 3 times consecutively"
    )
    private String location;


    @Pattern(regexp = "^(?=.{1,40}$)[A-Za-z0-9_ ]+$", message = "ModelName must be alphanumeric, spaces, or underscores, and up to 40 characters")
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

    @Pattern(
            regexp = "^(?=.{1,40}$)(?!.*([A-Za-z_])\\1{3,})[A-Za-z_ ]+$",
            message = "Only alphabets, spaces, and underscores allowed, max 40 chars, no character repeated more than 3 times consecutively"
    )
    private String assertSourcedBy;
}
